package ru.sakhalinenergy.alarmtripsettings.implemented;


/**
 * Implements built-in setting types list and methods for getting them.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class SettingsTypes 
{
    public static String ID_FIELD_NAME = "ID";
    public static String NAME_FIELD_NAME = "NAME";
        
    public static class ALARM_LL_SETTING
    {
        public static int ID = 1;
        public static String NAME = "Alarm LL";
    }// ALARM_LL_SETTING
    
    public static class ALARM_L_SETTING
    {
        public static int ID = 2;
        public static String NAME = "Alarm L";
    }// ALARM_L_SETTING
    
    public static class ALARM_H_SETTING
    {
        public static int ID = 3;
        public static String NAME = "Alarm H";
    }// ALARM_H_SETTING
    
    public static class ALARM_HH_SETTING
    {
        public static int ID = 4;
        public static String NAME = "Alarm HH";
    }// ALARM_HH_SETTING
    
    public static class RANGE_MIN_SETTING
    {
        public static int ID = 5;
        public static String NAME = "Range Min";
    }// RANGE_MIN_SETTING
    
    public static class RANGE_MAX_SETTING
    {
        public static int ID = 6;
        public static String NAME = "Range Max";
    }// RANGE_MAX_SETTING
    
    public static class UNITS_SETTING
    {
        public static int ID = 7;
        public static String NAME = "Units";
    }// UNITS_SETTING
        
    public static class SOURCE_SYSTEM_SETTING
    {
        public static int ID = 8;
        public static String NAME = "Source system";
    }// SOURCE_SYSTEM_SETTING
    
    
    /**
     * Returns setting type inner static class by given setting type id or
     * null if id is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param id Setting type identifier
     * @return Setting type class or null
     */
    public static Class<?> getTypeById(int id) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SettingsTypeClass : SettingsTypes.class.getClasses())
        {
            if (SettingsTypeClass.getDeclaredField(SettingsTypes.ID_FIELD_NAME).get(null).equals(id)) return SettingsTypeClass;
        }// for
        
        return null;
    }// getPlantByName
    
    
    /**
     * Returns setting type inner static class by given setting name or
     * null if name is invalid.
     * 
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @param name Setting type name
     * @return Setting type class or null
     */
    public static Class<?> getTypeByName(String name) throws NoSuchFieldException, IllegalAccessException
    {
        for (Class<?> SettingsTypeClass : SettingsTypes.class.getClasses())
        {
            if (SettingsTypeClass.getDeclaredField(SettingsTypes.NAME_FIELD_NAME).get(null).equals(name)) return SettingsTypeClass;
        }// for
        
        return null;
    }// getPlantByName
}// Settings
