/**
 * 
 */
package com.primovision.lutransport.core.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.admin.MessageResource;

@Repository("messageResourceCacheQuery")
public class MessageResourceCacheQuery implements ICacheQuery<MessageResource> {

    @Autowired
    private GenericDAO genericDAO;

    public void setGenericDAO(GenericDAO genericDAO) {
	this.genericDAO = genericDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.primovision.erp.core.cache.ICacheQuery#findData()
     */
    @Override
    public List<MessageResource> findData() {
	return genericDAO.findAll(MessageResource.class);
    }

}
