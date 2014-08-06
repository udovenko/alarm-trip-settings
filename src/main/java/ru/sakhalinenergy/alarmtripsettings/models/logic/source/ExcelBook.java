package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.Set;
import java.util.List;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.provider.XlsDataProvider;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Класс реализует нить для создания истоника данных путем парсинга данных
 * заданного листа книги MS Excel.
 *
 * @author Denis.Udovenko
 * @version 1.0.9
 */
public class ExcelBook extends TagsSource implements ExcelBookObservable, ExcelBookControlable
{
    private XlsDataProvider dataProvider;
    private HashMap<String, List> sheetsAndFields = new HashMap();
       
    
    /**
     * Конструктор класса.
     * 
     * @param source Экземпляр источника данных, который будет обернут в логику текущего класса
     */
    public ExcelBook(Source source)
    {
        super(source);
    }//ExcelBook
      
    
    /**
     * Метод отключает провайдер данных от книги.
     * 
     * @throws SQLException
     */
    @Override
    public void disconnect() throws SQLException
    {
        dataProvider.disconnect();
    }//disconnect
    
    
    /**
     * Метод возвращает статус соединения с книгой MS Excel.
     * 
     * @throws SQLException
     * @return true, если соединение открыто, иначе false 
     */
    @Override
    public boolean isConnected() throws SQLException
    {
        return dataProvider != null && dataProvider.isConnected();
    }//isConnected
           
    
    /**
     * Метод возвращает текущий путь к книге, если она подключена.
     * 
     * @return Текущий путь к книге или null, если книга не подключена
     */
    @Override
    public String getBookFilePath()
    {
        return (dataProvider != null) ? dataProvider.filePath : null;
    }//getBookFilePath
    
    
    /**
     * Метод возвращает значение поля модели с массивом имен листов книги и их 
     * полей, если таковые были получены.
     * 
     * @return Массивом имен листов книги и их полей
     */
    @Override
    public HashMap getSheetsAndFields()
    {
        return sheetsAndFields;
    }//getSheetsAndFields
    
    
    /**
     * Метод возвращает сущность источника данных, обернутую в текущую логику.
     * 
     * @return Сущность источника данных
     */
    @Override
    public Source getSource()
    {
        return source;
    }//getTags    
    
    
    /**
     * Метод создает и запускает нить для подключения провайдера данных модели 
     * к заданной книги MS Excel, а также чтения массива имен листов книги и их 
     * полей. Исключения нити рассылаются подписчикам на исключения модели в 
     * EDT. 
     */
    @Override
    public void connectAndReadStructure(final String filePath)
    {
        //Создаем анонимную нить для получения списка листов и полей текущей книги MS Excel:
        WorkerThread sheetsAndTablesReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                              
                try
                {    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Connecting to book via ODBC");
                    publish(progress);
                    
                    //Соединяемся с книгой:
                    if (isConnected()) disconnect();
                    if (dataProvider == null) dataProvider = new XlsDataProvider(filePath);
                    dataProvider.connect();
                        
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Reading sheets and fields");
                    publish(progress);
                    
                    List<String> tableNames = dataProvider.getTables();
                    List<String> fields;
            
                    //Собираем списки полей в хэш-массив:
                    for (String tableName : tableNames)
                    {
                        fields = dataProvider.getFields(tableName);
                        sheetsAndFields.put(tableName, fields);
                    }//for
     
                } catch (final Exception exception) { //Рассылаем событие ошибки в EDT:
                     
                    _invokeExceptionInEdt("Error during book connection", exception, WorkerThread.Event.ERROR);
                }//catch
                
                return sheetsAndFields;
            }//doInBackground
        };//sheetsAndTablesReader
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(SourceEvent.THREAD_ERROR) != null) sheetsAndTablesReader.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) sheetsAndTablesReader.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) sheetsAndTablesReader.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.BOOK_CONNECTED) != null) sheetsAndTablesReader.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.BOOK_CONNECTED));

        //Запускаем нить:
        sheetsAndTablesReader.execute();
    }//readSheetsAndFields
    
    
    /**
     * Метод создает и запускает нить для чтения тагов из выбранного листа 
     * подключенной книги MS Excel. Исключения нити рассылаются подписчикам на
     * исключения модели в EDT. 
     * 
     * @param tagMask Маска тага, которая будет использоваться для обработки тагов выбранного листа
     */
    @Override
    public void readTags(final TagMask tagMask)
    {
        // Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                // Publish progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Resetting book format");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 5);
                publish(progress);
                
                // Создаем провайдер данных для копии текущей книги:
                XlsDataProvider copyBookDataProvider = null;
                String copyBookPath = "temp\\resetFormat.xls";
                
                try //Создаем копию книги во временной дирректории и сбрасываем формат всех ячеек копии на текстовый:
                {    
                    dataProvider.createDatabaseCopyWithResetFormat(copyBookPath);
                    copyBookDataProvider = new XlsDataProvider(copyBookPath);
                    copyBookDataProvider.connect();
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Book copy creation / cells format resetting error", exception, WorkerThread.Event.ERROR);
                }//catch 
                
                //Публикуем текущий прогресс:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Executing rows query");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 70);
                publish(progress);
                
                ExcelBookParsingDialogSettings settingsInstatnce = ExcelBookParsingDialogSettings.getInstance();
        
                //Определяем необходимый для набор полей листа:
                Set<String> distinctFields = _selectDistingFields(settingsInstatnce);
                
                //Формируем запрос из полученных полей:
                String query = "SELECT ";
                for (String field : distinctFields)
                {
                    query = query + field + ", ";
                }//for
                query = query.substring(0, query.lastIndexOf(", "));
                query = query + " FROM [" + settingsInstatnce.getSheetName() + "$]";
                
                List<HashMap> tagsRecordset = null;
                int tagsCount = 0;
                
                try //Выполняем запрос и получаем набор тагов:
                {
                    tagsRecordset = copyBookDataProvider.selectQuery(query);
                    tagsCount = tagsRecordset.size();
            
                } catch (Exception exception) { //Рассылаем событие ошибки в EDT:
                     
                    _invokeExceptionInEdt("Tags recordset creation error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                //Публикуем текущий прогресс:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Creating tags");
                publish(progress);

                String tempTagName;
                Tag tempTag;
                TagSetting tempSetting;
                Map<String, Tag> processedTags = new HashMap();
                
                //Обходим все найденные по строке поиска записи перкеметров и пытаемся добавить их к прибору:
                for (int i = 0; i < tagsCount; i++)
                {
                    HashMap row = tagsRecordset.get(i);
                    tempTagName = (String)row.get(settingsInstatnce.getTagNameField());
                    
                    try
                    {
                        //Если таг уже добавлен в хэш-массив, добавляем уставки к нему:
                        if (processedTags.containsKey(tempTagName)) tempTag = processedTags.get(tempTagName);
                        else {
                        
                            //Добавляем в коллекцию новый экземпляр тага: 
                            tempTag = addTag((String)row.get(settingsInstatnce.getTagNameField()));
                        
                            //Сохраняем ссылку на созданный таг с ключом из исходного имени тага:
                            processedTags.put(tempTagName, tempTag);
                        }//else
                        
                        try //Ищем в текущей строке уставку нижнего трипа:
                        {    
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_LL_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmLowLowKeyField(), settingsInstatnce.getAlarmLowLowKeyValue(), 
                                settingsInstatnce.getAlarmLowLowValueField(), settingsInstatnce.getAlarmLowLowPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке уставку нижнего аларма:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_L_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmLowKeyField(), settingsInstatnce.getAlarmLowKeyValue(), 
                                settingsInstatnce.getAlarmLowValueField(), settingsInstatnce.getAlarmLowPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке уставку врерхнего аларма:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_H_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmHighKeyField(), settingsInstatnce.getAlarmHighKeyValue(), 
                                settingsInstatnce.getAlarmHighValueField(), settingsInstatnce.getAlarmHighPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке уставку верхнего трипа:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_HH_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmHighHighKeyField(), settingsInstatnce.getAlarmHighHighKeyValue(), 
                                settingsInstatnce.getAlarmHighHighValueField(), settingsInstatnce.getAlarmHighHighPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке нижнюю границу диапазона:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.RANGE_MIN_SETTING.ID, distinctFields,
                                settingsInstatnce.getRangeMinKeyField(), settingsInstatnce.getRangeMinKeyValue(), 
                                settingsInstatnce.getRangeMinValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке верхнюю границу диапазона:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.RANGE_MAX_SETTING.ID, distinctFields,
                                settingsInstatnce.getRangeMaxKeyField(), settingsInstatnce.getRangeMaxKeyValue(), 
                                settingsInstatnce.getRangeMaxValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке единицы измерения:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.UNITS_SETTING.ID, distinctFields,
                                settingsInstatnce.getUnitsKeyField(), settingsInstatnce.getUnitsKeyValue(), 
                                settingsInstatnce.getUnitsValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try //Ищем в текущей строке систему - источник сигнала:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.SOURCE_SYSTEM_SETTING.ID, distinctFields,
                                settingsInstatnce.getSourceSystemKeyField(), settingsInstatnce.getSourceSystemKeyValue(), 
                                settingsInstatnce.getSourceSystemValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                    } catch (Exception exception) { //Рассылаем событие ошибки в EDT:
                        
                        //Рассылаем исключение и комментарий подписчикам:
                        _invokeExceptionInEdtAndWait("Can't create tag " + tempTagName, exception, WorkerThread.Event.WARNING);
                    }//catch
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)i / (double)tagsCount * 100));
                    publish(progress);
                }//for

                try //Пытаемся отключиться от исходной книги и от копии со сброшенным форматом:
                {
                    dataProvider.disconnect();
                    copyBookDataProvider.disconnect();
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Books disconnecting error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                return new HashMap();
            }//doInBackground
        };//sheetsAndTablesReader
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(SourceEvent.THREAD_ERROR) != null) tagsReader.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) tagsReader.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) tagsReader.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.TAGS_READ) != null) tagsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.TAGS_READ));
                
        tagsReader.execute();
    }//readTags
       
    
    /**
     * Метод отсеивает из значащих полей экземпляра настроек обработки набор
     * неповторяющихся полей для генерации запроса выбранному листу.
     * 
     * @param settingsInstatnce Экземпляр конфига параметров обработки
     * @return Множество полоей для формирования запроса
     */
    private Set<String> _selectDistingFields(ExcelBookParsingDialogSettings settingsInstatnce)
    {
        Set<String> distinctFields = new TreeSet();
        distinctFields.add(settingsInstatnce.getTagNameField());
        
        if (settingsInstatnce.getAlarmLowLowKeyField() != null && !settingsInstatnce.getAlarmLowLowKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowKeyField());
        if (settingsInstatnce.getAlarmLowLowValueField() != null && !settingsInstatnce.getAlarmLowLowValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowValueField());
        if (settingsInstatnce.getAlarmLowLowPossibleFlagField() != null && !settingsInstatnce.getAlarmLowLowPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowPossibleFlagField());
        
        if (settingsInstatnce.getAlarmLowKeyField() != null && !settingsInstatnce.getAlarmLowKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowKeyField());
        if (settingsInstatnce.getAlarmLowValueField() != null && !settingsInstatnce.getAlarmLowValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowValueField());
        if (settingsInstatnce.getAlarmLowPossibleFlagField() != null && !settingsInstatnce.getAlarmLowPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowPossibleFlagField());
        
        if (settingsInstatnce.getAlarmHighKeyField() != null && !settingsInstatnce.getAlarmHighKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighKeyField());
        if (settingsInstatnce.getAlarmHighValueField() != null && !settingsInstatnce.getAlarmHighValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighValueField());
        if (settingsInstatnce.getAlarmHighPossibleFlagField() != null && !settingsInstatnce.getAlarmHighPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighPossibleFlagField());
        
        if (settingsInstatnce.getAlarmHighHighKeyField() != null && !settingsInstatnce.getAlarmHighHighKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighKeyField());
        if (settingsInstatnce.getAlarmHighHighValueField() != null && !settingsInstatnce.getAlarmHighHighValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighValueField());
        if (settingsInstatnce.getAlarmHighHighPossibleFlagField() != null && !settingsInstatnce.getAlarmHighHighPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighPossibleFlagField());
        
        if (settingsInstatnce.getRangeMinKeyField() != null && !settingsInstatnce.getRangeMinKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMinKeyField());
        if (settingsInstatnce.getRangeMinValueField() != null && !settingsInstatnce.getRangeMinValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMinValueField());
        
        if (settingsInstatnce.getRangeMaxKeyField() != null && !settingsInstatnce.getRangeMaxKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMaxKeyField());
        if (settingsInstatnce.getRangeMaxValueField() != null && !settingsInstatnce.getRangeMaxValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMaxValueField());
        
        if (settingsInstatnce.getUnitsKeyField() != null && !settingsInstatnce.getUnitsKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getUnitsKeyField());
        if (settingsInstatnce.getUnitsValueField() != null && !settingsInstatnce.getUnitsValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getUnitsValueField());
        
        if (settingsInstatnce.getSourceSystemKeyField() != null && !settingsInstatnce.getSourceSystemKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getSourceSystemKeyField());
        if (settingsInstatnce.getSourceSystemValueField() != null && !settingsInstatnce.getSourceSystemValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getSourceSystemValueField());
        
        return distinctFields;
    }//_selectDistingFields
    
    
    /**
     * Метод пытается получить настройки тага заданного типа из заданной строки 
     * набора записей листа.
     * 
     * @throws Exception
     * @param tagRow Строка из набора записей листа
     * @param settingType id тип искомой настройки тага
     * @param distinctFields Множество полей, вошедших в звапрос и доступных в строке из набора записей
     * @param keyField Назавание поля, хранящего ключи настроек
     * @param keyValue Значение ключа искомой настройки (например, "SP_HH")
     * @param valueField Название поля, хранящего значение настройки
     * @param possibleFlagField Назавание поля, хранящего флаг "Possible" настройки
     */
    private TagSetting _readTagSetting(HashMap tagRow, int settingType, Set distinctFields, 
        String keyField, String keyValue, String valueField, String possibleFlagField) throws Exception
    {
        TagSetting resultSetting = null;
        TagSettingProperty resultSettingProperty;
        String tempKey;
        
        //Если поле, хранящее ключ уставки нижнего трипа, входит в список отобранных в запрос полей и текущая запись содерджит в этом поле именно ключ нижней уставки:
        if (valueField != null && keyField != null && distinctFields.contains(valueField) && distinctFields.contains(keyField))
        {
            tempKey = (String)tagRow.get(keyField);
            tempKey = tempKey.trim();
            if (tempKey.equals(keyValue))
            { 
                resultSetting = new TagSetting();
                resultSetting.setTypeId(settingType);
                resultSetting.setValue((String)tagRow.get(valueField));
            }//if
                                                    
        } else if (valueField != null && distinctFields.contains(valueField)) { //Если заданно только поле, храняещее величину нижнего трипа, создаем уставку без учета ключа:
            
            resultSetting = new TagSetting();
            resultSetting.setTypeId(settingType);
            resultSetting.setValue((String)tagRow.get(valueField));
        }//else
                        
        //Если настройка была создана, и в настрорйках указано поле, хранящее флаг "Possible", добавляем свойство настройки:
        if (resultSetting != null && possibleFlagField != null && distinctFields.contains(possibleFlagField))
        {
            String possibleFlagValue = (String)tagRow.get(possibleFlagField);
            
            //Если значение флага - не пустая строка:
            if (!possibleFlagValue.trim().equals(""))
            {    
                resultSettingProperty = new TagSettingProperty();
                resultSettingProperty.setTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                resultSettingProperty.setValue((String)tagRow.get(possibleFlagField));
                resultSetting.getProperties().add(resultSettingProperty);
            }//if
        }//if
        
        return resultSetting;
    }//_readTagSetting
}//ExcelBookDataSourceCreator