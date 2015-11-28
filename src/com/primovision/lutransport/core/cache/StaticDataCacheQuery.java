package com.primovision.lutransport.core.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.StaticData;

@Repository("staticDataCacheQuery")
public class StaticDataCacheQuery implements ICacheQuery<StaticData> {
    @Autowired
    protected GenericDAO genericDAO;

    public void setGenericDAO(GenericDAO genericDAO) {
	this.genericDAO = genericDAO;
    }

    @Override
    public List<StaticData> findData() {
	return genericDAO.findAll(StaticData.class, false);
    }

}
