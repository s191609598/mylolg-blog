declare namespace API {
  type ArticleCarouselVO = {
    articleType?: number
    cover?: string
    id?: number
    isRecommend?: number
    isTop?: number
    sort?: number
    title?: string
  }

  type ArticleVO = {
    articleType?: number
    categoryId?: number
    categoryName?: string
    collectNum?: number
    commentNum?: number
    content?: string
    cover?: string
    createBy?: number
    createByName?: string
    createTime?: string
    editTime?: string
    excerpt?: string
    id?: number
    isCarousel?: number
    isDelete?: number
    isRecommend?: number
    isTop?: number
    readNum?: number
    reprintUrl?: string
    sort?: number
    state?: number
    tags?: string[]
    title?: string
    upNum?: number
    updateTime?: string
  }

  type CaptchaVO = {
    captchaImg?: string
    captchaKey?: string
  }

  type CategoryPageListDTO = {
    id?: number
    name?: string
    pageNo?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type CategoryPageListVO = {
    createTime?: string
    id?: number
    name?: string
    sort?: number
    updateTime?: string
  }

  type CategoryVO = {
    id?: number
    name?: string
  }

  type CharSequence = true

  type collectArticleUsingGETParams = {
    /** articleId */
    articleId?: number
  }

  type CommentHomeDTO = {
    articleId?: number
    content?: string
    pid?: number
  }

  type ComparableObject_ = true

  type EditArticleDTO = {
    articleType?: number
    categoryId?: number
    content?: string
    cover?: string
    coverId?: string
    excerpt?: string
    id?: number
    isCarousel?: number
    isRecommend?: number
    isTop?: number
    reprintUrl?: string
    sort?: number
    state?: number
    tags?: string[]
    title?: string
  }

  type EditCategoryDTO = {
    id?: number
    name?: string
    sort?: number
  }

  type EditTagDTO = {
    id?: number
    name?: string
    sort?: number
  }

  type EditUserDTO = {
    email?: string
    id?: number
    mpOpenId?: string
    phonenumber?: string
    remark?: string
    sex?: number
    unionId?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: string
  }

  type FileVO = {
    fileName?: string
    fileUrl?: string
    id?: number
  }

  type getArticleByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type getFileUsingGETParams = {
    /** id */
    id?: number
  }

  type getHomeArticleByIdUsingGETParams = {
    /** id */
    id?: number
  }

  type HomeArticleVO = {
    categoryId?: number
    categoryName?: string
    collectNum?: number
    commentNum?: number
    content?: string
    cover?: string
    createByName?: string
    createTime?: string
    editTime?: string
    excerpt?: string
    id?: number
    isCollect?: boolean
    isRecommend?: number
    isTop?: number
    isUp?: boolean
    readNum?: number
    reprintUrl?: string
    state?: number
    tags?: string[]
    title?: string
    upNum?: number
    updateTime?: string
  }

  type HomeTagVO = {
    id?: number
    name?: string
    num?: number
    sort?: number
  }

  type IdDTO = {
    id?: number
  }

  type IPageCategoryPageListVO_ = {
    current?: number
    pages?: number
    records?: CategoryPageListVO[]
    size?: number
    total?: number
  }

  type IPageQueryMyCollectVO_ = {
    current?: number
    pages?: number
    records?: QueryMyCollectVO[]
    size?: number
    total?: number
  }

  type IPageSearchArticleByKeywordVO_ = {
    current?: number
    pages?: number
    records?: SearchArticleByKeywordVO[]
    size?: number
    total?: number
  }

  type IPageTagPageListVO_ = {
    current?: number
    pages?: number
    records?: TagPageListVO[]
    size?: number
    total?: number
  }

  type IPageTreeLong_ = {
    current?: number
    pages?: number
    records?: TreeLong_[]
    size?: number
    total?: number
  }

  type noArticleUsingGET1Params = {
    /** articleId */
    articleId: number
  }

  type noArticleUsingGETParams = {
    /** articleId */
    articleId?: number
  }

  type PageQueryArticleVO_ = {
    current?: number
    pages?: number
    records?: QueryArticleVO[]
    size?: number
    total?: number
  }

  type PageQueryUserVO_ = {
    current?: number
    pages?: number
    records?: QueryUserVO[]
    size?: number
    total?: number
  }

  type QueryArticleDTO = {
    articleType?: number
    categoryId?: number
    content?: string
    excerpt?: string
    id?: number
    isCarousel?: number
    isRecommend?: number
    isTop?: number
    maxCommentNum?: number
    maxReadNum?: number
    maxUpNum?: number
    minCommentNum?: number
    minReadNum?: number
    minUpNum?: number
    pageNo?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    state?: number
    title?: string
  }

  type QueryArticleVO = {
    articleType?: number
    categoryId?: number
    categoryName?: string
    collectNum?: number
    commentNum?: number
    content?: string
    cover?: string
    createBy?: number
    createByName?: string
    createTime?: string
    editTime?: string
    excerpt?: string
    id?: number
    isCarousel?: number
    isRecommend?: number
    isTop?: number
    readNum?: number
    reprintUrl?: string
    sort?: number
    state?: number
    tags?: string[]
    title?: string
    upNum?: number
    updateTime?: string
  }

  type queryCommentDTO = {
    articleId?: number
    pageNo?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type QueryMyCollectDTO = {
    pageNo?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    userId?: number
  }

  type QueryMyCollectVO = {
    excerpt?: string
    id?: number
    title?: string
  }

  type QueryUserVO = {
    createBy?: number
    createTime?: string
    editTime?: string
    email?: string
    id?: number
    isDelete?: number
    lastLoginDate?: string
    lastLoginIp?: string
    loginDate?: string
    loginIp?: string
    mpOpenId?: string
    phonenumber?: string
    remark?: string
    sex?: number
    unionId?: string
    updateBy?: number
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type RecommendArticleVO = {
    cover?: string
    createTime?: string
    editTime?: string
    id?: number
    isRecommend?: number
    sort?: number
    title?: string
    updateTime?: string
  }

  type SaResult = true

  type SearchArticleByKeywordDTO = {
    categoryId?: number
    keyword?: string
    pageNo?: number
    pageSize?: number
    showTop?: boolean
    sortField?: string
    sortOrder?: string
    tagId?: number
  }

  type SearchArticleByKeywordVO = {
    articleType?: number
    categoryId?: number
    categoryName?: string
    collectNum?: number
    commentNum?: number
    content?: string
    cover?: string
    createTime?: string
    editTime?: string
    excerpt?: string
    id?: number
    isCarousel?: number
    isDelete?: number
    isRecommend?: number
    isTop?: number
    readNum?: number
    reprintUrl?: string
    sort?: number
    state?: number
    tags?: string[]
    title?: string
    upNum?: number
    updateTime?: string
  }

  type TagPageListDTO = {
    id?: number
    name?: string
    pageNo?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type TagPageListVO = {
    createTime?: string
    id?: number
    name?: string
    sort?: number
    updateTime?: string
  }

  type TreeLong_ = true

  type TreeNodeConfig = {
    childrenKey?: string
    deep?: number
    idKey?: string
    nameKey?: string
    parentIdKey?: string
    weightKey?: string
  }

  type upArticleUsingGETParams = {
    /** articleId */
    articleId: number
  }

  type UpdateArticleStatusDTO = {
    id?: number
    status?: number
  }

  type UpdateUserDTO = {
    email?: string
    id?: number
    phonenumber?: string
    remark?: string
    sex?: number
    userAvatar?: string
    userName?: string
    userProfile?: string
  }

  type UserLoginDTO = {
    userAccount?: string
    userPassword?: string
  }

  type UserQueryDTO = {
    email?: string
    id?: number
    isDelete?: number
    pageNo?: number
    pageSize?: number
    phonenumber?: string
    remark?: string
    sex?: number
    sortField?: string
    sortOrder?: string
    userAccount?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterDTO = {
    captchaCode?: string
    captchaKey?: string
    checkPassword?: string
    userAccount?: string
    userPassword?: string
  }

  type UserVO = {
    email?: string
    id?: number
    lastLoginDate?: string
    phonenumber?: string
    remark?: string
    sex?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }
}
