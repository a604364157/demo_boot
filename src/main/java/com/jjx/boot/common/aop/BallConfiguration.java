package com.jjx.boot.common.aop;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Configuration
public class BallConfiguration extends WebMvcConfigurationSupport {
//    以下WebMvcConfigurerAdapter 比较常用的重写接口
//    /** 解决跨域问题 **/
//    public void addCorsMappings(CorsRegistry registry) ;
//    /** 添加拦截器 **/
//    void addInterceptors(InterceptorRegistry registry);
//    /** 这里配置视图解析器 **/
//    void configureViewResolvers(ViewResolverRegistry registry);
//    /** 配置内容裁决的一些选项 **/
//    void configureContentNegotiation(ContentNegotiationConfigurer configurer);
//    /** 视图跳转控制器 **/
//    void addViewControllers(ViewControllerRegistry registry);
//    /** 静态资源处理 **/
//    void addResourceHandlers(ResourceHandlerRegistry registry);
//    /** 默认静态资源处理器 **/
//    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);

    @Resource
    private com.jjx.boot.common.aop.LoginInterceptor loginInterceptor;

    /**
     * 配置静态资源
     * 静态资源走静态资源处理模式，典型就是程序不处理
     *
     * @param registry reg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }

    /**
     * 配置拦截规则
     *
     * @param registry reg
     */
/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                //addPathPatterns 用于添加拦截规则
                .addPathPatterns("/**")
                //excludePathPatterns 用于排除拦截
                // 项目启动测试接口
                .excludePathPatterns("/")
                //登录相关
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/templates/**");
        super.addInterceptors(registry);
    }*/

    /**
     * 这个用于替换默认的消息格式化工具
     * 因为框架默认协议是restful，交互为json
     * 框架默认是使用jackson
     * 这里替换为fastjson 注意 版本时 1.2.49
     *
     * 目前最新得版本时2016年发布的1.2.9
     * 但是1.2.9不知道做了什么精简，
     * 源码上一些包和类都删掉了，
     * 一些在之前版本过期得函数又给放了出来
     * 最重要的是，使用新版之后会出现入参格式化错误
     * 也不知道是哪配的不对还是怎么回事。
     * 阿里就是这点坑爹，开源后特么官方文档也不清不楚
     * 版本差异也不清不楚，搞得特么很头疼
     *
     * @param converters con
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig jsonConfig = new FastJsonConfig();
        jsonConfig.setSerializerFeatures(
                //结果格式化（增强可读性）
//                SerializerFeature.PrettyFormat,
                //当对象中节点为null时，输出该节点（否则会丢掉该节点）
                SerializerFeature.WriteMapNullValue,
                //集合对象为null时输出[]
                SerializerFeature.WriteNullListAsEmpty,
                //字符为null时输出""
                SerializerFeature.WriteNullStringAsEmpty,
                //启用date格式化(默认yyyy-MM-dd HH:mm:ss)
                SerializerFeature.WriteDateUseDateFormat
        );
        //设置字符编码
        jsonConfig.setCharset(Charset.forName("UTF-8"));
        //设置全局时间格式化使用格式(一旦配置了全局格式，那么单独格式配置就会失效，很坑爹，建议不配置)
        // jsonConfig.setDateFormat(DateConst.DATE_MODEL_2)
        fastConverter.setFastJsonConfig(jsonConfig);
        //设计HTTP响应格式和字符
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(fastConverter);
    }
}
