package com.hailian.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hailian.interceptors.AppInterceptors;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.hailian.swagger2.SwaggerConfig
 * @Copyright: Copyright (c) 2016
 * Author lv bin
 * @date 2017/5/19 10:45
 * version V1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    //http://localhost:8080/swagger-ui.html
	//https://blog.csdn.net/xupeng874395012/article/details/68946676

   /**
     * 使用enableMVC注解的话,该配置必须,否则无法映射资源
     * @Api：用在类上，说明该类的作用
		@ApiOperation：用在方法上，说明方法的作用
		@ApiImplicitParams：用在方法上包含一组参数说明
		@ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
		paramType：参数放在哪个地方
		header-->请求参数的获取：@RequestHeader
		query-->请求参数的获取：@RequestParam
		path（用于restful接口）-->请求参数的获取：@PathVariable
		body（不常用）
		form（不常用）
		name：参数名
		dataType：参数类型
		required：参数是否必须传
		value：参数的意思
		defaultValue：参数的默认值
		@ApiResponses：用于表示一组响应
		@ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
		code：数字，例如400
		message：信息，例如"请求参数没填好"
		response：抛出异常的类
		@ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
		@ApiModelProperty：描述一个model的属性
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    @Bean
    public Docket createRestApi() {

        ParameterBuilder tokenParams = new ParameterBuilder();
        ParameterBuilder platformParams = new ParameterBuilder();
        platformParams.name(AppInterceptors.DEFAULT_PLATFORM).description("平台(PC/ANDROID/IOS)").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        tokenParams.name(AppInterceptors.DEFAULT_TOKEN_NAME).description("登陆令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        ParameterBuilder signParams = new ParameterBuilder();
        signParams.name(AppInterceptors.DEFAULT_AUTH_NAME).description("加密签名").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        
        //添加公用参数
       /* ParameterBuilder versionParams = new ParameterBuilder();
        versionParams.name("version").description("版本，例如v1").modelRef(new ModelRef("string")).parameterType("path").required(true).build();
        */
        List<Parameter> headerParams = new ArrayList<Parameter>();
        headerParams.add(tokenParams.build());
        headerParams.add(signParams.build());
        headerParams.add(platformParams.build());
        //headerParams.add(versionParams.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("api")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hailian.controller"))
                .build()
                .globalOperationParameters(headerParams);
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("左青贝","http://hlsofttech.com","qingbei.zuo@hlsofttech.com");
        return new ApiInfoBuilder()
                .title("青岛海联软件科技有限公司")
                .description("API统一接口平台")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
