package io.flippedclassroom.server.config.swagger;

import io.flippedclassroom.server.util.Const.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.flippedclassroom.server"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后端 API")
                .description("基于 Swagger2，可直接在此页面进行测试。\n\n" +
                        "需要注意以下两点：1.数据全部使用json格式，并且uri符合rest标准，全部以名词组成，post,delete,put,get代表增删改查，另外对多个数据操作时，uri中的名词为复数形式" +
                        "\n\n2.以auth开头的uri表示需要携带token" +
                        "\n\n由于 Swagger 本身的原因，不能设置 Example Value，测试的时候请手动输入 JSON 数据，测试数据会在 API 旁标明。" +
                        "\n\n由于未知原因，引号的显示可能不正常，测试的时候请注意。。。")
                .version(Const.swaggerVersion)
                .build();
    }
}
