package com.primovision.lutransport.controller.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;


public class AbstractModelEditor extends PropertyEditorSupport {

	private Class<?> clazz;
	
	public AbstractModelEditor(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	private Long getPkFromString(String text) {
		if (!StringUtils.isEmpty(text))
			return Long.parseLong(text);
		return null;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		AbstractBaseModel model = null;
		if (!StringUtils.isEmpty(text)) {
			model = getEntityInstance(); 
			model.setId(getPkFromString(text));
		}
		setValue(model);
	}
	
	
    protected AbstractBaseModel getEntityInstance()
    {
        try {
            return (AbstractBaseModel)clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}