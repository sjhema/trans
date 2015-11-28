package com.primovision.lutransport.web;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import com.primovision.lutransport.core.cache.CacheManager;
import com.primovision.lutransport.core.configuration.Configuration;

/**
 * @author Rakesh
 *         <p>
 *         Web Context Loader Listener Class. Startup Application and Jobs setup
 *         is performed.
 *         </p>
 */
public class ExtendedContextLoaderListener extends ContextLoaderListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.context.ContextLoaderListener#contextDestroyed
	 * (javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}

	/**
	 * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		SpringAppContext
				.setApplicationContext(getCurrentWebApplicationContext());
		CacheManager globalCacheManager = (CacheManager) getCurrentWebApplicationContext()
				.getBean("globalCacheManager");
		try {
			Configuration.load(ExtendedContextLoaderListener.class
					.getResourceAsStream("/System.properties"));
			globalCacheManager.refreshAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
