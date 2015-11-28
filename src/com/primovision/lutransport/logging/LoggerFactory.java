package com.primovision.lutransport.logging;

import com.primovision.lutransport.core.configuration.Configuration;

/**
 * Factory class for creating different logger implementations
 * 
 * @author rakesh
 */
public class LoggerFactory {

    private static String loggerClass;

    /**
     * Factory method which returns logger instance
     * @param context Source class of logging
     * @return logger interface
     */
    public static ILogger getLogger(String context){
        if (loggerClass == null)
            initialize();
        ILogger logger = null;
        Class clazz;
        try {
            clazz = Class.forName(loggerClass);
            logger = (ILogger)clazz.newInstance();
            logger.setContext(context);
        }
        catch(ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return logger;
    }

    /**
     * Method to initialize base configuration
     */
    private static void initialize(){
        loggerClass = Configuration.getProperty("logger.type");
        if (loggerClass == null) {
            System.out.println("No logger configuration found using default Logger (java)");
            loggerClass = "com.neovartis.jobportal.logging.Log4jLogger";
        }
    }
}
