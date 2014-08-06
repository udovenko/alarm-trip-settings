package ru.sakhalinenergy.alarmtripsettings.implemented;


/**
 * Implements built-in tags sources types list and methods for getting them.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class SourcesTypes
{
    public static String ID_FIELD_NAME = "ID";
    public static String NAME_FIELD_NAME = "NAME";
    
    public static class INTOOLS_EXPORT_DOCUMENT
    {
        public static int ID = 1;
        public static String NAME = "SmartPlant Instrumentation export";
    }// INTOOLS_EXPORT_DOCUMENT
    
    public static class ALARM_AND_TRIP_SCHEDULE
    {
        public static int ID = 2;
        public static String NAME = "Alarm and Trip schedule";
    }// ALARM_AND_TRIP_SCHEDULE
    
    public static class DCS_VARIABLE_TABLE
    {
        public static int ID = 3;
        public static String NAME = "DCS variable table";
    }// DCS_VARIABLE_TABLE
    
    public static class ESD_VARIABLE_TABLE
    {
        public static int ID = 4;
        public static String NAME = "ESD variable table";
    }// DCS_VARIABLE_TABLE
    
    public static class FGS_VARIABLE_TABLE
    {
        public static int ID = 5;
        public static String NAME = "FGS variable table";
    }// FGS_VARIABLE_TABLE
       
    
    /**
     * Returns source type inner static class by given source type name or null 
     * if name is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param name Source type name
     * @return Source type class or null
     */
    public static Class<?> getSourceTypeByName(String name) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SourceTypeClass : SourcesTypes.class.getClasses())
        {
            if (SourceTypeClass.getDeclaredField(NAME_FIELD_NAME).get(null).equals(name)) return SourceTypeClass;
        }// for
        
        return null;
    }// getSourceTypeByName
}// SourcesTypes
