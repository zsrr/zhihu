package com.stephen.zhihu.config;

import com.stephen.zhihu.authorization.AuthorizationInterceptor;
import com.stephen.zhihu.authorization.CurrentUserAnnotationResolver;
import com.stephen.zhihu.authorization.TokenManager;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.stephen.zhihu.web", "com.stephen.zhihu.exception"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ControllerAdvice.class))
@EnableAsync
public class WebConfig extends WebMvcConfigurationSupport {

    /*@Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(60);
        executor.setQueueCapacity(100);
        configurer.setTaskExecutor(executor);
    }*/

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
