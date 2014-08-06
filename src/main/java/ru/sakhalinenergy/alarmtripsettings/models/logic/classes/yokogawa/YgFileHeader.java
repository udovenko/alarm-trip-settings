package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Класс описывает структуру полей файла диаграммы функциональных блоков в 
 * бэкапе проекта Yokogawa DCS.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.2
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
     * Конструктор класса. Разбирает полученный массив байт на сегменты и 
     * преобразует их в поля экземпляра согласно структуре файла.
     * 
     * @throws  RuntimeException
     * @param   headerBytes       Массив байт звголовка файла
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
        
        //Читаем имя файла из заголовка:
        System.arraycopy(headerBytes, 0, temp12ByteArray, 0, 12);
        this.fName = new String(temp12ByteArray);
        bytesRead += temp12ByteArray.length;
        
        //Читаем Editor Id:
        System.arraycopy(headerBytes, 12, temp4ByteArray, 0, 4);
        this.editorId = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        //Читаем имя проекта:
        System.arraycopy(headerBytes, 16, temp8ByteArray, 0, 8);
        this.projectName = new String(temp8ByteArray);
        bytesRead += temp8ByteArray.length;
        
        //Читаем имя хоста:
        System.arraycopy(headerBytes, 24, temp8ByteArray, 0, 8);
        this.hostName = temp8ByteArray;
        bytesRead += temp8ByteArray.length;
        
        //Читаем байтовые данные:
        this.createSubRevision  = headerBytes[32];
        this.createRevision     = headerBytes[33];
        this.createVersion      = headerBytes[34];
        this.reservedOne        = headerBytes[35];
        this.modifySubRevision  = headerBytes[36]; 
        this.modifyRevision     = headerBytes[37];  
        this.modifyVersion      = headerBytes[38];
        this.reservedTwo        = headerBytes[39];
        this.fileFormatRevision = headerBytes[40];
        bytesRead += 9;
        
        //Читаем третий зарезервированный участок:
        System.arraycopy(headerBytes, 41, temp3ByteArray, 0, 3);
        this.reservedThree = temp3ByteArray;
        bytesRead += temp3ByteArray.length;
        
        //Читаем целочисленные данные:
        System.arraycopy(headerBytes, 44, temp4ByteArray, 0, 4);
        this.itemNumber = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(headerBytes, 48, temp4ByteArray, 0, 4);
        this.createTime = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(headerBytes, 52, temp4ByteArray, 0, 4);
        this.modifyTime = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        //Читаем тип файла:
        System.arraycopy(headerBytes, 56, temp8ByteArray, 0, 8);
        this.fileType = temp8ByteArray;
        bytesRead += temp8ByteArray.length;
        
        //Читаем оглавление файла:
        YgFileHeaderContents tempContents;
        
        for (int i = 0; i < 28; i++)
        {
            System.arraycopy(headerBytes, 64 + i*16, temp16ByteArray, 0, 16);
            tempContents = new YgFileHeaderContents(temp16ByteArray);
            this.contents[i] = tempContents;
            bytesRead += temp16ByteArray.length;
        }//for
        
        //Проверяем, что прочитан весь массив:
        if (headerBytes.length != bytesRead) throw new RuntimeException("YgFileHeader constructor error: Wrong number of bytes read!"); 
    }//YgFileHeader
       
    
    /**
     * Метод возвращает значение поля fName.
     * 
     * @return  Значение поля fName
     */
    public String getFName()
    {
        return this.fName;
    }//getFName
    
    
    /**
     * Метод возвращает значение поля editorId.
     * 
     * @return  Значение поля editorId
     */
    public long getEditorId()
    {
        return this.editorId;
    }//getEditorId
    
    
    /**
     * Метод находит элемент оглавления заоголовка по его имени.
     * 
     * @param   item  Имя элемента оглавления
     * @return  Экземпляр элемента оглавления, если он был найден, или null
     */
    public YgFileHeaderContents getContentItem(String item)
    {
        for (YgFileHeaderContents tempContents : this.contents)
        {
            if (tempContents.getItem().equals(item)) return tempContents;
        }//for
        
        return null;
    }//getContentItem
}//FunctionalBlockDiagram
