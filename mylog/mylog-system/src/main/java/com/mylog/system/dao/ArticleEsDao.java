package com.mylog.system.dao;

import com.mylog.system.entity.article.dto.ArticleEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author pss
 * @date 2025/3/12 22:03
 */
public interface ArticleEsDao extends ElasticsearchRepository<ArticleEsDTO, Long> {
}
