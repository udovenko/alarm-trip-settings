package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Класс описывает функциональный блок в бэкапке DCS Yokogawa.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
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
     * Конструктор класса. Разбирает полученный массив байт на сегменты и 
     * преобразует их в поля экземпляра согласно структуре файла.
     * 
     * @throws  RuntimeException
     * @param   blockBytes        Массив байт описания функционального блока
     */
    public YgBlockRecord(byte[] blockBytes)
    {
        if (blockBytes.length != 48) throw new RuntimeException("YgBlockRecord constructor error: Wrong parameter length!");
        
        byte bytesRead = 0;
        byte[] temp2BytesArray = new byte[2];
        byte[] temp4BytesArray = new byte[4];
        byte[] temp8BytesArray = new byte[8];
        byte[] temp16BytesArray = new byte[16];
        
        //Читаем имя тага:
        System.arraycopy(blockBytes, 0, temp16BytesArray, 0, 16);
        this.tagName = new String(temp16BytesArray);
        bytesRead += temp16BytesArray.length;
        
        //Читаем параметры Control Flag и Block No:
        System.arraycopy(blockBytes, 16, temp2BytesArray, 0, 2);
        this.controlFlag = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        bytesRead += temp2BytesArray.length;
        
        System.arraycopy(blockBytes, 18, temp2BytesArray, 0, 2);
        this.blockNo = DataTypesUtils.readBytesAsUnsignedShort(temp2BytesArray);
        bytesRead += temp2BytesArray.length;
        
        //Читаем имя инстанса:
        System.arraycopy(blockBytes, 20, temp8BytesArray, 0, 8);
        this.instanceName = new String(temp8BytesArray);
        bytesRead += temp8BytesArray.length;
        
        //Читаем параметр Option Id:
        System.arraycopy(blockBytes, 28, temp4BytesArray, 0, 4);
        this.optionId = temp4BytesArray;
        bytesRead += temp4BytesArray.length;
        
        //Читаем параметр usInsName:
        System.arraycopy(blockBytes, 32, temp8BytesArray, 0, 8);
        this.usInsName = temp8BytesArray;
        bytesRead += temp8BytesArray.length;
        
        //Читаем параметр auxThree:
        System.arraycopy(blockBytes, 40, temp8BytesArray, 0, 8);
        this.auxThree = temp8BytesArray;
        bytesRead += temp8BytesArray.length;
        
        //Проверяем, что прочитан весь массив:
        if (blockBytes.length != bytesRead) throw new RuntimeException("YgBlockRecord constructor error: Wrong number of bytes read!"); 
    }//YgBlockRecord
        
    
    /**
     * Метод возвращает значение поля с именем тага.
     * 
     * @return  Значение поля с именем тага
     */
    public String getTagName()
    {
        return this.tagName;
    }//getTagName
    
    
    /**
     * 
     * 
     */
    public String getInstanceName()
    {
        return this.instanceName;              
    }//getInstanceName
}//Block
