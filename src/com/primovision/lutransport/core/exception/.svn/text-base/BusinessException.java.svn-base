package com.primovision.lutransport.core.exception;

/**
 * Generic class for all functional level exceptions. Information contaned in this exception will be shown to the application users and will not contain any technical details
 */
public class BusinessException extends ApplicationException {
    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     */
    public BusinessException(String message) {
	this(message, null, null);
    }

    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     * @param param
     *            exception message parameter
     */
    public BusinessException(String message, Object param) {
	this(message, new Object[] { param }, null);
    }

    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     * @param params
     *            exception message parameters
     */
    public BusinessException(String message, Object[] params) {
	this(message, params, null);
    }

    /**
     * Construct exception. Message is set to (x == null) ? null : x.toString().
     * 
     * @param x
     *            underlying exception
     */
    public BusinessException(Exception x) {
	this(null, null, x);
    }

    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     * @param x
     *            underlying exception
     */
    public BusinessException(String message, Exception x) {
	this(message, null, x);
    }

    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     * @param param
     *            exception message parameter
     * @param x
     *            underlying exception
     */
    public BusinessException(String message, Object param, Exception x) {
	this(message, new Object[] { param }, x);
    }

    /**
     * Construct exception.
     * 
     * @param message
     *            exception message, or localization key for actual message
     * @param params
     *            exception message parameters
     * @param x
     *            underlying exception
     */
    public BusinessException(String message, Object[] params, Exception x) {
	super("ApplicationResources", message, params, x);
    }
}
