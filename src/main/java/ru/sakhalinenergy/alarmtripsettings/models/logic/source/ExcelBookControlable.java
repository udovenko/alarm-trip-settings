package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.sql.SQLException;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;


/**
 * Интерфейс для управления моделью книги MS Excel контролдлером. Дополняет 
 * интерфейс для представлений доступом к сеттерам дополнительным ключам.
 * 
 * @author Denis.Udovenko
 * @version 1.0.1
 */
public interface ExcelBookControlable extends TagsSourceControllable
{
  
    /**
     * Метод отключает провайдер данных от книги.
     * 
     * @throws SQLException
     */
    public void disconnect() throws SQLException;
        
    
    /**
     * Метод возвращает статус соединения с книгой MS Excel.
     * 
     * @throws SQLException
     * @return true, если соединение открыто, иначе false 
     */
    public boolean isConnected() throws SQLException;

    
    /**
     * Метод создает и запускает нить для подключения провайдера данных модели 
     * к заданной книги MS Excel, а также чтения массива имен листов книги и их 
     * полей. Исключения нити рассылаются подписчикам на исключения модели в 
     * EDT. 
     */
    public void connectAndReadStructure(final String filePath);
    
    
    /**
     * Метод создает и запускает нить для чтения тагов из выбранного листа 
     * подключенной книги MS Excel. Исключения нити рассылаются подписчикам на
     * исключения модели в EDT. 
     * 
     * @param TagMask Маска тага, которая будет использоваться для обработки тагов выбранного листа
     */
    public void readTags(final TagMask tagMask);
    
}//ExcelBookControlable
