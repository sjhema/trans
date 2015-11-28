package com.primovision.lutransport.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.primovision.lutransport.model.BaseModel;

@SuppressWarnings("unchecked")
public class CrudUtil {

    private static Logger log = Logger.getLogger(CrudUtil.class);

    /**
     * Creates the logging message for new audit logs
     * 
     * @param obj
     * @return
     */
    public static String buildCreateMessage(BaseModel obj) {
	StringBuffer message = new StringBuffer("Added ");
	message.append(obj.getClass().getSimpleName()) // class name
		.append(" ").append("id").append(":");
	Object value = retrieveObjectValue(obj, "id");
	if (value != null)
	    message.append(value.toString()).append(" - ");
	// loop through the fields list
	Field[] auditFields = obj.getClass().getDeclaredFields();
	int count = 0;
	String fieldName = null;
	for (Field property : auditFields) {
	    fieldName = property.getName();
	    fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
	    Object ret = retrieveObjectValue(obj, fieldName);
	    if (ret != null && ret.toString().trim().length() > 0) {
		if (count > 0)
		    message.append(",");
		message.append(fieldName).append("=").append(ret.toString());
		count++;
	    }
	}

	return message.toString();
    }

    /**
     * Creates the logging message for update audit logs
     * 
     * @param obj
     * @return
     */
    public static String buildUpdateMessage(BaseModel oldObject, BaseModel newObject) {

	StringBuffer message = new StringBuffer("Changed ");
	message.append(oldObject.getClass().getSimpleName()) // class name
		.append(" ").append("id").append(":");
	Object value = retrieveObjectValue(oldObject, "id");
	if (value != null)
	    message.append(value.toString()).append(" - ");
	// loop through the fields list
	Field[] auditFields = oldObject.getClass().getDeclaredFields();
	int count = 0;
	String fieldName = null;
	for (Field property : auditFields) {
	    fieldName = property.getName();
	    fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
	    Object oldValue = retrieveObjectValue(oldObject, fieldName);
	    Object newValue = retrieveObjectValue(newObject, fieldName);
	    if (oldValue == null)
		oldValue = new String("");
	    if (newValue == null)
		newValue = new String("");
	    if (!oldValue.equals(newValue)) {
		if (count > 0)
		    message.append(",");
		message.append(fieldName).append(" from ‘").append(oldValue.toString()).append("‘ to ‘").append(newValue.toString()).append("‘");
		count++;
	    }
	}
	return message.toString();
    }

    /**
     * Creates the logging message for new audit logs
     * 
     * @param obj
     * @return
     */
    public static String buildDeleteMessage(BaseModel obj) {
	StringBuffer message = new StringBuffer("Deleted ");
	message.append(obj.getClass().getSimpleName()) // class name
		.append(" ").append("id").append(":");
	Object value = retrieveObjectValue(obj, "id");
	if (value != null)
	    message.append(value.toString());
	return message.toString();
    }

    /**
     * Retrieves the property name for a method name. (e.g. getName will return name)
     * 
     * @param methodName
     * @return
     */
    public static String getPropertyName(String methodName) {
	if (StringUtils.isEmpty(methodName) || methodName.length() <= 3)
	    return null;
	if (methodName.startsWith("get") || methodName.startsWith("set")) {
	    String prop = methodName.substring(4);
	    char c = Character.toLowerCase(methodName.charAt(3));
	    return c + prop;
	} else
	    return null;
    }

    /**
     * Retrieves the getter method name for a given property. (e.g. name will return getName)
     * 
     * @param propertyName
     * @return
     */
    public static String getGetterMethodName(String propertyName) {
	if (StringUtils.isEmpty(propertyName) || propertyName.length() <= 0)
	    return null;
	char c = Character.toUpperCase(propertyName.charAt(0));
	return "get" + c + propertyName.substring(1);
    }

    /**
     * This method retrieves the object value that corresponds to the property specified. This method can recurse inner classes until specified property is reached. For example: obj.firstName obj.address.Zipcode
     * 
     * @param obj
     * @param property
     * @return
     */
    public static Object retrieveObjectValue(Object obj, String property) {
	if (property.contains(".")) {
	    // we need to recurse down to final object
	    String props[] = property.split("\\.");
	    try {
		Method method = obj.getClass().getMethod(getGetterMethodName(props[0]));
		Object ivalue = method.invoke(obj);
		if (ivalue == null)
		    return null;
		return retrieveObjectValue(ivalue, property.substring(props[0].length() + 1));
	    } catch (Exception e) {
		log.warn("Failed to retrieve value for " + property);
		return null;
	    }
	} else {
	    // let’s get the object value directly
	    try {
		Method method = obj.getClass().getMethod(getGetterMethodName(property));
		return method.invoke(obj);
	    } catch (Exception e) {
		log.warn("Failed to retrieve value for " + property);
		return null;
	    }
	}
    }

    /**
     * This method retrieves the object type that corresponds to the property specified. This method can recurse inner classes until specified property is reached. For example: obj.firstName obj.address.Zipcode
     * 
     * @param obj
     * @param property
     * @return
     */
    public static Class retrieveObjectType(Object obj, String property) {
	if (property.contains(".")) {
	    // we need to recurse down to final object
	    String props[] = property.split("\\.");
	    try {
		Method method = obj.getClass().getMethod(getGetterMethodName(props[0]));
		Object ivalue = method.invoke(obj);
		return retrieveObjectType(ivalue, property.substring(props[0].length() + 1));
	    } catch (Exception e) {
		log.warn("Failed to retrieve value for " + property);
		return null;
	    }
	} else {
	    // let’s get the object value directly
	    try {
		Method method = obj.getClass().getMethod(getGetterMethodName(property));
		return method.getReturnType();
	    } catch (Exception e) {
		log.warn("Failed to retrieve value for " + property);
		return null;
	    }
	}
    }
}