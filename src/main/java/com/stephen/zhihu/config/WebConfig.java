package com.stephen.zhihu.config;

import com.stephen.zhihu.authorization.AuthorizationInterceptor;
import com.stephen.zhihu.authorization.CurrentUserAnnotationResolver;
import com.stephen.zhihu.authorization.TokenManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.stephen.zhihu.web", "com.stephen.zhihu.exception"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ControllerAdvice.class))
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor((TokenManager) getApplicationContext().getBean("tokenManager")));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserAnnotationResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
