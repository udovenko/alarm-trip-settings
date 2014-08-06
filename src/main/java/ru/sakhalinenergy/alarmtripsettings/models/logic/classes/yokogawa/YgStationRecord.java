package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;


/**
 * Implements station record entity in DCS Yokogawa backup.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class YgStationRecord 
{
    private String stationName;
    private byte[] rest;
    
    
    /**
     * Public constructor. Parses given bytes array to segments and translates 
     * them to instance fields according to backup station record structure.
     * 
     * @throws RuntimeException
     * @param stationBytes Station record data bytes array
     */
    public YgStationRecord(byte[] stationBytes)
    {
       if (stationBytes.length != 256) throw new RuntimeException("YgStationRecord constructor error: Wrong parameter lenth!");
       
       int bytesRead = 0;
       byte[] temp8ByteArray = new byte[8];
       byte[] temp248ByteArray = new byte[248];
       
       // Read station name:
       System.arraycopy(stationBytes, 0, temp8ByteArray, 0, 8);
       stationName = new String(temp8ByteArray);
       bytesRead += temp8ByteArray.length;
       
       // Read remainig bytes array:
       System.arraycopy(stationBytes, 8, temp248ByteArray, 0, 248);
       rest = temp248ByteArray;
       bytesRead += temp248ByteArray.length;
       
       // Check that array was fully read:
        if (stationBytes.length != bytesRead) throw new RuntimeException("YgStationRecord constructor error: Wrong number of bytes read!"); 
    }// YgStationRecord
    
    
    /**
     * Returns station name parameter value.
     * 
     * @return Station name
     */
    public String getStationName()
    {
        return stationName;
    }// getStationName
    
    
    /**
     * Overrides parent "toString()" method. Returns station name string instead.
     * 
     * @return Station name string
     */
    @Override
    public String toString()
    {
        return stationName.trim();
    }// toString
}// YgStationRecord
