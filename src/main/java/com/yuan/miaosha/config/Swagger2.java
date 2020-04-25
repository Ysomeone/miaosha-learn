package com.yuan.miaosha.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuan
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", value = {"enable"}, havingValue = "true")
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        /**
         * .host("域名") 如果通过域名访问文档，需要加上这个，以免在文档请求因为跨域问题而请求不了
         */
        return new Docket(DocumentationType.SWAGGER_2)//.host("域名")
                .apiInfo(apiInfo()).select()
                //扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage("com.yuan.miaosha"))
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build().securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("基础平台  API文档")
                .version("1.0.0")
                .build();
    }

    /**
     * 权限控制，在请求参数加上sessionId
     *
     * @return
     */
    private List<ApiKey> security() {
        List<ApiKey> apiKeyList = new ArrayList();
        ApiKey apiKey = new ApiKey("sessionId", "sessionId", "query");
        apiKeyList.add(apiKey);
        return apiKeyList;
    }

}