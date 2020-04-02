package com.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
      //  registry.addViewController("/login").setViewName("/login");
    	registry.addViewController( "/" ).setViewName( "redirect:/welcome" );
    	//registry.addViewController("/welcome").setViewName("/welcome");
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/registration").setViewName("/registration");
        registry.addViewController("/userWelcome").setViewName("/userWelcome");
        registry.addViewController("/teacherWelcome").setViewName("/teacherWelcome");
        registry.addViewController("/uploadCV").setViewName("/students/uploadCV");
        registry.addViewController("/students/teachers").setViewName("/teachers");
        registry.addViewController("/students/viewTeacherDetails").setViewName("/teacherDetails");
        registry.addViewController("/studentRequest").setViewName("/studentRequest");
        registry.addViewController("/submitRequest").setViewName("/submitRequest");
        registry.addViewController("/viewRequests").setViewName("/viewRequests");
        registry.addViewController("/teachers/completeDetails").setViewName("/completeDetails");
        registry.addViewController("/viewReceivedRequests").setViewName("/teachers/viewReceivedRequests");
        registry.addViewController("/submitProject").setViewName("/completeDetailsTeacher");


        
        
        
    }	

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Add additional interceptors here
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/img/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/");
    

}

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //Add additional formatters here
    }
}