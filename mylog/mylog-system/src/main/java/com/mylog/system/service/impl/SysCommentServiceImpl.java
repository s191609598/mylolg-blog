package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.DateUtil;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysCommentDao;
import com.mylog.system.entity.comment.SysComment;
import com.mylog.system.entity.comment.dto.CommentHomeDTO;
import com.mylog.system.entity.comment.dto.queryCommentDTO;
import com.mylog.system.entity.comment.vo.CommentHomeTreeVO;
import com.mylog.system.entity.user.SysUser;
import com.mylog.system.redis.RedisCacheUtils;
import com.mylog.system.service.SysCommentService;
import com.mylog.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pss
 * @description 针对表【sys_comment(评论表)】的数据库操作Service实现
 * @createDate 2025-02-21 15:34:21
 */
@Service
public class SysCommentServiceImpl extends ServiceImpl<SysCommentDao, SysComment> implements SysCommentService {
    @Resource
    private SysUserService sysUserService;


    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    public boolean submitComment(CommentHomeDTO dto) {
        // 从 RequestContextHolder 中获取 ServletRequestAttributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取 HttpServletRequest 对象
        HttpServletRequest request = attributes.getRequest();
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        AssertUtils.isNull(dto, ErrorCode.PARAMS_ERROR);
        Long articleId = dto.getArticleId();
        SysComment sysComment = ConvertUtils.sourceToTarget(dto, SysComment.class);
        if (StringUtils.isNotNull(dto.getPid())) {
            SysComment byId = this.getById(dto.getPid());
            AssertUtils.isNull(byId, ErrorCode.PARAMS_ERROR);
            if (StringUtils.isNotNull(byId.getRootId())) {
                sysComment.setRootId(byId.getRootId());
            } else {
                sysComment.setRootId(byId.getId());
            }
        }
        sysComment.setCreateBy(userId);
        String ip = IpUtils.getIp(request);
        sysComment.setIp(ip);
        String ipAddr = IpUtils.getIpAddr(ip);
        sysComment.setIpSource(ipAddr);
        String os = IpUtils.getOs(request);
        sysComment.setOs(os);
        sysComment.setCreateTime(new Date());
        boolean save = this.save(sysComment);
        if (save) {
            CommentHomeTreeVO vo = ConvertUtils.sourceToTarget(sysComment, CommentHomeTreeVO.class);
            this.setUserName(vo);
            if (StringUtils.isNotNull(sysComment.getRootId())) {
                Integer index = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_INDEX + sysComment.getRootId());
                CommentHomeTreeVO vo1 = (CommentHomeTreeVO) redisCacheUtils.getCacheIndexList(RedisConstants.REDIS_ARTICLE_COMMENT + articleId, index);
                if (StringUtils.isEmpty(vo1.getChildren())) {
                    vo1.setChildren(new ArrayList<>());
                    CommentHomeTreeVO node = new CommentHomeTreeVO();
                    BeanUtils.copyProperties(vo1, node);
                    node.setChildren(null);
                    vo1.getChildren().add(node);
                }
                vo1.getChildren().add(vo);
                redisCacheUtils.setCacheSetList(RedisConstants.REDIS_ARTICLE_COMMENT + articleId, index, vo1);
            } else {
                redisCacheUtils.setCacheRightPushList(RedisConstants.REDIS_ARTICLE_COMMENT + articleId, vo);
                redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_COMMENT_ROOT_SIZE + articleId);
                Long cacheListSize = redisCacheUtils.getCacheListSize(RedisConstants.REDIS_ARTICLE_COMMENT + articleId);
                Integer index = Math.toIntExact(cacheListSize - 1);
                redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_INDEX + sysComment.getId(), index);
            }
            redisCacheUtils.incrementValue(RedisConstants.REDIS_ARTICLE_COMMENT_TOTAL + articleId);
        }
        return save;
    }


    /**
     * 查询文章评论
     *
     * @param dto
     * @return
     */
    @Override
    public IPage<Tree<Long>> queryByArticleId(queryCommentDTO dto) {
        Long articleId = dto.getArticleId();
        AssertUtils.isNull(articleId, "文章id不能为空");
        int pageNo = dto.getPageNo();
        int pageSize = dto.getPageSize();
        Integer rootSize = redisCacheUtils.getCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_ROOT_SIZE + articleId);
        if (StringUtils.isNull(rootSize)) {
            rootSize = 0;
        }
        int mm = rootSize % pageSize;
        int ii = Math.floorDiv(rootSize, pageSize);
        ii = ii + mm;
        if (ii < pageNo) {
            return null;
        }
        List<Tree<Long>> treeNodes = null;
        List<CommentHomeTreeVO> vo = null;
        IPage<Tree<Long>> pageTree = new Page<>();
        IPage<SysComment> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        Long listSize = 0L;
        //分页
        int current = 0;
        int size = dto.getPageSize();
        if (pageNo != 1) {
            current = (pageNo - 1) * size;
            size = current + pageSize - 1;
        } else {
            size = pageSize - 1;
        }
        vo = redisCacheUtils.getCacheList(RedisConstants.REDIS_ARTICLE_COMMENT + articleId, current, size);
        //没有缓存 从数据库查询  并 缓存到redis
        if (StringUtils.isEmpty(vo)) {
            QueryWrapper<SysComment> wq = new QueryWrapper<>();
            wq.eq("articleId", articleId);
            wq.eq("isStick", 0);
            wq.isNull("pid");
            wq.isNull("rootId");
            wq.orderByAsc("createTime");
            IPage<SysComment> list = this.page(page, wq);
            if (StringUtils.isEmpty(list.getRecords())) {
                return null;
            }
            listSize = list.getTotal();
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_ROOT_SIZE + articleId, Integer.valueOf(listSize.toString()), 60 * 60 * 24, TimeUnit.SECONDS);
            vo = ConvertUtils.sourceToTarget(list.getRecords(), CommentHomeTreeVO.class);

            boolean exists = redisCacheUtils.exists(RedisConstants.REDIS_ARTICLE_COMMENT + articleId);

            AtomicInteger atomicInteger = new AtomicInteger(0);
            if (exists) {
                atomicInteger.set(current);
            }
            vo.forEach(i -> {
                Long rootid = i.getId();
                redisCacheUtils.setCacheObject(RedisConstants.REDIS_ARTICLE_COMMENT_INDEX + rootid, atomicInteger.getAndIncrement(), 60 * 60 * 24, TimeUnit.SECONDS);
            });

            //查询子节点
            vo = this.queryByRootId(vo);
            //填充用户信息
            this.setUserName(vo);

            //缓存数据
            redisCacheUtils.setCacheRightPushList(RedisConstants.REDIS_ARTICLE_COMMENT + articleId, vo);
        }
        if (StringUtils.isNotEmpty(vo)) {
            List<CommentHomeTreeVO> newList = new ArrayList<>();
            vo.forEach(i -> {
                if (StringUtils.isNotEmpty(i.getChildren())) {
                    newList.addAll(i.getChildren());
                } else {
                    newList.add(i);
                }
            });
            AtomicInteger atomicInteger = new AtomicInteger(0);
            newList.forEach(i -> {
                i.setWeight(atomicInteger.getAndIncrement());
            });

            //树结构构件
            TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
            // 自定义属性名
            treeNodeConfig.setParentIdKey("pid");
            //转换器 (含义:找出父节点为字符串零的所有子节点, 并递归查找对应的子节点, 深度最多为 3)
             treeNodes = TreeUtil.build(newList, Long.valueOf(0), treeNodeConfig, (treeNode, tree) -> {
                tree.setId(treeNode.getId());
                tree.setParentId(treeNode.getpid() != null ? treeNode.getpid() : 0);
                tree.setWeight(treeNode.getWeight());
                // 扩展属性 ...
                tree.putExtra("content", treeNode.getContent());
                tree.putExtra("upNum", treeNode.getUpNum());
                tree.putExtra("createName", treeNode.getCreateName());
                tree.putExtra("userAvatar", treeNode.getUserAvatar());
                tree.putExtra("isLiked", treeNode.getIsLiked());
                tree.putExtra("createTime", DateUtil.getDateTimeLo(treeNode.getCreateTime()));
                tree.putExtra("showReply", false);
            });
        }

        //数据
        pageTree.setRecords(treeNodes);
        //当前页
        pageTree.setCurrent(pageNo);
        //总数量
        pageTree.setTotal(rootSize);
        //查询条数
        pageTree.setSize(pageSize);
        // 总页数
        long pages = Math.floorDiv(rootSize, pageSize);
        pageTree.setPages(pages);
        return pageTree;
    }

    @Override
    public Integer queryByArticleIdCount(Long articleId) {
        QueryWrapper<SysComment> wq = new QueryWrapper<>();
        wq.eq("articleId", articleId);
        Long count = this.count(wq);
        return StringUtils.isNotNull(count) ? Integer.valueOf(count.toString()) : 0;
    }

    /**
     * 查询子节点
     *
     * @param volist
     * @return
     */
    public List<CommentHomeTreeVO> queryByRootId(List<CommentHomeTreeVO> volist) {
        // 判断传入的volist是否为空
        if (StringUtils.isEmpty(volist)) {
            return null;
        }
        List<CommentHomeTreeVO> clone = new ArrayList<>();
        clone.addAll(volist);
        Map<Long, CommentHomeTreeVO> map = new HashMap<>(16);
        clone.forEach(i -> {
            CommentHomeTreeVO vo = new CommentHomeTreeVO();
            BeanUtils.copyProperties(i, vo);
            vo.setChildren(null);
            map.put(vo.getId(), vo);
        });
        // 遍历volist
        clone.forEach(i -> {
            // 创建QueryWrapper对象
            QueryWrapper<SysComment> wq = new QueryWrapper<>();
            // 设置查询条件
            wq.eq("rootId", i.getId());
            // 查询SysComment表
            List<SysComment> list = this.list(wq);
            // 判断查询结果是否为空
            if (StringUtils.isNotEmpty(list)) {
                // 将查询结果转换为CommentHomeTreeVO对象
                List<CommentHomeTreeVO> commentHomeTreeVOS = ConvertUtils.sourceToTarget(list, CommentHomeTreeVO.class);
                // 将转换后的对象添加到clone中
                List<CommentHomeTreeVO> children = new ArrayList<>();
                children.addAll(commentHomeTreeVOS);
                CommentHomeTreeVO commentHomeTreeVO = map.get(i.getId());
//                commentHomeTreeVO.setChildren(Arrays.asList());
                children.add(commentHomeTreeVO);
                children.sort(Comparator.comparing(CommentHomeTreeVO::getCreateTime));
                i.setChildren(children);
            }
        });
//        对clone进行排序
        clone.sort(Comparator.comparing(CommentHomeTreeVO::getCreateTime));
        // 返回clone
        return clone;
    }

    /**
     * 查询评论用户信息
     *
     * @param volist
     */
    public void setUserName(List<CommentHomeTreeVO> volist) {
        if (StringUtils.isEmpty(volist)) {
            return;
        }
        volist.forEach(this::setUserName);
    }

    /**
     * 查询评论用户信息
     *
     * @param vo
     */
    public void setUserName(CommentHomeTreeVO vo) {
        if (StringUtils.isNull(vo)) {
            return;
        }
        if (StringUtils.isNotNull(vo.getCreateBy())) {
            SysUser userById = sysUserService.getUserById(vo.getCreateBy());
            if (StringUtils.isNotNull(userById)) {
                vo.setCreateName(userById.getUserName());
                vo.setUserAvatar(userById.getUserAvatar());
                vo.setIsLiked(false);
            }
        }
        if (StringUtils.isNotEmpty(vo.getChildren())) {
            this.setUserName(vo.getChildren());
        }
    }
}




