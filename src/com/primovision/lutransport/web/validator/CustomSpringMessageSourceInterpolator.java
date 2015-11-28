/**
 * 
 */
package com.primovision.lutransport.web.validator;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.engine.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;

/**
 * Custom MessageInterpolator which delegates the message codes to MessageSource Default is - ValidationMessages.properties
 * 
 * @author gaurav
 */
public class CustomSpringMessageSourceInterpolator extends ResourceBundleMessageInterpolator implements MessageInterpolator, MessageSourceAware,
	InitializingBean {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String interpolate(String messageTemplate, Context context) {
	try {
	    return messageSource.getMessage(messageTemplate, new Object[] {}, Locale.getDefault());
	} catch (NoSuchMessageException e) {
	    return super.interpolate(messageTemplate, context);
	}
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
	try {
	    return messageSource.getMessage(messageTemplate, new Object[] {}, locale);
	} catch (NoSuchMessageException e) {
	    return super.interpolate(messageTemplate, context, locale);
	}
    }

    public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
    }

    public void afterPropertiesSet() throws Exception {
	if (messageSource == null) {
	    throw new IllegalStateException("MessageSource was not injected, could not initialize " + this.getClass().getSimpleName());
	}
    }
}
