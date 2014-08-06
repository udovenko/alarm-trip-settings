package ru.sakhalinenergy.alarmtripsettings.implemented;


/**
 * Implements built-in source properties type list and methods for getting them.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class SourcesPropertiesTypes
{
    public static class DOCUMENT_NUMBER
    {
        public static int ID = 1;
        public static String NAME = "Document number";
    }// DOCUMENT_NUMBER
    
    public static class DOCUMENT_LINK
    {
        public static int ID = 2;
        public static String NAME = "Document link";
    }// DOCUMENT_LINK
    
    public static class DATABASE_NAME
    {
        public static int ID = 3;
        public static String NAME = "Database name";
    }// DATABASE_NAME   
    
    public static class COMMENT
    {
        public static int ID = 4;
        public static String NAME = "Comment";
    }// COMMENT    
    
    
    /**
     * Returns source property type inner static class by given type id or null
     * if id is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param id Source property type identifier
     * @return Source property type class or null
     */
    public static Class<?> getSourcePropertyTypeById(int id) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SourceTypeClass : SourcesPropertiesTypes.class.getClasses())
        {
            if ((Integer)SourceTypeClass.getDeclaredField("ID").get(null) == id) return SourceTypeClass;
        }// for
        
        return null;
    }// getSourcePropertyTypeById
}// SourcesPropertiesTypes
