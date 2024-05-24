import {UserType} from "./user";
import {BlogType} from "./blog";
import {CommentType} from "./comment";

export type MessageType = {
    id: string,
    type: number,
    fromId: string,
    toId: string,
    data: string,
    isRead: number,
    createTime: string,
    updateTime: string,
    isDelete: number,
    fromUser: UserType,
    blog?: BlogType,
    comment?: CommentType
}
