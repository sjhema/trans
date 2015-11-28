package com.primovision.lutransport.core.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Single point of reference for all application configuration data. This class mirrors the java.util.Properties interface, but wraps a single static properties set. Configuration information can be loaded from a file or remote URL, or specified as command line arguments. Note that configuration data can also be specified explicitly as system properties using the standard Java command line option for setting property values. This class also will lookup configuration data contained within a file
 * or remote URL. The configuration data is merged from data sources in the order that are specified : later definitions of the same configuration parameter override any preceding definitions.
 * 
 * @see Properties#load
 */
public class Configuration {

    private static Properties properties = new Properties(System.getProperties());
    private static boolean loaded;

    /**
     * Load configuration data from URL or file, specified via standard system properties. Properties defined in a file override those defined via a URL. Does nothing if the standard properties ( <b>config.url </b> and <b>config.file </b>) are not defined. Note that the file may reside [as resources] within a jar file on the classpath. Any local file takes precedence over one within a jar file.
     */
    public static void load() throws Exception {
	String url = System.getProperty("config.url");
	if (url != null)
	    load(url);
	String filename = System.getProperty("config.file");
	if (filename != null) {
	    try {
		InputStream is = null;
		File file = new File(filename);
		if (file.exists())
		    is = new FileInputStream(file);
		else {
		    ClassLoader cl = Configuration.class.getClassLoader();
		    is = cl.getResourceAsStream(filename);
		}
		try {
		    load(is);
		} finally {
		    is.close();
		}
	    } catch (IOException x) {
		throw new Exception("Cannot read configuration file");
	    }
	}
    }

    /**
     * Load confiuration data from URL
     * 
     * @param url
     *            URL to load data from
     * @see Properties#load
     */
    public static void load(String url) throws Exception {
	if (url == null)
	    throw new Exception("Cannot read url");
	try {
	    load(new URL(url));
	} catch (IOException x) {
	    throw new Exception("Cannot read url");
	}
    }

    /**
     * Load configuration data from URL
     * 
     * @param url
     *            URL to load data frm
     * @see Properties#load
     */
    public static void load(URL url) throws IOException {
	load(new BufferedInputStream(url.openStream()));
    }

    /**
     * Load configuration data from input stream.
     * 
     * @param stream
     *            Stream to load data from
     * @see Properties#load
     */
    public static void load(InputStream stream) throws IOException {
	load(stream, false);
    }

    public static void load(InputStream stream, boolean isXml) throws IOException {
	if (!isXml) {
	    properties.load(stream);
	    loaded = true;
	} else {
	    try {
		Document document = new SAXReader().read(stream);
		@SuppressWarnings("unused")
		Element element = document.getRootElement();
	    } catch (DocumentException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Test if configuration has been loaded.
     * 
     * @return <b>true </b> if configuration has been explicitly loaded
     */
    public static boolean isLoaded() {
	return loaded;
    }

    /**
     * Assert that specified configuration parameter has been specified. If it has not been set a configuration exception is thrown.
     * 
     * @param param
     *            name of configuration parameter to check
     * @param msg
     *            error message if parameter has not been set
     * @exception Exception
     *                parameter has not been set
     */
    public static void require(String param, String message) throws Exception {
	if (!contains(param))
	    throw new Exception("Configuration parameters are not set");
    }

    /**
     * Tests if configuration parameter has been set.
     * 
     * @param name
     *            configuration parameter to test
     * @return true if parameter has been set, otherwise false
     */
    public static boolean contains(String name) {
	// test is for null value, as this also checks in System [containsKey()
	// does not]
	return properties.getProperty(name) != null;
    }

    /**
     * Get number of configuration parameters set.
     * 
     * @return number of configuration parameters set
     */
    public static int size() {
	return properties.size();
    }

    /**
     * Get configuration parameter.
     * 
     * @param name
     *            configuration parameter name
     * @return configuration parameter value, or <b>null </b> if parameter name not found
     */
    public static String getProperty(String name) {
	return properties.getProperty(name);
    }

    /**
     * Get configuration parameter.
     * 
     * @param name
     *            configuration parameter name
     * @param def
     *            default configuration parameter value
     * @return configuration parameter value, or default value if parameter name not found
     */
    public static String getProperty(String name, String def) {
	return properties.getProperty(name, def);
    }

    /**
     * Get configuration parameter, allowing for specifcation that the parameter is mandatory, ie. that it must be configured. If the parameter has not been set an exception is raised.
     * 
     * @param name
     *            configuration parameter name
     * @return configuration parameter value
     * @exception Exception
     *                configuration parameter not set
     */
    public static String getProperty(String name, boolean mandatory) throws Exception {
	if (mandatory && !contains(name))
	    throw new Exception("Missing configuration parameter");
	return (properties.getProperty(name).trim());
    }

    /**
     * Retrives the boolean value of a global application property. Values of <B>true </b>, <B>y </B>, <B>yes </B>, <B>on </B> and <B>1 </B> (number) are returned as <B>true </B>. Any other values, or if the property is not defined, are returned as <B>false </B>. Case is ignored when checking the property value.
     */
    public static boolean getPropertyAsBoolean(String name) {
	String value = getProperty(name);
	if (value == null)
	    return false;
	return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on")
		|| value.equalsIgnoreCase("1");
    }

    /**
     * Get copy of configuration parameters.
     * 
     * @return copy of configuration parameters
     */
    public static Properties getAll() {
	return new Properties(properties);
    }

    /**
     * Returns an read-only view of all configuration parameters.
     * 
     * @return read-only set of all configuration parameters
     */
    @SuppressWarnings("unchecked")
    public static Set entrySet() {
	return Collections.unmodifiableSet(properties.entrySet());
    }

    /**
     * List configuration data.
     * 
     * @param out
     *            output destination
     */
    public static void list(PrintStream out) {
	properties.list(out);
    }

    /**
     * List configuration data.
     * 
     * @param out
     *            output destination
     */
    public static void list(PrintWriter out) {
	properties.list(out);
    }

    /**
     * Saves configuration data.
     * 
     * @param out
     *            output destination
     * @param header
     *            comment header for output (can be <B>null </B>)
     * @see Properties#store
     */
    public static void store(OutputStream out, String header) throws IOException {
	properties.store(out, header);
    }

    /**
     * Set configuration parameter value. If the value supplied is null the configuration value is set to be an empty string (ie. "").
     * 
     * @param name
     *            configuration parameter name
     * @param value
     *            configuration prameter value
     */
    public static void setProperty(String name, String value) {
	if (value == null)
	    value = "";
	properties.setProperty(name, value);
    }
}
