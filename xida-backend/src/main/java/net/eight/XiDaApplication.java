package net.eight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 应用程序
 *
 * @author
 * @date 2024/03/25
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableAspectJAutoProxy
public class XiDaApplication {
    protected XiDaApplication() {
    }

    /**
     * 程序入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(XiDaApplication.class, args);
    }
}

