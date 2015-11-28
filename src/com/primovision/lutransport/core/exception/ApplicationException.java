package com.primovision.lutransport.core.exception;

import com.primovision.lutransport.core.util.I18N;

/**
 * Generic exception class for service A chained exception mechanism is used to provide access to the low-level cause of a higher-level exception. This class supports localization for the display of exception messages. The <i>message</i> argument may be either a raw value or a localization key. During formatting, if the key can be located within the global application resource bundle it is replaced by the localized value. Otherwise the original <i>message</i> string is used. Formatting uses
 * java.text.MessageFormat style formatting to format parameters, so for example a format string "{0} {1}" would format two parameters as strings. As a convenience methods are provided that take a class object and key as parameters : these are converted into a key argument of the form <i>fully-qualified-class-name.key</i> for lookup in the resource bundle. Formatting, which is a relatively heavyweight function, will only occur when the getMessage() or toString() methods are invoked.
 * 
 * @see java.text.MessageFormat
 * @author rakesh
 */
public class ApplicationException extends Exception {

    /** underlying exception */
    private Exception cause;
    /** resource bundle for the message */
    private String resource;
    /** message key */
    private String key;
    /** parameters values for the message */
    private Object[] params;

    /**
     * Construct exception.
     * 
     * @param resource
     *            resource bundle base name
     * @param message
     *            exception message, or localization key for actual message
     * @param params
     *            exception message parameters
     * @param x
     *            underlying exception
     */
    protected ApplicationException(String resource, String message, Object[] params, Exception x) {
	super(message);
	cause = x;
	this.resource = resource;
	this.params = params;
    }

    /**
     * Return underlying exception.
     * 
     * @return underlying exception <b>null</b> if none
     */
    public Throwable getCause() {
	return cause;
    }

    /**
     * Return exception message. Overriden to ensure message is localized.
     * 
     * @return localized exception message
     * @param locale
     *            language locale for the message
     */
    public String getMessage(String locale) {
	return getLocalizedMessage(locale);
    }

    /**
     * Return exception message. Overriden to ensure message is localized.
     * 
     * @return localized exception message
     */
    public String getMessage() {
	return super.getMessage();
    }

    /**
     * Return localized exception message. The result is the top-level reason, and details plus a chain of underlying exceptions (allowing the root cause to be ascertained). The string returned is formatted according to the value of the specified localization key (if it could be located).
     * 
     * @param locale
     *            language locale for the message
     * @return localized exception message
     */
    public String getLocalizedMessage(String locale) {
	StringBuffer result = new StringBuffer();
	result.append(getFormattedMessage(locale));
	if (getCause() != null) {
	    StringBuffer cause = new StringBuffer();
	    getReason(cause, getCause(), locale);
	    result.append("/");
	    result.append(cause.toString());
	}
	return result.toString();
    }

    /**
     * Convert exception to string representation.
     * 
     * @return localized exception message
     */
    public String toString() {
	return getLocalizedMessage(null);
    }

    /**
     * Get formatted message for this exception instance.
     * 
     * @param locale
     *            language locale for the message
     * @return formatted message
     */
    private String getFormattedMessage(String locale) {
	return I18N.getText(resource, super.getMessage(), params);
    }

    /**
     * Get string representation of underlying cause of exception
     * 
     * @param buffer
     *            holds exception messages
     * @param exception
     *            holds underlying exception
     * @param locale
     *            language locale for the message
     */
    private void getReason(StringBuffer buffer, Throwable exception, String locale) {
	if (exception == null)
	    return;
	else if (exception instanceof ApplicationException) {
	    ApplicationException x = (ApplicationException) exception;
	    buffer.append(x.getFormattedMessage(locale));
	    if (x.getCause() != null) {
		buffer.append("/");
		getReason(buffer, x.getCause(), locale);
	    }
	} else
	    buffer.append(exception.toString());
    }
}
