package com.bank.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@ComponentScan(basePackages = {"com.bank.controller"})
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer, ApplicationContextAware{

private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
	 @Bean
	    public SpringResourceTemplateResolver templateResolver(){
	      
	        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	        templateResolver.setApplicationContext(this.applicationContext);
	        templateResolver.setPrefix("/");
	        templateResolver.setSuffix(".html");
	        // HTML es la plantilla por defecto, se indica por claridad.
	        templateResolver.setTemplateMode(TemplateMode.HTML);
	        // Template cache is true by default. Set to false if you want
	        // templates to be automatically updated when modified.
	        templateResolver.setCacheable(true);
	        return templateResolver;
	    }

	    @Bean
	    public SpringTemplateEngine templateEngine(){
	        // SpringTemplateEngine automatically applies SpringStandardDialect and
	        // enables Spring's own MessageSource message resolution mechanisms.
	        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        templateEngine.setTemplateResolver(templateResolver());
	        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
	        // speed up execution in most scenarios, but might be incompatible
	        // with specific cases when expressions in one template are reused
	        // across different data types, so this flag is "false" by default
	        // for safer backwards compatibility.
	        templateEngine.setEnableSpringELCompiler(true);
	        return templateEngine;
	    }

	    @Bean
	    public ThymeleafViewResolver viewResolver(){
	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	        viewResolver.setTemplateEngine(templateEngine());
	        return viewResolver;
	    }
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/").setViewName("acces");
	    registry.addViewController("/toIncome").setViewName("income");
	    registry.addViewController("/toWithdrawal").setViewName("withdrawal");
	    registry.addViewController("/toMenu").setViewName("menu");
	    registry.addViewController("/toMovements").setViewName("movements");
	    registry.addViewController("/toTransfer").setViewName("transfers");
		registry.addViewController("/toUpdateClient").setViewName("updateclient");

	    //Admin
	    registry.addViewController("/toSaveClient").setViewName("newClient");
	    registry.addViewController("/toSaveAccount").setViewName("newaccount");
	    registry.addViewController("/toMovClienteRango").setViewName("movclienterango");
	    registry.addViewController("/toClientaccounts").setViewName("clientaccounts");
	    registry.addViewController("/toSinMovimientoDesde").setViewName("sinmovimientodesde");
	    registry.addViewController("/toMovCuentaCliente").setViewName("movcuentacliente");
	    registry.addViewController("/toBlockAccount").setViewName("blockaccount");
		registry.addViewController("/toUnlockAccount").setViewName("unlockaccount");
		registry.addViewController("/toCancelTransfer").setViewName("canceltransfer");

}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*").addResourceLocations("/");
    }
	
}
