package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Интерфейс модели книги MS Excel для использования модели представлениями. 
 * Допускает только использование геттерорв.
 * 
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public interface ExcelBookObservable extends TagsSourceObservable
{
 
    /**
     * Метод возвращает текущий путь к книге, если она подключена.
     * 
     * @return Текущий путь к книге или null, если книга не подключена
     */
    public String getBookFilePath();
        
    
    /**
     * Метод возвращает значение поля модели с массивом имен листов книги и их 
     * полей, если таковые были получены.
     * 
     * @return Массивом имен листов книги и их полей
     */
    public HashMap getSheetsAndFields();
        
    
    /**
     * Метод возвращает сущность источника данных, обернутую в текущую логику.
     * 
     * @return Сущность источника данных
     */
    public Source getSource(); 
}//ExcelBookObservable
