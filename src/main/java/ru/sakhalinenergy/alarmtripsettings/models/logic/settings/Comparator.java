package ru.sakhalinenergy.alarmtripsettings.models.logic.settings;


/**
 * Implements an abstract parent for classes with tag settings comparison logic.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public abstract class Comparator 
{
    
    // Comparison result types enumeration:
    protected static enum Compared
    { 
        AS_DOUBLE_EQUALS,
        AS_DOUBLE_LESS,
        AS_DOUBLE_MORE,
        AS_STRING_EQUALS,
        AS_STRING_NOT_EQUALS
    }// Compared
    
    protected final String POSSIBILITY_FLAG_POSITIVE_VALUE = "YES";
    protected final Double eps = 0.001;
    
    
    /**
     * Tries to compare given values as double with fixed epsilon. If it's
     * possible, returns one of Compared.AS_DOUBLE... constants, else compares
     * values as strings.
     *  
     * @param setpointOne First value to compare
     * @param setpointTwo Second value to compare
     * @return Comparison result type constant
     */
    protected Enum _compare(String setpointOne, String setpointTwo)
    {
        try // Try to compare values as double with epsilon precision:
        {
            // If first value is more than (second one + eps):
            if (Double.parseDouble(setpointOne.replace(',', '.')) - Double.parseDouble(setpointTwo.replace(',', '.')) > this.eps)
                return Compared.AS_DOUBLE_MORE; 
            
            // If (first value - eps) less than second one:
            else if (Double.parseDouble(setpointOne.replace(',', '.')) - Double.parseDouble(setpointTwo.replace(',', '.')) < -this.eps)
                return Compared.AS_DOUBLE_LESS; 
            else 
                return Compared.AS_DOUBLE_EQUALS;
            
        } catch (Exception exception){ // If it's impossible to compare values as double, compare them as strings:
            
            if (!setpointOne.equals(setpointTwo)) return Compared.AS_STRING_NOT_EQUALS; 
            else return Compared.AS_STRING_EQUALS;
        }// catch
    }// compareSetpoints
}// Logic
