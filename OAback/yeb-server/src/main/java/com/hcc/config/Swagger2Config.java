package com.hcc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger接口文档配置
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)

                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hcc.controller"))
                .paths(PathSelectors.any())
                .build()
                //Swgger 授权登录
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CC办公")
                .description("云办公")
                .contact(new Contact("HKMCC","http:localhost:8081/doc.html","903613657@qq.com"))
                .version("1.0")
                .build();
    }
    private List<ApiKey> securitySchemes(){
        ArrayList<ApiKey> result = new ArrayList<>();
        //设置请求头信息
        ApiKey apiKey = new ApiKey("Authorization","Authorization","Header");
        result.add(apiKey);

        return result;

    }
    private List<SecurityContext> securityContexts(){

        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;

    }

    private SecurityContext getContextByPath(String PathRegex) {
        return SecurityContext.builder()
                .securityReferences(dafaultAuth())
                .forPaths(PathSelectors.regex((PathRegex)))
                .build();

    }

    private List<SecurityReference> dafaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "AccessEverthing");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0]=authorizationScope;
        result.add(new SecurityReference("Authorization",authorizationScopes));

        return result;
    }


}

