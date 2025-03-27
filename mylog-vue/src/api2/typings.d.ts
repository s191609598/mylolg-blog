declare namespace API {
  type CommentsTreeVO = {
    id: number
    pid: number
    upNum: number
    userAvatar?: string
    createName?: string
    content?: string
    replyContent?: string
    createTime?: string
    isLiked: boolean
    showReply: boolean
    children?: []
  }
}
