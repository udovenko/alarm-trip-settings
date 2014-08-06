package ru.sakhalinenergy.alarmtripsettings.implemented;


/**
 * Implements built-in settings properties list and methods for getting them.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class SettingsPropertiesTypes 
{
    public static String ID_FIELD_NAME = "ID";
    public static String NAME_FIELD_NAME = "NAME";
    
    
    public static class POSSIBLE_SETTING_PROPERTY
    {
        public static int ID = 1;
        public static String NAME = "Possible";
    }// POSSIBLE_SETTING_PROPERTY 
    
    
    /**
     * Returns inner static class with setting property info by given property
     * type id or null if id is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param id Setting type identifier
     * @return Setting property class of null
     */
    public static Class<?> getSettingsPropertyTypeById(int id) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SettingsPropertyTypeClass : SettingsPropertiesTypes.class.getClasses())
        {
            if (SettingsPropertyTypeClass.getDeclaredField(SettingsPropertiesTypes.ID_FIELD_NAME).get(null).equals(id)) return SettingsPropertyTypeClass;
        }// for
        
        return null;
    }// getSettingsPropertyTypeById
    
    
    /**
     * Returns inner static class with setting property info by given property
     * name or null if name is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param name Setting type name
     * @return Setting property class or null
     */
    public static Class<?> getSettingsPropertyTypeByName(String name) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SettingsPropertyTypeClass : SettingsPropertiesTypes.class.getClasses())
        {
            if (SettingsPropertyTypeClass.getDeclaredField(SettingsPropertiesTypes.NAME_FIELD_NAME).get(null).equals(name)) return SettingsPropertyTypeClass;
        }// for
        
        return null;
    }// getSettingsPropertyTypeByName
}// SettingsPropertiesTypes
