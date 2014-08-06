package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;


/**
 * Класс-обертка для объета с добавлением флага "выбрано". Используется для 
 * отображения объектов в виде чекбоксов в элементах управления.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class CheckboxNode 
{
    private Object object;
    private boolean selected;

    
    /**
     * Конструктор класса. 
     */
    public CheckboxNode(Object object, boolean selected) 
    {
        this.object = object;
        this.selected = selected;
    }//CheckboxNode

    
    /**
     * Метод возвращает значение флага "выбран" текущего экземпляра.
     * 
     * @return Значение флага "выбран"
     */
    public boolean isSelected() 
    {
        return this.selected;
    }//isSelected
    

    /**
     * Метод устанавливает значение флага "выбран" текущего экземпляра.
     * 
     * @param newValue Новое значение флага "выбран"
     * @return void
     */
    public void setSelected(boolean newValue) 
    {
        this.selected = newValue;
    }//setSelected

    
    /**
     * Метод возвращает объект, обернутый в текущий экземпляр.
     * 
     * @return Объект текущего экземпляра
     */
    public Object getObject() 
    {
        return this.object;
    }//getObject

    
    /**
     * Метод устанавливает объект, обернутый в текущий экземпляр.
     * 
     * @param newObject Новый объект текущего экземпляра обертки
     * @return void
     */
    public void setObject(Object newObject) 
    {
        this.object = newObject;
    }//setObject

    
    /**
     * Метод перегружает стандартный метод преобразования объекта в строку.
     * 
     * @return Строковое предстваление текущего экземпляра
     */
    @Override
    public String toString() 
    {
        return getClass().getName() + "[" + this.object + "/" + this.selected + "]";
    }//toString 
}//CheckboxNode
