package com.primovision.lutransport.logging;

/**
 * Interface for logging all the activities in application. Multiple implementations exist related to JavaLogger and log4j. This wrapper class provides access to both these implementatons by switching a configuration property.
 * 
 * @author rakesh
 */
public interface ILogger {

    /**
     * Initialize logging subsystem according to properties within the global application configuration. A basic logging configuration can be used by setting the <b>logging.basic </b> configuration parameter to <b>true </b>. If it is not used the default log4j initialization sequence is followed, which seeks a <b>log4j.properties </b> file by default. Note that this method only needs to be explicitly invoked by an application if the configuration has been changed during the program execution.
     */
    public void configure();

    /**
     * Get current logging context. If the root context is being used this value will be <b>null </b>.
     * 
     * @return context
     */
    public String getContext();

    /**
     * Set current logging context. If no logger has been configured for this context
     * 
     * @param context
     *            name of new current logging context (if <b>null </b> the root context is used)
     */
    public void setContext(String context);

    /**
     * Get logging package Logger object corresponding to current log context. If no logger has been configured for the required context the default root Logger is returned instead.
     * 
     * @return Logger that will be used for all current log requests
     */
    public Object getLogger();

    /**
     * Test if info messages are output.
     * 
     * @return <b>true </b> if info messages are output
     */
    public abstract boolean isInfoEnabled();

    /**
     * Test if debug messages are output.
     * 
     * @return <b>true </b> if debug messages are output
     */
    public abstract boolean isDebugEnabled();

    /**
     * Test if log messages are output for specified logging level.
     * 
     * @param level
     *            logging level name, as specified to Level constructor
     * @return <b>true </b> if log messages are output
     */
    public abstract boolean isLoggingEnabled(String level);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param message
     *            message key
     */
    public void fatal(String message);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void fatal(String message, Object param);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void fatal(String message, Object[] params);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     */
    public void fatal(String bundle, String message);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void fatal(String bundle, String message, Object param);

    /**
     * Log fatal error, and terminate application.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void fatal(String bundle, String message, Object[] params);

    /**
     * Log serious error.
     * 
     * @param message
     *            message key
     */
    public void error(String message);

    /**
     * Log serious error.
     * 
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void error(String message, Object param);

    /**
     * Log serious error.
     * 
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void error(String message, Object[] params);

    /**
     * Log serious error.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     */
    public void error(String bundle, String message);

    /**
     * Log serious error.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void error(String bundle, String message, Object param);

    /**
     * Log serious error.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void error(String bundle, String message, Object[] params);

    /**
     * Log warning.
     * 
     * @param message
     *            message key
     */
    public void warn(String message);

    /**
     * Log warning.
     * 
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void warn(String message, Object param);

    /**
     * Log warning.
     * 
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void warn(String message, Object[] params);

    /**
     * Log warning.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     */
    public void warn(String bundle, String message);

    /**
     * Log warning.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void warn(String bundle, String message, Object param);

    /**
     * Log warning.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void warn(String bundle, String message, Object[] params);

    /**
     * Log informational message.
     * 
     * @param message
     *            message key
     */
    public void info(String message);

    /**
     * Log informational message.
     * 
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void info(String message, Object param);

    /**
     * Log informational message.
     * 
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void info(String message, Object[] params);

    /**
     * Log informational message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     */
    public void info(String bundle, String message);

    /**
     * Log informational message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void info(String bundle, String message, Object param);

    /**
     * Log informational message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            message key
     * @param params
     *            parameter values
     */
    public void info(String bundle, String message, Object[] params);

    /**
     * Log debug message.
     * 
     * @param message
     *            message key
     */
    public void debug(String message);

    /**
     * Log debug message.
     * 
     * @param message
     *            message key
     * @param param
     *            parameter values
     */
    public void debug(String message, Object param);

    /**
     * Log debug message.
     * 
     * @param message
     *            key for debug message
     * @param params
     *            parameter values
     */
    public void debug(String message, Object[] params);

    /**
     * Log debug message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            key for debug message
     */
    public void debug(String bundle, String message);

    /**
     * Log debug message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            key for debug message
     * @param param
     *            parameter values
     */
    public void debug(String bundle, String message, Object param);

    /**
     * Log debug message.
     * 
     * @param bundle
     *            resource bundle for the message
     * @param message
     *            key for debug message
     * @param params
     *            parameter values
     */
    public void debug(String bundle, String message, Object[] params);
}
