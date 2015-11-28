package com.primovision.lutransport.core.cache;

import java.io.Serializable;

/**
 * Interface to define whether an entity can be put into the cache
 * 
 * @author Rakesh
 * 
 */
public interface Cacheable extends Serializable {
    public String getKey();

    public String getValue();
}
