package com.primovision.lutransport.web;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

/**
 * Class to share spring context through static references
 * 
 * @author Rakesh
 * 
 */
public class SpringAppContext {

    /**
     * Spring context
     */
    private static ApplicationContext ctx;
    private static ServletContext servletContext;

    /**
     * Sets application context to the class
     * 
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
	ctx = applicationContext;
    }

    /**
     * Gets bean from context
     * 
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
	return ctx.getBean(beanName);
    }

    public static ServletContext getServletContext() {
	return servletContext;
    }

    public static void setServletContext(ServletContext servletContext) {
	SpringAppContext.servletContext = servletContext;
    }
}
