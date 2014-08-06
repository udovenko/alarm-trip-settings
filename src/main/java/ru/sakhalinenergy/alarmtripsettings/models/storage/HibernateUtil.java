package ru.sakhalinenergy.alarmtripsettings.models.storage;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettingsObservable;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.C3P0ConnectionProvider;


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
     * 
     */
    public static SessionFactory getSessionFactory() 
    {
        return sessionFactory;
    }//getSessionFactory
    
    
    /**
     * 
     * 
     */
    public static void setSessionFactory(StorageConnectionDialogSettingsObservable settings)
    {   System.out.println(System.getProperty("user.dir"));
        if(sessionFactory instanceof SessionFactoryImpl) 
        {
            SessionFactoryImpl sf = (SessionFactoryImpl)sessionFactory;
            ConnectionProvider conn = sf.getConnectionProvider();
            if(conn instanceof C3P0ConnectionProvider) 
            { 
                ((C3P0ConnectionProvider)conn).close(); 
            }//if
        }//if
        
        if (sessionFactory != null) sessionFactory.close();
    
        //if (sessionFactory != null) sessionFactory.close();
        Configuration hibernateConfig = new Configuration().configure();
        hibernateConfig.setProperty("hibernate.connection.url", "jdbc:sqlite:" + settings.getSqliteDatabasePath());
        sessionFactory = hibernateConfig.buildSessionFactory();
    }//setConfiguration
}//HibernateUtil
