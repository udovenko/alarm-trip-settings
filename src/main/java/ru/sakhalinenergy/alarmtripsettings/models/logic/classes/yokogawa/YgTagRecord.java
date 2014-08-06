package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Implements tag record entity structure in Yokogawa DCS backup.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class YgTagRecord 
{
    private final String tagName;
    private final String tagComent;
    private final String upperPanel;
    private final byte alarmType;
    private final byte accessLevel;
    private final byte elemTypeOne;
    private final byte elemTypeTwo;
    private final int elemNo;
    private final int helpNo;
    private final int instNo;
    private final int plantNo;
    private final long auxPointer;
    private final byte instType;
    private final byte auxCodeOne;
    private final byte dataDisp;
    private final byte auxCodeTwo;
    private final byte instMark;
    private final byte tagMark;
    private final byte dp;
    private final byte tp;
    private final byte rp;
    private final byte mvdp;
    private final byte ocMark;
    private final byte divideNo;
    private final byte labelCode;
    private final byte userCodeOne;
    private final byte userCodeTwo;
    private final byte userCodeThree;
    private final int mvUnitsIndex;
    private final int engUnitsIndex;
    private final long sh;
    private final long sl;
    
    
    /**
     * Public constructor. Parses given bytes array to segments and translates 
     * them to instance fields according to backup tag record structure.
     * 
     * @throws RuntimeException
     * @param record Tag record data bytes array
     */
    public YgTagRecord(byte[] record)
    {
        if (record.length != 128) throw new RuntimeException("Yokogawa Tag Record constructor: Wrong parameter length!");
        
        byte[] temp2BytesArray = new byte[2];
        byte[] temp4BytesArray = new byte[4];
        byte[] temp16BytesArray = new byte[16];
                
        // Read tag name:
        System.arraycopy(record, 0, temp16BytesArray, 0, 16);
        tagName = new String(temp16BytesArray);
        
        // Read tag's comment:
        byte[] tagCmntBytes = new byte[24];
        System.arraycopy(record, 16, tagCmntBytes, 0, 24);
        tagComent = new String(tagCmntBytes);
        
        // Read "upperPanel" parameter value:
        System.arraycopy(record, 40, temp16BytesArray, 0, 16);
        upperPanel = new String(temp16BytesArray);        
        
        // Read bytes fields:
        alarmType = record[56];
        accessLevel = record[57];
        elemTypeOne = record[58];
        elemTypeTwo = record[59];
        
        // Read "elemNo" parameter:
        System.arraycopy(record, 60, temp2BytesArray, 0, 2);
        elemNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        // Read "helpNo" parameter:
        System.arraycopy(record, 62, temp2BytesArray, 0, 2);
        helpNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        // Read "instNo" parameter:
        System.arraycopy(record, 64, temp2BytesArray, 0, 2);
        instNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        // Read "plantNo" parameter:
        System.arraycopy(record, 66, temp2BytesArray, 0, 2);
        plantNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
               
        // Read "auxPointer" parameter:
        System.arraycopy(record, 68, temp4BytesArray, 0, 4);
        auxPointer = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);
        
        // Read byte fields values:
        instType      = record[72];
        auxCodeOne    = record[73];
        dataDisp      = record[74];      
        auxCodeTwo    = record[75];
        instMark      = record[76];
        tagMark       = record[77];
        dp            = record[78];
        tp            = record[79];
        rp            = record[80];
        mvdp          = record[81];
        ocMark        = record[82];
        divideNo      = record[83];
        labelCode     = record[84];
        userCodeOne   = record[85];
        userCodeTwo   = record[86];
        userCodeThree = record[87];
        
        // Read "mvUnit" parameter:
        System.arraycopy(record, 88, temp2BytesArray, 0, 2);
        mvUnitsIndex = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        // Read units of measure index parameter:
        System.arraycopy(record, 90, temp2BytesArray, 0, 2);
        engUnitsIndex = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        // Read range max parameter:
        System.arraycopy(record, 92, temp4BytesArray, 0, 4);
        sh = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);  
        
        // Read range min parameter:
        System.arraycopy(record, 96, temp4BytesArray, 0, 4);
        sl = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);  
    }// YgTagRecord
    
    
    /**
     * Returns tag name parameter value.
     * 
     * @return Tag name parameter value
     */
    public String getTagName()
    {
        return tagName;
    }// getTagName
    
    
    /**
     * Returns "dp" parameter value.
     * 
     * @return "dp" parameter value
     */
    public byte getDp()
    {
        return dp;
    }// getDp
    
    
    /**
     * Returns "mvUnits" parameter value.
     * 
     * @return "mvUnits" parameter value
     */
    public int getMvUnits()
    {
        return mvUnitsIndex;
    }// getMvUnits
    
    
    /**
     * Returns engineering units index parameter value.
     * 
     * @return Engineering units
     */
    public int getEngUnitsIndex()
    {
        return engUnitsIndex;
    }// getEngUnitsIndex
    
    
    /**
     * Returns range max parameter value.
     * 
     * @return Range max parameter value
     */
    public long getSh()
    {
        return sh;
    }// getSh
    
    
    /**
     * Returns range min parameter value.
     * 
     * @return Range min parameter value
     */
    public long getSl()
    {
        return sl;
    }// getSl
}// YgTagRecord
