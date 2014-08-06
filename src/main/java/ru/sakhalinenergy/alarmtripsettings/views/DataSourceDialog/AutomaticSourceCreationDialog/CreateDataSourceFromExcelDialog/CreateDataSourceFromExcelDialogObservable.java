package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDataSourceFromExcelDialog;

import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.AutomaticSourceCreationDialogObservable;


/**
 * An interface for using "Create source from MS Excel" dialog by controllers.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface CreateDataSourceFromExcelDialogObservable extends AutomaticSourceCreationDialogObservable
{
    
    /**
     * Метод возвращает имя выбранного типа источника данных.
     * 
     * @return Имя выбранного типа источника данных
     */
    public String getDataSourceType();
    
    
    /**
     * Метод возвращает выбранное имя листа книги MS Excel.
     * 
     * @return Выбранное имя листа книги MS Excel
     */
    public String getSheetName();
        
    
    /**
     * Метод возвращает выбранное поле, содержащее имя тага.
     * 
     * @return Поле, содержащее имя тага
     */
    public String getTagFieldName();
        
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки нижнего трипа.
     * 
     * @return Имя поля, хранящего ключ уставки нижнего трипа
     */
    public String getAlarmLowLowKeyField();
    
    
    /**
     * Метод возвращает значение ключа уставки нижнего трипа.
     * 
     * @return Значение ключа уставки нижнего трипа
     */
    public String getAlarmLowLowKeyValue();
    
    
    /**
     * Метод возвращаяет имя поля, хранящего значение уставки нижнего трипа.
     * 
     * @return Имя поля, хранящего значение уставки нижнего трипа
     */
    public String getAlarmLowLowValueField();
    
    
    /**
     * Метод возращает имя поля, хранящего значение флага "Possible" нижнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" нижнего трипа
     */
    public String getAlarmLowLowPossibleFlagField();
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки нижнего аларма.
     * 
     * @return Имя поля, хранящего ключ уставки нижнего аларма
     */
    public String getAlarmLowKeyField();
    
    
    /**
     * Метод возвращает значение ключа уставки нижнего аларма.
     * 
     * @return Значение ключа уставки нижнего аларма
     */
    public String getAlarmLowKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки нижнего аларма.
     * 
     * @return Имя поля, хранящего значение уставки нижнего аларма
     */
    public String getAlarmLowValueField();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" нижнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" нижнего трипа
     */
    public String getAlarmLowPossibleFlagField();
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки верхнего аларма.
     * 
     * @return Имя поля, хранящего ключ уставки верхнего аларма
     */
    public String getAlarmHighKeyField();
    
    
    /**
     * Метод возвращает значение ключа уставки верхнего аларма.
     * 
     * @return Значение ключа уставки верхнего аларма
     */
    public String getAlarmHighKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки верхнего аларма.
     * 
     * @return Имя поля, хранящего значение уставки верхнего аларма
     */
    public String getAlarmHighValueField();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" верхнего аларма.
     * 
     * @return Имя поля, хранящего значение флага "Possible" верхнего аларма
     */
    public String getAlarmHighPossibleFlagField();
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки верхнего трипа.
     * 
     * @return Имя поля, хранящего ключ уставки верхнего трипа
     */
    public String getAlarmHighHighKeyField();
    
    
    /**
     * Метод возвращает значение ключа уставки верхнего трипа.
     * 
     * @return Значение ключа уставки верхнего трипа
     */
    public String getAlarmHighHighKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки верхнего трипа.
     * 
     * @return Имя поля, хранящего значение уставки верхнего трипа
     */
    public String getAlarmHighHighValueField();
            
            
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" верхнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" верхнего трипа
     */
    public String getAlarmHighHighPossibleFlagField();
        
    
    /**
     * Метод возвращает имя поля, хранящего ключ нижней границы диапазона.
     * 
     * @return Имя поля, хранящего ключ нижней границы диапазона
     */
    public String getRangeMinKeyField();
        
    
    /**
     * Метод возвращает значение ключа нижней границы диапазона.
     * 
     * @return Значение ключа нижней границы диапазона
     */
    public String getRangeMinKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение нижней границы диапазона.
     * 
     * @return Имя поля, хранящего значение нижней границы диапазона
     */
    public String getRangeMinValueField();
            
    
    /**
     * Метод возвращает имя поля, хранящего ключ верхней границы диапазона.
     * 
     * @return Имя поля, хранящего ключ верхней границы диапазона
     */
    public String getRangeMaxKeyField();
    
            
    /**
     * Метод возвращает значение ключа верхней границы диапазона.
     * 
     * @return Значение ключа верхней границы диапазона
     */
    public String getRangeMaxKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение верхней границы диапазона.
     * 
     * @return Имя поля, хранящего значение верхней границы диапазона
     */
    public String getRangeMaxValueField();
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ единиц измерения.
     * 
     * @return Имя поля, хранящего ключ единиц измерения
     */
    public String getUnitsKeyField();
    
    
    /**
     * Метод возвращает значение ключа единиц измерения.
     * 
     * @return Значение ключа единиц измерения
     */
    public String getUnitsKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего значение единиц измерения.
     * 
     * @return Имя поля, хранящего значение единиц измерения
     */
    public String getUnitsValueField();
    
    
    /**
     * Метод возвращвает имя поля, хранящего ключ названия системы-источника 
     * данных.
     * 
     * @return Имя поля, хранящего ключ названия системы-источника данных
     */
    public String getSourceSystemKeyField();
        
    
    /**
     * Метод возвращает значение ключа названия системы-источника данных.
     * 
     * @return Значение ключа названия системы-источника данных
     */
    public String getSourceSystemKeyValue();
    
    
    /**
     * Метод возвращает имя поля, хранящего название системы-источника данных.
     * 
     * @return Имя поля, хранящего название системы-источника данных
     */
    public String getSourceSystemValueField();
    
}//CreateDataSourceFromExcelDialogObservable
