/**
 * 
 */
package com.primovision.lutransport.core.cache;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

/**
 * Implementation for cache Manager
 * 
 * @author Rakesh
 */
@Repository("globalCacheManager")
@SuppressWarnings("unchecked")
public class GlobalCacheManager implements CacheManager,
		ApplicationContextAware {

	private List<String> cacheDefinitions = new ArrayList<String>();
	private ApplicationContext applicationContext;

	public GlobalCacheManager() {
		cacheDefinitions.add("staticDataCache");
		cacheDefinitions.add("messageResourceCache");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.primovision.core.cache.CacheManager#clear()
	 */
	@Override
	public void clear() {
		for (String cacheName : cacheDefinitions) {
			Cache globalDataCache = (Cache) applicationContext
					.getBean(cacheName);
			globalDataCache.removeAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.primovision.core.cache.CacheManager#refreshAll()
	 */
	@Override
	public void refreshAll() throws Exception {
		for (String cacheName : cacheDefinitions) {
			ICacheQuery<Cacheable> cacheQuery = (ICacheQuery<Cacheable>) applicationContext
					.getBean(cacheName + "Query");
			List<Cacheable> cachedData = cacheQuery.findData();
			Cache globalDataCache = (Cache) applicationContext
					.getBean(cacheName);
			for (Cacheable cacheable : cachedData) {
				globalDataCache.put(new Element(cacheable.getKey(), cacheable));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primovision.core.cache.CacheManager#refreshCache(java.lang.String)
	 */
	@Override
	public void refreshCache(String cacheName) throws Exception {
		Cache globalDataCache = (Cache) applicationContext.getBean(cacheName);
		globalDataCache.removeAll();
		ICacheQuery<Cacheable> cacheQuery = (ICacheQuery<Cacheable>) applicationContext
				.getBean(cacheName + "Query");
		List<Cacheable> cachedData = cacheQuery.findData();
		for (Cacheable cacheable : cachedData) {
			globalDataCache.put(new Element(cacheable.getKey(), cacheable));
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
