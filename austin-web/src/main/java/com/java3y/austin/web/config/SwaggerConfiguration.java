package com.java3y.austin.web.config;

import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * swagger配置类
 * @author 3y
 */
@Component
@EnableOpenApi
@ApiModel
public class SwaggerConfiguration {
    /**
     * 对C端用户的接口文档
     *
     * @return
     */
    @Bean
    public Docket webApiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.java3y.austin.web.controller"))
                //正则匹配请求路径，并分配到当前项目组
                //.paths(PathSelectors.ant("/api/**"))
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("austin平台")
                .description("消息推送接口接口文档")
                .contact(new Contact("3y", "http://gitee.com/zhongfucheng/austin", "403686131@qq.com"))
                .version("v1.0")
                .build();
    }

}