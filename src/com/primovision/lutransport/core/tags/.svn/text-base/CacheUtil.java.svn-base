package com.primovision.lutransport.core.tags;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.primovision.lutransport.core.cache.Cacheable;
import com.primovision.lutransport.web.SpringAppContext;

public class CacheUtil {
	

	public static String getText(String cacheName, String key) {
        Cache cache = (Cache)SpringAppContext.getBean(cacheName);
		Element element = cache.get(key);
        if(element != null)
        {
        	Cacheable cacheable=(Cacheable)element.getValue();
    		return cacheable.getValue();
        }
        return key.split("_")[1];
	}
}
