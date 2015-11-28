package com.primovision.lutransport.core.util;

import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Global resource bundle cache. Maintains a global cache of resource bundles, providing methods for extracting strings, objects and arrays from them without having to reload the resource bundles. This is both faster, and simpler for applications as they do not have to maintain references to multiple resource bundles.
 * 
 * @see ResourceBundle for description of parameter values.
 */
public class ResourceBundleCache {
    private static Hashtable _bundles = new Hashtable();

    /**
     * Get an object from a ResourceBundle.
     * 
     * @see ResourceBundle#getObject
     */
    public static Object getObject(String base, Locale locale, String key) throws MissingResourceException {
	return getBundle(base, locale).getObject(key);
    }

    /**
     * Get string from ResourceBundle.
     * 
     * @see ResourceBundle#getString
     */
    public static String getString(String base, Locale locale, String key) throws MissingResourceException {
	return getBundle(base, locale).getString(key);
    }

    /**
     * Get string array from ResourceBundle.
     * 
     * @see ResourceBundle#getStringArray
     */
    public static String[] getStringArray(String base, Locale locale, String key) throws MissingResourceException {
	return getBundle(base, locale).getStringArray(key);
    }

    /**
     * Get resource bundle.
     * 
     * @see ResourceBundle#getBundle
     */
    public static ResourceBundle getBundle(String base, Locale locale) throws MissingResourceException {
	String key = base + "_" + locale.toString();
	ResourceBundle bundle = (ResourceBundle) _bundles.get(key);

	if (bundle == null) {
	    bundle = ResourceBundle.getBundle(base, locale);
	    _bundles.put(key, bundle);
	}

	return bundle;
    }

    /**
     * Add resource bundle into global cache.
     * 
     * @param base
     *            resource bundle base name
     * @param bundle
     *            resource bundle to add to global cache
     */
    public static void cacheBundle(String base, ResourceBundle bundle) {
	String key = base + "_" + bundle.getLocale().toString();
	_bundles.put(key, bundle);
    }
}
