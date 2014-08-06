package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Implements functional block entity in DCS Yokogawa backup.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class YgBlockRecord
{
    private String  tagName;
    private int     controlFlag;
    private int     blockNo;
    private String  instanceName;
    private byte[]  optionId = new byte[4];
    private byte[]  usInsName = new byte[8];
    private byte[]  auxThree = new byte[8];
    
    
    /**
     * Public constructor. Parses given bytes array to segments and translates 
     * them to instance fields according to backup file structure.
     *
     * @throws RuntimeException
     * @param blockBytes Functional block data bytes array
     */
    public YgBlockRecord(byte[] blockBytes)
    {
        if (blockBytes.length != 48) throw new RuntimeException("YgBlockRecord constructor error: Wrong parameter length!");
        
        byte bytesRead = 0;
        byte[] temp2BytesArray = new byte[2];
        byte[] temp4BytesArray = new byte[4];
        byte[] temp8BytesArray = new byte[8];
        byte[] temp16BytesArray = new byte[16];
        
        // Read tag name:
        System.arraycopy(blockBytes, 0, temp16BytesArray, 0, 16);
        tagName = new String(temp16BytesArray);
        bytesRead += temp16BytesArray.length;
        
        // Read "Control Flag" and "Block No" parameters:
        System.arraycopy(blockBytes, 16, temp2BytesArray, 0, 2);
        controlFlag = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        bytesRead += temp2BytesArray.length;
        
        System.arraycopy(blockBytes, 18, temp2BytesArray, 0, 2);
        blockNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        bytesRead += temp2BytesArray.length;
        
        // Read block instance name:
        System.arraycopy(blockBytes, 20, temp8BytesArray, 0, 8);
        instanceName = new String(temp8BytesArray);
        bytesRead += temp8BytesArray.length;
        
        // Read "option id" parameter:
        System.arraycopy(blockBytes, 28, temp4BytesArray, 0, 4);
        optionId = temp4BytesArray;
        bytesRead += temp4BytesArray.length;
        
        // Read "usInsName" parameter:
        System.arraycopy(blockBytes, 32, temp8BytesArray, 0, 8);
        this.usInsName = temp8BytesArray;
        bytesRead += temp8BytesArray.length;
        
        // Read "auxThree" parameter:
        System.arraycopy(blockBytes, 40, temp8BytesArray, 0, 8);
        this.auxThree = temp8BytesArray;
        bytesRead += temp8BytesArray.length;
        
        // Check that array was fully read:
        if (blockBytes.length != bytesRead) throw new RuntimeException("YgBlockRecord constructor error: Wrong number of bytes read!"); 
    }// YgBlockRecord
        
    
    /**
     * Returns tag name parameter value.
     * 
     * @return Tag name parameter value
     */
    public String getTagName()
    {
        return this.tagName;
    }// getTagName
    
    
    /**
     * Returns functional block instance name parameter value.
     * 
     * @return Instance name parameter value
     */
    public String getInstanceName()
    {
        return this.instanceName;              
    }// getInstanceName
}// YgBlockRecord
