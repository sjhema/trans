package com.primovision.lutransport.core.cache;

/**
 * Interface for data storage linked to object cache. Cache manager is used to populate the cache with data
 */
public interface CacheManager {

    public void refreshCache(String cacheName) throws Exception;

    /**
     * Ensure data is up to date within the local cache.
     * 
     * @execption Exception data retrieval operation failed
     */
    public void refreshAll() throws Exception;

    /**
     * Respond to operation to clear cache of data. This method exists so that data sources have an opportunity to implement a scheme whereby the first refresh call retrieves all data items, and subsequent ones retrieve data updates. In such a scheme a full refresh is required after cached data has been cleared.
     */
    public void clear();
}
