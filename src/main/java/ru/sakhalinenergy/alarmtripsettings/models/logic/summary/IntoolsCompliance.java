package ru.sakhalinenergy.alarmtripsettings.models.logic.summary;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * Класс рассчитывает итоги для отрисовки чаpтов соответвия уставок лупов 
 * выбранного объекта из базы данных SPI.
 * 
 * @author   Udovenko
 * @version  1.0.4
 */
public class IntoolsCompliance extends Model
{
    
    public static enum Event
    {
        SUMMARY_CALCULATED
    }// Event
    
    
    private final static Double FULL_CONFORMITY_MIN = 99.0;
    private final static Double SEMI_CONFORMITY_MIN = 20.0;
    
    private final static Byte FULLY_CONFIRMED = 1;
    private final static Byte CONFIRMED_AGAINST_DOCUMENTS = 2;
    private final static Byte CONFIRMED_AGAINST_SYSTEMS = 3;
    private final static Byte NOT_CONFIRMED = 4;

    private int totalAlarmsCompliantWithDocumentsAndSystems;
    private int totalAlarmsCompliantWithDocuments;
    private int totalAlarmsCompliantWithSystems;
    private int totalAlarmsNonCompliant;
    
    private int alarmsLowLowCompliantWithDocumentsAndSystems;
    private int alarmsLowLowCompliantWithDocuments;
    private int alarmsLowLowCompliantWithSystems;
    private int alarmsLowLowNonCompliant;
        
    private int alarmsLowCompliantWithDocumentsAndSystems;
    private int alarmsLowCompliantWithDocuments;
    private int alarmsLowCompliantWithSystems;
    private int alarmsLowNonCompliant;
        
    private int alarmsHighCompliantWithDocumentsAndSystems;
    private int alarmsHighCompliantWithDocuments;
    private int alarmsHighCompliantWithSystems;
    private int alarmsHighNonCompliant;
      
    private int alarmsHighHighCompliantWithDocumentsAndSystems;
    private int alarmsHighHighCompliantWithDocuments;
    private int alarmsHighHighCompliantWithSystems;
    private int alarmsHighHighNonCompliant;
    
    private final LoopsTableObservable loopsTable;
    
    
    /**
     * Конструктор класса. 
     */
    public IntoolsCompliance(LoopsTableObservable loopsTable)
    {
        this.loopsTable = loopsTable;
        this.loopsTable.on(CollectionEvent.LOOPS_READ, new _LoopsTableUpdateEventHandler());
    }// SummaryCalculationLogic
    
    
    /**
     * Метод рассчитывает итоговый анализ уставок на основании коефициентов
     * соответвия алармов каждого устройства.
     * 
     * @return void 
     */
     private class _LoopsTableUpdateEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Byte tempComplianceStatus;

            totalAlarmsCompliantWithDocumentsAndSystems = 0;
            totalAlarmsCompliantWithDocuments = 0;
            totalAlarmsCompliantWithSystems = 0;
            totalAlarmsNonCompliant = 0;

            alarmsLowLowCompliantWithDocumentsAndSystems = 0;
            alarmsLowLowCompliantWithDocuments = 0;
            alarmsLowLowCompliantWithSystems = 0;
            alarmsLowLowNonCompliant = 0;

            alarmsLowCompliantWithDocumentsAndSystems = 0;
            alarmsLowCompliantWithDocuments = 0;
            alarmsLowCompliantWithSystems = 0;
            alarmsLowNonCompliant = 0;

            alarmsHighCompliantWithDocumentsAndSystems = 0;
            alarmsHighCompliantWithDocuments = 0;
            alarmsHighCompliantWithSystems = 0;
            alarmsHighNonCompliant = 0;

            alarmsHighHighCompliantWithDocumentsAndSystems = 0;
            alarmsHighHighCompliantWithDocuments = 0;
            alarmsHighHighCompliantWithSystems = 0;
            alarmsHighHighNonCompliant = 0;

