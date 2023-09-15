package com.github.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {

        //swagger에서 header 입력할 수 있도록 Authorization 헤더의 내용 입력
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameterBuilder1 = new ParameterBuilder().name("ACCESS_TOKEN").description("Jwt Access Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        Parameter parameterBuilder2 = new ParameterBuilder().name("REFRESH_TOKEN").description("Jwt Refresh Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        parameters.add(parameterBuilder1);
        parameters.add(parameterBuilder2);

        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey(),apiRefreshKey()));
//                .globalOperationParameters(parameters);
    }

//    @Bean
//    public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "octet-stream")));
//        return converter;
//    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("ACCESS_TOKEN", authorizationScopes),new SecurityReference("REFRESH_TOKEN", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("ACCESS_TOKEN", "ACCESS_TOKEN", "header");
    }
    private ApiKey apiRefreshKey() {
        return new ApiKey("REFRESH_TOKEN", "REFRESH_TOKEN", "header");
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GuerrillaCommerce Swagger")
                .description("GuerrillaCommerce API 리스트입니다.")
                .version("0.1")
                .build();
    }

}
