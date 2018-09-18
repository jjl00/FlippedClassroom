package io.flippedclassroom.server.config.webmvc;


import io.flippedclassroom.server.annotation.resolver.CurrentUserMethodArgumentResolver;
import io.flippedclassroom.server.util.Const.Const;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    // 注入自定义注解
    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    // 文件上传相关
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(Const.size1M * 100);
        factory.setMaxRequestSize(Const.size1M * 100);
        return factory.createMultipartConfig();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
    }

//	@Bean
//	public HttpMessageConverters fastJsonHttpMessageConverters() {
//		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//		fastConverter.setFastJsonConfig(fastJsonConfig);
//		HttpMessageConverter<?> converter = fastConverter;
//		return new HttpMessageConverters(converter);
//	}

}
