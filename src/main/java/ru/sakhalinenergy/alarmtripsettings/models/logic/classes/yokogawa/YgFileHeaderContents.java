package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Implements contents element of Yokogawa DCS backup file header.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class YgFileHeaderContents 
{
    private String  item;
    private long    offset;
    private long    number;
    private long    pitch;
    
    
    /**
     * Public constructor. Parses given bytes array to segments and translates 
     * them to instance fields according to backup file header contents element
     * structure.
     * 
     * @throws RuntimeException
     * @param contentsBytes File header contents element data bytes array
     */
    public YgFileHeaderContents(byte[] contentsBytes)
    {
        // Check given bytes array length:
        if (contentsBytes.length != 16) throw new RuntimeException("YgFileHeaderContents constructor error: Wrong parameter length!"); 
        
        byte bytesRead = 0;
        byte[] temp4ByteArray = new byte[4];
        
        // Read "Item" parameter:
        System.arraycopy(contentsBytes, 0, temp4ByteArray, 0, 4);
        item = new String(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        // Read integer values:
        System.arraycopy(contentsBytes, 4, temp4ByteArray, 0, 4);
        offset = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(contentsBytes, 8, temp4ByteArray, 0, 4);
        this.number = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(contentsBytes, 12, temp4ByteArray, 0, 4);
        this.pitch = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        // Check that array was fully read:
        if (contentsBytes.length != bytesRead) throw new RuntimeException("YgFileHeaderContents constructor error: Wrong number of bytes read!"); 
    }// YgFileHeaderContents
        
    
    /**
     * Returns "item" parameter value.
     * 
     * @return "item" parameter value
     */
    public String getItem()
    {
        return item;
    }// getItem
    
    
    /**
     * Returns an offset, starting from which block info can be read.
     * 
     * @return "offset" parameter value
     */
    public long getOffset()
    {
        return offset;
    }// getOffset
    
    
    /**
     * Returns "number" parameter value.
     * 
     * @return "number" parameter value
     */
    public long getNumber()
    {
        return number;
    }// getNumber
}// YgFileHeaderContents
