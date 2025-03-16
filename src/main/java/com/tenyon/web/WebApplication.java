package com.tenyon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

// 如需开启 Redis，须移除 exclude = {RedisAutoConfiguration.class}
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
//		SpringApplication application = new SpringApplication(WebApplication.class);
//		application.setApplicationStartup(new BufferingApplicationStartup(2048));
//		ConfigurableApplicationContext app = application.run(args);
        log.info("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ");
//		Environment env = app.getEnvironment();
//		// ip
//		String ip = InetAddress.getLocalHost().getHostAddress();
//		// port
//		String port = env.getProperty("server.port");
//		// path
//		String path = env.getProperty("server.servlet.context-path");
//		if (StringUtils.isBlank(path)) {
//			path = "";
//		}
//		logger.info("\n----------------------------------------------------------\n\t" +
//				"Application  is running! Access URLs:\n\t" +
//				"Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
//				"External访问网址: \thttp://" + ip + ":" + port + path + "\n\t" +
//				"接口文档地址: \t\thttp://localhost:" + port + path + "doc.html\n\t" +
//				"----------------------------------------------------------");
    }

}
