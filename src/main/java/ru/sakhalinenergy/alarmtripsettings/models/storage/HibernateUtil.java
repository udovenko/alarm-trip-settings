package ru.sakhalinenergy.alarmtripsettings.models.storage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.C3P0ConnectionProvider;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettingsObservable;


/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class HibernateUtil
{
    private static SessionFactory sessionFactory;

    
    /**
     * Returns current Hibernate session factory.
     * 
     * @return Current Hibernate session factory
     */
    public static SessionFactory getSessionFactory() 
    {
        return sessionFactory;
    }// getSessionFactory
    
    
    /**
     * Sets up Hibernate session factory with given configuration.
     * 
     * @param settings Storage connection configuration object
     */
    public static void setSessionFactory(StorageConnectionDialogSettingsObservable settings)
    {   
        // Close previous C3P0 connection:
        if(sessionFactory instanceof SessionFactoryImpl) 
        {
            SessionFactoryImpl sf = (SessionFactoryImpl)sessionFactory;
            ConnectionProvider conn = sf.getConnectionProvider();
            if(conn instanceof C3P0ConnectionProvider) 
            { 
                ((C3P0ConnectionProvider)conn).close(); 
            }// if
        }// if
        
        // Close previous session factory:
        if (sessionFactory != null) sessionFactory.close();
    
        // Create and configure new session factory:
        Configuration hibernateConfig = new Configuration().configure();
        hibernateConfig.setProperty("hibernate.connection.url", "jdbc:sqlite:" + settings.getSqliteDatabasePath());
        sessionFactory = hibernateConfig.buildSessionFactory();
    }// setConfiguration
}// HibernateUtil
