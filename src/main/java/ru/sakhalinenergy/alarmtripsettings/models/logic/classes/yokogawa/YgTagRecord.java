package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Класс описывает структуру записи тага в файле функционального блока бэкапа 
 * DCS Yokogawa.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
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
     * 128 байт
     * 
     */
    public YgTagRecord(byte[] record)
    {
        if (record.length != 128) throw new RuntimeException("Yokogawa Tag Record constructor: Wrong parameter length!");
        
        byte[] temp2BytesArray = new byte[2];
        byte[] temp4BytesArray = new byte[4];
        byte[] temp16BytesArray = new byte[16];
                
        //Получаем имя тага:
        System.arraycopy(record, 0, temp16BytesArray, 0, 16);
        this.tagName = new String(temp16BytesArray);
        
        //Получаем комментарий к тагу:
        byte[] tagCmntBytes = new byte[24];
        System.arraycopy(record, 16, tagCmntBytes, 0, 24);
        this.tagComent = new String(tagCmntBytes);
        
        //Получаем значение поля upperPanel:
        System.arraycopy(record, 40, temp16BytesArray, 0, 16);
        this.upperPanel = new String(temp16BytesArray);        
        
        //Получаем значение байтовых полей:
        this.alarmType = record[56];
        this.accessLevel = record[57];
        this.elemTypeOne = record[58];
        this.elemTypeTwo = record[59];
        
        //Получаем значение поля elemNo:
        System.arraycopy(record, 60, temp2BytesArray, 0, 2);
        this.elemNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        //Получаем значение helpNo:
        System.arraycopy(record, 62, temp2BytesArray, 0, 2);
        this.helpNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        //Получаем значение instNo:
        System.arraycopy(record, 64, temp2BytesArray, 0, 2);
        this.instNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        //Получаем значение plantNo:
        System.arraycopy(record, 66, temp2BytesArray, 0, 2);
        this.plantNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
               
        //Получаем значение поля auxPointer:
        System.arraycopy(record, 68, temp4BytesArray, 0, 4);
        this.auxPointer = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);
        
        //Получаем значение байтовых полей:
        this.instType      = record[72];
        this.auxCodeOne    = record[73];
        this.dataDisp      = record[74];      
        this.auxCodeTwo    = record[75];
        this.instMark      = record[76];
        this.tagMark       = record[77];
        this.dp            = record[78];
        this.tp            = record[79];
        this.rp            = record[80];
        this.mvdp          = record[81];
        this.ocMark        = record[82];
        this.divideNo      = record[83];
        this.labelCode     = record[84];
        this.userCodeOne   = record[85];
        this.userCodeTwo   = record[86];
        this.userCodeThree = record[87];
        
        //Получаем значение поля mvUnit:
        System.arraycopy(record, 88, temp2BytesArray, 0, 2);
        this.mvUnitsIndex = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        //Получаем индекс единиц измерения:
        System.arraycopy(record, 90, temp2BytesArray, 0, 2);
        this.engUnitsIndex = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        
        //Получаем ваерхнюю границу измеряемого диапазона:
        System.arraycopy(record, 92, temp4BytesArray, 0, 4);
        this.sh = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);  
        
        //Получаем ваерхнюю границу измеряемого диапазона:
        System.arraycopy(record, 96, temp4BytesArray, 0, 4);
        this.sl = DataTypesUtils.readAsUnsignedLong(temp4BytesArray);  
    }//YgTagRecord
    
    
    /**
     * 
     * 
     */
    public String getTagName()
    {
        return this.tagName;
    }//getTagName
    
    
    /**
     * 
     * 
     */
    public byte getDp()
    {
        return this.dp;
    }//getDp
    
    
    /**
     * 
     * 
     */
    public int getMvUnits()
    {
        return this.mvUnitsIndex;
    }//getMvUnits
    
    
    /**
     * 
     * 
     */
    public int getEngUnitsIndex()
    {
        return this.engUnitsIndex;
    }//getEngUnitsIndex
    
    
    /**
     * 
     * 
     */
    public long getSh()
    {
        return this.sh;
    }//getSh
    
    
    /**
     * 
     * 
     */
    public long getSl()
    {
        return this.sl;
    }//getSl
}//YgTag
