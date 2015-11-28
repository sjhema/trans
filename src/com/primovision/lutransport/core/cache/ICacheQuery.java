package com.primovision.lutransport.core.cache;

import java.util.List;

public interface ICacheQuery<T> {

    public List<T> findData();
}
