package ru.sakhalinenergy.alarmtripsettings.util;


/**
 * Data types utility methods library.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class DataTypesUtils 
{
    
    /**
     * Reads two bytes array as Unsigned short and returns result in Integer.
     * 
     * @throws RuntimeException
     * @param bytes Input bytes array
     * @return Unsigned short stored in Integer
     */
    public static int readBytesAsUnsignedShort(byte[] bytes)
    {
        if (bytes.length != 2) throw new RuntimeException("Method readBytesAsUnsignedShort: Wrong parameter length!");
        else return ((bytes[0] & 0xFF) << 0) | ((bytes[1] & 0xFF) << 8);
    }// readBytesAsUnsignedShort
    
    
    /**
     * Reads four bytes array as Unsigned integer and stores result in Long.
     * 
     * @param bytes Input bytes array
     * @return Unsigned integer stored in Long
     */
    public static long readAsUnsignedLong(byte[] bytes)
    {
        if (bytes.length == 4)
        {
            long value = 
                ((bytes[0] & 0xFF) <<  0) |
                ((bytes[1] & 0xFF) <<  8) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[3] & 0xFF) << 24);
            
            return value;
        } else {
        
            throw new RuntimeException("Method readAsUnsignedLong: Wrong parameter length!");
        }// else
    }// readAsUnsignedLong
}// DataTypesUtils
