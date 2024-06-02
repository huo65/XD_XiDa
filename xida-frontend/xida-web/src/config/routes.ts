import Index from "../pages/Index.vue";
import Team from "../pages/team/TeamPage.vue";
import User from "../pages/user/UserPage.vue";
import Search from "../pages/SearchPage.vue";
import EditUser from "../pages/user/UserEditPage.vue";
import SearchResult from "../pages/SearchResultPage.vue";
import UserLogin from "../pages/UserLoginPage.vue";
import TeamAddPage from "../pages/team/TeamAddPage.vue";
import TeamUpdatePage from "../pages/team/TeamUpdatePage.vue";
import UserUpdatePage from "../pages/user/UserUpdatePage.vue";
import UserTeamJoinPage from "../pages/user/UserTeamJoinPage.vue";
import UserTeamCreatePage from "../pages/user/UserTeamCreatePage.vue";
import SignUpPage from "../pages/SignUpPage.vue";
import BlogEditPage from "../pages/blog/BlogEditPage.vue";
import MessagePage from "../pages/message/MassagePage.vue";
import UserBlogPage from "../pages/user/UserBlogPage.vue";
import TeamDetailPage from "../pages/team/TeamDetailPage.vue";
import BlogPage from "../pages/blog/BlogPage.vue";
import UserTagPage from "../pages/user/UserTagPage.vue";
import ForgetPasswordPage from "../pages/ForgetPasswordPage.vue";
import AfterSignUp from "../pages/AfterSignUp.vue";
import ChatPage from "../pages/ChatPage.vue";
import UserDetailPage from "../pages/user/UserDetailPage.vue";
import UserFollowPage from "../pages/message/UserFollowPage.vue";
import UserBlogCommentPage from "../pages/message/UserBlogCommentPage.vue";
import UserFansPage from "../pages/message/UserFansPage.vue";
import UserLikePage from "../pages/message/UserLikePage.vue";
import UserFollowedUserPage from "../pages/message/UserFollowedUserPage.vue";
import UpdatePasswordPage from "../pages/UpdatePasswordPage.vue";
import PrivateChatPage from "../pages/message/PrivateChatPage.vue";

const routes = [
    { path: "/", title: "嘻搭XIDA", component: Index },
    { path: "/search", title: "搜索", component: Search },
    { path: "/search/userList", title: "搜索到的用户", component: SearchResult },
    { path: "/team", title: "队伍", component: Team },
    { path: "/team/detail", title: "队伍详情", component: TeamDetailPage },
    { path: "/team/add", title: "创建队伍", component: TeamAddPage },
    { path: "/team/update", title: "更新队伍", component: TeamUpdatePage },
    { path: "/user", title: "个人", component: User },
    { path: "/user/tag", title: "标签", component: UserTagPage },
    { path: "/user/detail", title: "用户详情", component: UserDetailPage },
    { path: "/user/signup", component: SignUpPage },
    { path: "/after", title: "标签", component: AfterSignUp, meta: { layout: "after" } },
    { path: "/user/login", title: "用户登录", component: UserLogin },
    { path: "/user/edit", title: "修改用户", component: EditUser },
    { path: "/user/update", title: "修改用户", component: UserUpdatePage },
    { path: "/user/team/join", title: "我加入的队伍", component: UserTeamJoinPage },
    { path: "/user/team/create", title: "我创建的队伍", component: UserTeamCreatePage },
    { path: "/blog", component: BlogPage, meta: { layout: "blog" } },
    { path: "/blog/edit", title: "博文编辑", component: BlogEditPage, meta: { layout: "blog-edit" } },
    { path: "/user/blog", title: "我写的博文", component: UserBlogPage },
    { path: "/message", title: "消息中心", component: MessagePage },
    { path: "/forget", title: "找回密码", component: ForgetPasswordPage, meta: { layout: "forget" } },
    { path: "/chat", component: ChatPage, meta: { layout: "chat" } },
    { path: "/user/follow", title: "我关注的用户", component: UserFollowPage },
    { path: "/user/comment", component: UserBlogCommentPage, meta: { layout: "comment" } },
    { path: "/fans", title: "我的粉丝", component: UserFansPage },
    { path: "/user/like", title: "赞", component: UserLikePage },
    { path: "/user/follow/blog", title: "关注", component: UserFollowedUserPage },
    { path: "/update/password", title: "修改密码", component: UpdatePasswordPage, meta: { layout: "password" } },
    { path: "/private", title: "私聊", component: PrivateChatPage },
];
export default routes;
