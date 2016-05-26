package com.packex.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.packex.Constants;
import com.packex.connector.BigQueryConnector;

public class LanguageManagerFactory {
    private static final Logger logger = Logger.getLogger(LanguageManagerFactory.class.getName());
    
    private static LanguageManagerFactory instance;
    private LanguageManagerFactory() { }
    
    public static LanguageManagerFactory getInstance() {
        if (instance == null) {
            instance = new LanguageManagerFactory();
        }
        
        return instance;
    }
    
    public LanguageManager getLanguageManager(
            BigQueryConnector connector, String language, String companyName, String packageName, String category) {
        LanguageManager manager = null;
        if (language.toUpperCase().equals(Constants.PHP_LANGUAGE)) {
            manager = new PhpManager(connector, companyName, packageName, category);
        }
        else if (language.toUpperCase().equals(Constants.NODE_LANGUAGE)) {
            manager = new NodeManager(connector, companyName, packageName, category);
        }
        else if (language.toUpperCase().equals(Constants.RUBY_LANGUAGE)) {
            manager = new RubyManager(connector, companyName, packageName, category);
        }
        else if (language.toUpperCase().equals(Constants.PYTHON_LANGUAGE)) {
            manager = new PythonManager(connector, companyName, packageName, category);
        }
        else if (language.toUpperCase().equals(Constants.DOTNET_LANGUAGE)) {
            manager = new DotnetManager(connector, companyName, packageName, category);
        }
        else {
            logger.log(Level.SEVERE, String.format("We don't support yet the language \"%s\"", language));
        }
        
        return manager;
    }
}
