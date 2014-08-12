package ru.sakhalinenergy.alarmtripsettings.util;


/**
 * String utility methods library.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class StringUtils 
{
    
    /**
     * Finds first non-numeric symbol in given string.
     * 
     * @param highstack String where non-numeric symbol will be searched
     * @return Position of first non numeric symbol in given string or -1 if no non-numeric symbols were found 
     */
    public static int findFirstNonNumeric(String highstack)
    {
        for (int i = 0; i < highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c < '0' || c > '9')
            {
                return i;
            }// if
        }// for
        
        return -1;
    }// findFirstNonNumeric
    
    
    /**
     * Finds last non-numeric symbol in given string.
     * 
     * @param highstack String where non-numeric symbol will be searched
     * @return Position of last non numeric symbol in given string or -1 if no non-numeric symbols were found 
     */
    public static int findLastNonNumeric(String highstack)
    {
        int lastIndex = -1;
        
        for (int i = 0; i < highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c < '0' || c > '9')
            {
                lastIndex = i;
            }// if
        }// for
        
        return lastIndex;
    }// findLastNonNumeric
    
    
    /**
     * Finds first numeric symbol in given string.
     * 
     * @param highstack String where numeric symbol will be searched
     * @return Position of first numeric symbol in given string or -1 if no numeric symbols were found 
     */
    public static int findFirstNumeric(String highstack)
    {
        for (int i = 0; i < highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c >= '0' && c <= '9')
            {
                return i;
            }// if
        }// for
        
        return -1;
    }// findFirstNumeric
    
    
    /**
     * Finds last numeric symbol in given string.
     * 
     * @param highstack String where numeric symbol will be searched
     * @return Position of last numeric symbol in given string or -1 if no numeric symbols were found 
     */
    public static int findLastNumeric(String highstack)
    {
        int lastIndex = -1;
        
        for (int i = 0; i < highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c >= '0' && c <= '9')
            {
                lastIndex = i;
            }// if
        }// for
        
        return lastIndex;
    }// findLastNumeric
    
    
    /**
     * Implements alphanumeric increment of given string, for example "124V3F"
     * increments to "124V3G".
     * 
     * @param original Input string
     * @param minDigit Increment range minimum symbol
     * @param maxDigit Increment range maximum symbol
     * @return Incremented string
     */
    public static String incrementString(String original, char minDigit, char maxDigit)
    {
        StringBuilder buf = new StringBuilder(original);
        int index = buf.length() -1;
        
        while(index >= 0)
        {
            char c = buf.charAt(index);
            c++;
       
            // Overflow, carry one:
            if(c > maxDigit) 
            { 
                buf.setCharAt(index, minDigit);
                index--;
                continue;
            }// if
            
            buf.setCharAt(index, c);
            return buf.toString();
        }// while
        
        // Overflow at the first "digit", need to add one more digit:
        buf.insert(0, minDigit);
        
        return buf.toString();
    }// incrementString
    
    
    /**
     * Implements alphanumeric decrement of given string, for example "124V3G"
     * decrements to "124V3F".
     * 
     * @param original Input string
     * @param minDigit Decrement range minimum symbol
     * @param maxDigit Decrement range maximum symbol
     * @return Decremented string
     */
    public static String decrementString(String original, char minDigit, char maxDigit)
    {
        StringBuilder buf = new StringBuilder(original);
        int index = buf.length() -1;
        
        while(index >= 0)
        {
            char c = buf.charAt(index);
            c--;
       
            // Overflow, carry one:
            if(c < minDigit) 
            { 
                buf.setCharAt(index, maxDigit);
                index--;
                continue;
            }// if
            
            buf.setCharAt(index, c);
            return buf.toString();
        }// while
        
        // Overflow at the first "digit", need to add one more digit:
        buf.insert(0, maxDigit);
        
        return buf.toString();
    }// decrementString
}// StringUtils
