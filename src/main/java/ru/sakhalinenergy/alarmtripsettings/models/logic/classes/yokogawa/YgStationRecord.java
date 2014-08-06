package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa;


/**
 * Класс описывает запись с информацией о станции в бэкапе DCS Yokogawa.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.4
 */
public class YgStationRecord 
{
    private String stationName;
    private byte[] rest;
    
    
    /**
     * Конструктор класса. Разбирает полученный массив байт на сегменты и 
     * преобразует их в поля экземпляра согласно структуре файла.
     * 
     * @throws  RuntimeException
     * @param   headerBytes       Массив байт элемента описания станции
     */
    public YgStationRecord(byte[] stationBytes)
    {
       if (stationBytes.length != 256) throw new RuntimeException("YgStationRecord constructor error: Wrong parameter lenth!");
       
       int bytesRead = 0;
       byte[] temp8ByteArray = new byte[8];
       byte[] temp248ByteArray = new byte[248];
       
       //Читаем имя станции:
       System.arraycopy(stationBytes, 0, temp8ByteArray, 0, 8);
       this.stationName = new String(temp8ByteArray);
       //System.out.println(this.stationName);
       bytesRead += temp8ByteArray.length;
       
       //Читаем массив оставшихся байтов:
       System.arraycopy(stationBytes, 8, temp248ByteArray, 0, 248);
       this.rest = temp248ByteArray;
       bytesRead += temp248ByteArray.length;
       
       //Проверяем, что прочитан весь массив:
        if (stationBytes.length != bytesRead) throw new RuntimeException("YgStationRecord constructor error: Wrong number of bytes read!"); 
    }//YgStationRecord
    
    
    /**
     * Метод возвращает поле с именем станции.
     * 
     * @return  Имя станции
     */
    public String getStationName()
    {
        return this.stationName;
    }//getStationName
    
    
    /**
     * Метод перегружает родительский метод преобразования объекта в строку.
     * 
     * @return  Имя станции
     */
    @Override
    public String toString()
    {
        return this.stationName.trim();
    }//toString
}//YgStationRecord
