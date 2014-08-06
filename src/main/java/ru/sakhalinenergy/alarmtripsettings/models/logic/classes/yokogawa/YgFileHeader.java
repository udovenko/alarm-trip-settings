package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Implements Yokogawa DCS backup file header structure.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class YgFileHeader 
{
    private String  fName;
    private long    editorId;
    private String  projectName;
    private byte[]  hostName = new byte[8];
    private byte    createSubRevision;
    private byte    createRevision;
    private byte    createVersion;  
    private byte    reservedOne;
    private byte    modifySubRevision;
    private byte    modifyRevision;
    private byte    modifyVersion;      
    private byte    reservedTwo;
    private byte    fileFormatRevision;
    private byte[]  reservedThree = new byte[3];
    private long    itemNumber;
    private long    createTime;
    private long    modifyTime;
    private byte[]  fileType = new byte[8];
    private YgFileHeaderContents[] contents = new YgFileHeaderContents[28];
    
    
    /**
     * Public constructor. Parses given bytes array to segments and translates 
     * them to instance fields according to backup file header structure.
     * 
     * @throws RuntimeException
     * @param headerBytes File header data bytes array
     */
    public YgFileHeader(byte[] headerBytes)
    {
        if (headerBytes.length != 512) throw new RuntimeException("YgFileHeader constructor error: Wrong parameter length!");
        
        int bytesRead = 0;
        byte[] temp2ByteArray = new byte[2];
        byte[] temp3ByteArray = new byte[3];
        byte[] temp4ByteArray = new byte[4];
        byte[] temp8ByteArray = new byte[8];
        byte[] temp12ByteArray = new byte[12];
        byte[] temp16ByteArray = new byte[16];
        
        // Read file name from header:
        System.arraycopy(headerBytes, 0, temp12ByteArray, 0, 12);
        fName = new String(temp12ByteArray);
        bytesRead += temp12ByteArray.length;
        
        // Read "Editor Id" param:
        System.arraycopy(headerBytes, 12, temp4ByteArray, 0, 4);
        editorId = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        // Read project name:
        System.arraycopy(headerBytes, 16, temp8ByteArray, 0, 8);
        projectName = new String(temp8ByteArray);
        bytesRead += temp8ByteArray.length;
        
        // Read host name:
        System.arraycopy(headerBytes, 24, temp8ByteArray, 0, 8);
        hostName = temp8ByteArray;
        bytesRead += temp8ByteArray.length;
        
        // Read byte data:
        createSubRevision  = headerBytes[32];
        createRevision     = headerBytes[33];
        createVersion      = headerBytes[34];
        reservedOne        = headerBytes[35];
        modifySubRevision  = headerBytes[36]; 
        modifyRevision     = headerBytes[37];  
        modifyVersion      = headerBytes[38];
        reservedTwo        = headerBytes[39];
        fileFormatRevision = headerBytes[40];
        bytesRead += 9;
        
        // Read third reserved sector:
        System.arraycopy(headerBytes, 41, temp3ByteArray, 0, 3);
        reservedThree = temp3ByteArray;
        bytesRead += temp3ByteArray.length;
        
        // Read integer data:
        System.arraycopy(headerBytes, 44, temp4ByteArray, 0, 4);
        itemNumber = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(headerBytes, 48, temp4ByteArray, 0, 4);
        createTime = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(headerBytes, 52, temp4ByteArray, 0, 4);
        modifyTime = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        // Read file type:
        System.arraycopy(headerBytes, 56, temp8ByteArray, 0, 8);
        fileType = temp8ByteArray;
        bytesRead += temp8ByteArray.length;
        
        // Read file contents:
        YgFileHeaderContents tempContents;
        
        for (int i = 0; i < 28; i++)
        {
            System.arraycopy(headerBytes, 64 + i*16, temp16ByteArray, 0, 16);
            tempContents = new YgFileHeaderContents(temp16ByteArray);
            contents[i] = tempContents;
            bytesRead += temp16ByteArray.length;
        }//for
        
        // Check that array was fully read:
        if (headerBytes.length != bytesRead) throw new RuntimeException("YgFileHeader constructor error: Wrong number of bytes read!"); 
    }// YgFileHeader
       
    
    /**
     * Returns "fName" parameter value.
     * 
     * @return "fName" parameter value
     */
    public String getFName()
    {
        return fName;
    }// getFName
    
    
    /**
     * Returns "editorId" parameter value.
     * 
     * @return "editorId" parameter value
     */
    public long getEditorId()
    {
        return editorId;
    }// getEditorId
    
    
    /**
     * Finds header contents element by name.
     * 
     * @param item Contents element name
     * @return Contents element instance or null if nothing was found
     */
    public YgFileHeaderContents getContentItem(String item)
    {
        for (YgFileHeaderContents tempContents : this.contents)
        {
            if (tempContents.getItem().equals(item)) return tempContents;
        }// for
        
        return null;
    }// getContentItem
}// YgFileHeader
