package com.test.everyday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FolerPathConfig implements WebMvcConfigurer {

//	 @Value("${file.upload-dir}")
//	    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
    {
//        registry.addResourceHandler("/employee/**")
//                .addResourceLocations("file:employee/");
        
        registry.addResourceHandler("/employees/**")
        .addResourceLocations("file:employees/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
//        registry.addResourceHandler("/uploadFiles/**")
//        .addResourceLocations("file:"+uploadDir+"/");


       
    }
}