            //Обходими всю коллекцию лупов:
            for (SettingsSelector tempLoop : loopsTable.getWrappedLoops())
            {
                //Получаем соотношение наижних трипов прибора:
                if (_hasAlarmLowLow(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsLowLowComplanceType(tempLoop);
                    if (tempComplianceStatus == FULLY_CONFIRMED) alarmsLowLowCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_DOCUMENTS) alarmsLowLowCompliantWithDocuments++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_SYSTEMS) alarmsLowLowCompliantWithSystems++;
                    if (tempComplianceStatus == NOT_CONFIRMED) alarmsLowLowNonCompliant++;
                }//if

                //Получаем соотношение наижних трипов прибора:
                if (_hasAlarmLow(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsLowComplanceType(tempLoop);
                    if (tempComplianceStatus == FULLY_CONFIRMED) alarmsLowCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_DOCUMENTS) alarmsLowCompliantWithDocuments++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_SYSTEMS) alarmsLowCompliantWithSystems++;
                    if (tempComplianceStatus == NOT_CONFIRMED) alarmsLowNonCompliant++;
                }//if

                //Получаем соотношение верхних алармов прибора:
                if (_hasAlarmHigh(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsHighComplanceType(tempLoop);
                    if (tempComplianceStatus == FULLY_CONFIRMED) alarmsHighCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_DOCUMENTS) alarmsHighCompliantWithDocuments++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_SYSTEMS) alarmsHighCompliantWithSystems++;
                    if (tempComplianceStatus == NOT_CONFIRMED) alarmsHighNonCompliant++;
                }//if

                //Получаем соотношение верхних трипов прибора:
                if (_hasAlarmHighHigh(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsHighHighComplanceType(tempLoop);
                    if (tempComplianceStatus == FULLY_CONFIRMED) alarmsHighHighCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_DOCUMENTS) alarmsHighHighCompliantWithDocuments++;
                    if (tempComplianceStatus == CONFIRMED_AGAINST_SYSTEMS) alarmsHighHighCompliantWithSystems++;
                    if (tempComplianceStatus == NOT_CONFIRMED) alarmsHighHighNonCompliant++;
                }//if
            }//for

            //Рассчитываем суммарные показатели:
            totalAlarmsCompliantWithDocumentsAndSystems = alarmsLowLowCompliantWithDocumentsAndSystems + alarmsLowCompliantWithDocumentsAndSystems 
                + alarmsHighCompliantWithDocumentsAndSystems + alarmsHighHighCompliantWithDocumentsAndSystems;
            totalAlarmsCompliantWithDocuments = alarmsLowLowCompliantWithDocuments + alarmsLowCompliantWithDocuments 
                + alarmsHighCompliantWithDocuments + alarmsHighHighCompliantWithDocuments;
            totalAlarmsCompliantWithSystems = alarmsLowLowCompliantWithSystems + alarmsLowCompliantWithSystems 
                + alarmsHighCompliantWithSystems + alarmsHighHighCompliantWithSystems;
            totalAlarmsNonCompliant = alarmsLowLowNonCompliant + alarmsLowNonCompliant
                + alarmsHighNonCompliant + alarmsHighHighNonCompliant;
                
            // Trigger an event which determines that summary calculation is complete:
            CustomEvent summaryCalculatedEvent = new CustomEvent(new Object());
            events.trigger(Event.SUMMARY_CALCULATED, summaryCalculatedEvent);
        }// customEventOccurred
    }// _LoopsTableUpdateEventHandler
     
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного нижнего трипа из 
     * любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmLowLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmLL() != null 
            || loop.getChosenDocumentsAlarmLL() != null 
            || loop.getChosenSystemsAlarmLL() != null);
    }// _hasAlarmLowLow
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного нижнего аларма 
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmH() != null 
            || loop.getChosenDocumentsAlarmH() != null 
            || loop.getChosenSystemsAlarmH() != null);
    }// _hasAlarmHigh
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного верхнего аларма
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmHighHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmHH() != null 
            || loop.getChosenDocumentsAlarmHH() != null 
            || loop.getChosenSystemsAlarmHH() != null);
    }// _hasAlarmHighHigh
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного верхнего трипа 
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmL() != null 
            || loop.getChosenDocumentsAlarmL() != null 
            || loop.getChosenSystemsAlarmL() != null);
    }// _hasAlarmLowLow
    
    
    /**
     * Метод определяет статус соответствия уставки нижнего трипа из SPI 
     * уставкам из других источников.
     * 
     * @param loop Экземпляр контура, для которого определяем статус соответсвия нижнего трипа
     * @return Статус сответсвия нижнего трипа
     */
    private static Byte _getLoopAlarmsLowLowComplanceType(SettingsSelector loop)
    {
        //Если уставки нет или ее уровень подтвержденности нулевой, возвращаем "уставка не подтвеждена":
        if (loop.getChosenIntoolsAlarmLL() == null 
            || loop.getIntoolsAlarmLLConformity() <= SEMI_CONFORMITY_MIN) 
                return NOT_CONFIRMED;
   
        //Если уставка имеет полный показатель совпадения, значит она подтверждена и документами, и SPI:
        if (loop.getIntoolsAlarmLLConformity() > FULL_CONFORMITY_MIN) return FULLY_CONFIRMED;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и документ, то она подтверждена относительно документа:
        if (loop.getChosenDocumentsAlarmLL() != null 
            && loop.getIntoolsAlarmLLConformity() > SEMI_CONFORMITY_MIN
            && loop.getDocumentsAlarmLLConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_DOCUMENTS;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и уставка из систем, то она подтверждена относительно систем:
        if (loop.getChosenSystemsAlarmLL() != null 
            && loop.getIntoolsAlarmLLConformity() > SEMI_CONFORMITY_MIN
            && loop.getSystemsAlarmLLConformity() > SEMI_CONFORMITY_MIN)
                return CONFIRMED_AGAINST_SYSTEMS;
    
        return NOT_CONFIRMED;
    }//_getLoopAlarmsLowLowComplanceType
    
    
    /**
     * Метод определяет статус соответствия уставки нижнего аларма из SPI 
     * уставкам из других источников.
     * 
     * @param loop Экземпляр контура, для которого определяем статус соответсвия нижнего трипа
     * @return Статус сответсвия нижнего трипа
     */
    private static Byte _getLoopAlarmsLowComplanceType(SettingsSelector loop)
    {
        //Если уставки нет или ее уровень подтвержденности нулевой, возвращаем "уставка не подтвеждена":
        if (loop.getChosenIntoolsAlarmL() == null 
            || loop.getIntoolsAlarmLConformity() <= SEMI_CONFORMITY_MIN) 
                return NOT_CONFIRMED;
   
        //Если уставка имеет полный показатель совпадения, значит она подтверждена и документами, и SPI:
        if (loop.getIntoolsAlarmLConformity() > FULL_CONFORMITY_MIN) return FULLY_CONFIRMED;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и документ, то она подтверждена относительно документа:
        if (loop.getChosenDocumentsAlarmL() != null 
            && loop.getIntoolsAlarmLConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmLConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_DOCUMENTS;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и уставка из систем, то она подтверждена относительно систем:
        if (loop.getChosenSystemsAlarmL() != null 
            && loop.getIntoolsAlarmLConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmLConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_SYSTEMS;
    
        return NOT_CONFIRMED;
    }// _getLoopAlarmsLowComplanceType
    
    
    /**
     * Метод определяет статус соответствия уставки верхнего аларма из SPI 
     * уставкам из других источников.
     * 
     * @param loop Экземпляр контура, для которого определяем статус соответсвия нижнего трипа
     * @return Статус сответсвия нижнего трипа
     */
    private static Byte _getLoopAlarmsHighComplanceType(SettingsSelector loop)
    {
        //Если уставки нет или ее уровень подтвержденности нулевой, возвращаем "уставка не подтвеждена":
        if (loop.getChosenIntoolsAlarmH() == null 
            || loop.getIntoolsAlarmHConformity() <= SEMI_CONFORMITY_MIN) 
                return NOT_CONFIRMED;
   
        //Если уставка имеет полный показатель совпадения, значит она подтверждена и документами, и SPI:
        if (loop.getIntoolsAlarmHConformity() > FULL_CONFORMITY_MIN) return FULLY_CONFIRMED;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и документ, то она подтверждена относительно документа:
        if (loop.getChosenDocumentsAlarmH() != null 
            && loop.getIntoolsAlarmHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmHConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_DOCUMENTS;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и уставка из систем, то она подтверждена относительно систем:
        if (loop.getChosenSystemsAlarmH() != null 
            && loop.getIntoolsAlarmHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmHConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_SYSTEMS;
    
        return NOT_CONFIRMED;
    }// _getLoopAlarmsHighComplanceType
    
    
    /**
     * Метод определяет статус соответствия уставки верхнего трипа из SPI 
     * уставкам из других источников.
     * 
     * @param loop Экземпляр контура, для которого определяем статус соответсвия нижнего трипа
     * @return Статус сответсвия нижнего трипа
     */
    private static Byte _getLoopAlarmsHighHighComplanceType(SettingsSelector loop)
    {
        //Если уставки нет или ее уровень подтвержденности нулевой, возвращаем "уставка не подтвеждена":
        if (loop.getChosenIntoolsAlarmHH() == null
            || loop.getIntoolsAlarmHHConformity() <= SEMI_CONFORMITY_MIN) 
                return NOT_CONFIRMED;
   
        //Если уставка имеет полный показатель совпадения, значит она подтверждена и документами, и SPI:
        if (loop.getIntoolsAlarmHHConformity() > FULL_CONFORMITY_MIN) return FULLY_CONFIRMED;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и документ, то она подтверждена относительно документа:
        if (loop.getChosenDocumentsAlarmHH() != null 
            && loop.getIntoolsAlarmHHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmHHConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_DOCUMENTS;
        
        //Если уставка имеет половину показатаеля подтвержденности, также, как и уставка из систем, то она подтверждена относительно систем:
        if (loop.getChosenSystemsAlarmHH() != null 
            && loop.getIntoolsAlarmHHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmHHConformity() > SEMI_CONFORMITY_MIN) 
                return CONFIRMED_AGAINST_SYSTEMS;
    
        return NOT_CONFIRMED;
    }// _getLoopAlarmsHighHighComplanceType
    

    /**
     * Метод возвращает рассчитанное общее количество алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов и систем
     */
    public int getTotalAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.totalAlarmsCompliantWithDocumentsAndSystems;
    }//getTotalAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Метод возвращает рассчитанное общее количество алармов, подтвержденных
     * данными из документов.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов
     */
    public int getTotalAlarmsCompliantWithDocuments()
    {
        return this.totalAlarmsCompliantWithDocuments;
    }//getTotalAlarmsCompliantWithDocuments
    
    
    /**
     * Метод возвращает рассчитанное общее количество алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из систем
     */
    public int getTotalAlarmsCompliantWithSystems()
    {
        return this.totalAlarmsCompliantWithSystems;
    }//getTotalAlarmsCompliantWithSystems
    
    
    /**
     * Метод возвращает рассчитанное общее количество неподтвержденных алармов.
     * 
     * @return Oбщее количество неподтвержденных алармов
     */
    public int getTotalNonCompliantAlarms()
    {
        return this.totalAlarmsNonCompliant;
    }//getTotalNonCompliantAlarms
    
    
    /**
     * Метод возвращает рассчитанное количество LL алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов и систем
     */
    public int getLowLowAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsLowLowCompliantWithDocumentsAndSystems;
    }//getLowLowAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Метод возвращает рассчитанное количество LL алармов, подтвержденных
     * данными из документов.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов
     */
    public int getLowLowAlarmsCompliantWithDocuments()
    {
        return this.alarmsLowLowCompliantWithDocuments;
    }//getLowLowAlarmsCompliantWithDocuments
    
    
    /**
     * Метод возвращает рассчитанное количество LL алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из систем
     */
    public int getLowLowAlarmsCompliantWithSystems()
    {
        return this.alarmsLowLowCompliantWithSystems;
    }//getLowLowAlarmsCompliantWithSystems
    
    
    /**
     * Метод возвращает рассчитанное количество неподтвержденных LL алармов.
     * 
     * @return Oбщее количество неподтвержденных алармов
     */
    public int getLowLowNonCompliantAlarms()
    {
        return this.alarmsLowLowNonCompliant;
    }//getLowLowNonCompliantAlarms
    
    
    /**
     * Метод возвращает рассчитанное количество L алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов и систем
     */
    public int getLowAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsLowCompliantWithDocumentsAndSystems;
    }//getLowAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Метод возвращает рассчитанное количество L алармов, подтвержденных
     * данными из документов.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов
     */
    public int getLowAlarmsCompliantWithDocuments()
    {
        return this.alarmsLowCompliantWithDocuments;
    }//getLowAlarmsCompliantWithDocuments
    
    
    /**
     * Метод возвращает рассчитанное количество L алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из систем
     */
    public int getLowAlarmsCompliantWithSystems()
    {
        return this.alarmsLowCompliantWithSystems;
    }//getLowAlarmsCompliantWithSystems
    
    
    /**
     * Метод возвращает рассчитанное количество неподтвержденных L алармов.
     * 
     * @return Oбщее количество неподтвержденных алармов
     */
    public int getLowNonCompliantAlarms()
    {
        return this.alarmsLowNonCompliant;
    }//getLowNonCompliantAlarms
    
    
    /**
     * Метод возвращает рассчитанное количество H алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов и систем
     */
    public int getHighAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsHighCompliantWithDocumentsAndSystems;
    }//getHighAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Метод возвращает рассчитанное количество H алармов, подтвержденных
     * данными из документов.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов
     */
    public int getHighAlarmsCompliantWithDocuments()
    {
        return this.alarmsHighCompliantWithDocuments;
    }//getHighAlarmsCompliantWithDocuments
    
    
    /**
     * Метод возвращает рассчитанное количество H алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из систем
     */
    public int getHighAlarmsCompliantWithSystems()
    {
        return this.alarmsHighCompliantWithSystems;
    }//getHighAlarmsCompliantWithSystems
    
    
    /**
     * Метод возвращает рассчитанное количество неподтвержденных H алармов.
     * 
     * @return Oбщее количество неподтвержденных алармов
     */
    public int getHighNonCompliantAlarms()
    {
        return this.alarmsHighNonCompliant;
    }//getHighNonCompliantAlarms
    
    
    /**
     * Метод возвращает рассчитанное количество HH алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов и систем
     */
    public int getHighHighAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsHighHighCompliantWithDocumentsAndSystems;
    }//getHighHighAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Метод возвращает рассчитанное количество HH алармов, подтвержденных
     * данными из документов.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из документов
     */
    public int getHighHighAlarmsCompliantWithDocuments()
    {
        return this.alarmsHighHighCompliantWithDocuments;
    }//getHighHighAlarmsCompliantWithDocuments
    
    
    /**
     * Метод возвращает рассчитанное количество HH алармов, подтвержденных
     * данными из документов и систем.
     * 
     * @return Oбщее количество алармов, подтвержденных данными из систем
     */
    public int getHighHighAlarmsCompliantWithSystems()
    {
        return this.alarmsHighHighCompliantWithSystems;
    }//getHighHighAlarmsCompliantWithSystems
    
    
    /**
     * Метод возвращает рассчитанное количество неподтвержденных HH алармов.
     * 
     * @return Oбщее количество неподтвержденных алармов
     */
    public int getHighHighNonCompliantAlarms()
    {
        return this.alarmsHighHighNonCompliant;
    }//getHighHighNonCompliantAlarms
}//IntoolsComplianceSummary
