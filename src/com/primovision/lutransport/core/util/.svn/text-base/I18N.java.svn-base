package com.primovision.lutransport.core.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

import com.primovision.lutransport.core.configuration.Configuration;

/**
 * This class supports localization for the display of arbitary messages. It is intended to provide a mechanism for localizing strings where an API requires a user-visible string parameter, eg. those passed into constructors for Java system or 3rd party exceptions, or window titles, etc. <p> The <i>key</i> argument may be either a raw value or a localization key. During formatting, if the key can be located within the global application resource bundle it is replaced by the localized value.
 * Otherwise the original <i>key</i> string is used. Formatting uses java.text.MessageFormat style formatting to format parameters, so for example a format string "{0} {1}" would format two parameters as strings. As a convenience methods are provided that take a class object and key as parameters : these are converted into a key argument of the form <i>fully-qualified-class-name.key</i> for lookup in the resource bundle. <p> Convenience constructors that take a single parameter are provided, in
 * addition to the constructors taking an array of arbitary parameters. <p> The default resource bundle is "application", unless overridden via the <b>resource.bundle</b> configuration parameter.
 * 
 * @see java.text.MessageFormat
 */
public class I18N {
    private static String _bundle;

    /**
     * Retrieve localized text string from the default resource bundle.
     * 
     * @param key
     *            text string, or localization key for actual message
     */
    public static String getText(String key) {
	return getFormattedText(null, key, null);
    }

    /**
     * Retrieve localized text string from default resource bundle.
     * 
     * @param message
     *            text string, or localization key for actual message
     * @param param
     *            text string parameter
     */
    public static String getText(String key, Object param) {
	return getFormattedText(null, key, new Object[] { param });
    }

    /**
     * Retrieve localized text string from default resource bundle.
     * 
     * @param bundle
     *            resource bundle base name (may be <b>null</b>)
     * @param message
     *            text string, or localization key for actual message
     * @param params
     *            text string parameters
     */
    public static String getText(String key, Object[] params) {
	return getFormattedText(null, key, params);
    }

    /**
     * Retrieve localized text string. <p> If no resource bundle is explicitly specified it is defaulted to that specified by the <b>resource.bundle</b> configuration parameter, or "application" if that is also not set.
     * 
     * @param bundle
     *            resource bundle base name (may be <b>null</b>)
     * @param key
     *            text string, or localization key for actual message
     */
    public static String getText(String bundle, String key) {
	return getFormattedText(bundle, key, null);
    }

    /**
     * Retrieve localized text string. <p> If no resource bundle is explicitly specified it is defaulted to that specified by the <b>resource.bundle</b> configuration parameter, or "application" if that is also not set.
     * 
     * @param bundle
     *            resource bundle base name (may be <b>null</b>)
     * @param message
     *            text string, or localization key for actual message
     * @param param
     *            text string parameter
     */
    public static String getText(String bundle, String key, Object param) {
	return getFormattedText(bundle, key, new Object[] { param });
    }

    /**
     * Retrieve localized text string. <p> If no resource bundle is explicitly specified it is defaulted to that specified by the <b>resource.bundle</b> configuration parameter, or "application" if that is also not set.
     * 
     * @param bundle
     *            resource bundle base name (may be <b>null</b>)
     * @param message
     *            text string, or localization key for actual message
     * @param params
     *            text string parameters
     */
    public static String getText(String bundle, String key, Object[] params) {
	return getFormattedText(bundle, key, params);
    }

    /**
     * Retrieve raw resource value. <p> If no resource bundle is explicitly specified it is defaulted to that specified by the <b>resource.bundle</b> configuration parameter, or "application" if that is also not set.
     * 
     * @param bundle
     *            resource bundle base name (may be <b>null</b>)
     * @param message
     *            localization key for resource value
     */
    public static String getResourceValue(String bundle, String key) throws MissingResourceException {
	// use default bundle if bundle not explictly specified
	if (bundle == null) {
	    if (_bundle == null)
		_bundle = Configuration.getProperty("resource.bundle", "application");

	    bundle = _bundle;
	}

	return ResourceBundleCache.getString(bundle, Locale.getDefault(), key);
    }

    /**
     * Get formatted and localized message string.
     */
    private static String getFormattedText(String bundle, String key, Object[] params) {
	// locate localization key value (if set)
	String message = null;

	try {
	    message = getResourceValue(bundle, key);
	} catch (MissingResourceException x) {
	    // ignore the error
	    // System.err.println( "missing resource: [" + bundle + "]/" + key );
	} finally {
	    if (message == null)
		message = key;
	}

	// format message using supplied parameters
	// (if parameters exist, and message is parameterized)
	if (params == null || message.indexOf("{0") == -1)
	    return message;

	return MessageFormat.format(message, params);
    }
}
