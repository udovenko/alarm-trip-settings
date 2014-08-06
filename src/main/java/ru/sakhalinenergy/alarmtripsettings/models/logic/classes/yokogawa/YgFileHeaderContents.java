package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;

import ru.sakhalinenergy.alarmtripsettings.util.DataTypesUtils;


/**
 * Клоасс описывает элемент оглавления заголовка файла диаграммы функциональных 
 * блоков.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class YgFileHeaderContents 
{
    private String  item;
    private long    offset;
    private long    number;
    private long    pitch;
    
    
    /**
     * Конструктор класса. Разбирает полученный массив байт на сегменты и 
     * преобразует их в поля экземпляра согласно структуре файла.
     * 
     * @throws  RuntimeException
     * @param   headerBytes       Массив байт элемента оглавления файла
     */
    public YgFileHeaderContents(byte[] contentsBytes)
    {
        //Проверяем корректность длины параметра:
        if (contentsBytes.length != 16) throw new RuntimeException("YgFileHeaderContents constructor error: Wrong parameter length!"); 
        
        byte bytesRead = 0;
        byte[] temp4ByteArray = new byte[4];
        
        //Читаем значение поля Item:
        System.arraycopy(contentsBytes, 0, temp4ByteArray, 0, 4);
        this.item = new String(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        //Читаем целочисленные значения:
        System.arraycopy(contentsBytes, 4, temp4ByteArray, 0, 4);
        this.offset = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(contentsBytes, 8, temp4ByteArray, 0, 4);
        this.number = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        System.arraycopy(contentsBytes, 12, temp4ByteArray, 0, 4);
        this.pitch = DataTypesUtils.readAsUnsignedLong(temp4ByteArray);
        bytesRead += temp4ByteArray.length;
        
        //Проверяем, что прочитан весь массив:
        if (contentsBytes.length != bytesRead) throw new RuntimeException("YgFileHeaderContents constructor error: Wrong number of bytes read!"); 
    }//YgFileHeaderContents
        
    
    /**
     * Метод возвращает значение имени пункта оглавления.
     * 
     * @return  Имя пункта оглавления
     */
    public String getItem()
    {
        return this.item;
    }//getItem
    
    
    /**
     * Метод возвращает отступ, начиная с которого нужно читать информацию о
     * блоках.
     * 
     * @return  Отступ, начиная с которого нужно читать информацию о блоках.
     */
    public long getOffset()
    {
        return this.offset;
    }//getOffset
    
    
    /**
     * Метод возвращзает значение параметра number.
     * 
     * @return  Значение параметра number
     */
    public long getNumber()
    {
        return this.number;
    }//getNumber
}//OdtContents
