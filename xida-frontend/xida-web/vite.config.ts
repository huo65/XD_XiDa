import vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import { VantResolver } from "unplugin-vue-components/resolvers";

// 添加代理配置到vite的服务器设置中
const proxyConfig = {
    "/api": {
        target: "http://你的目标服务器地址", // 目标API服务器的基础URL
        changeOrigin: true, // 是否改变请求源头
        rewrite: (path) => path.replace(/^\/api/, ""), // 路径重写规则，如果API路径中包含/api，这里可以去掉它
    },
};

export default {
    plugins: [
        vue(),
        Components({
            resolvers: [VantResolver()],
        }),
    ],
    server: {
        // 开发服务器配置
        host: "0.0.0.0", // 可选，允许外部访问开发服务器
        port: 3000, // 可选，指定开发服务器端口
        proxy: proxyConfig, // 添加代理配置
    },
};