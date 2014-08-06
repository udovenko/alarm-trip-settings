package ru.sakhalinenergy.alarmtripsettings.models.logic.settings;


/**
 * Класс реализует базового потомка для всех классов логики операций с устваками
 * притборов, таких как выбор уставок из множества тагов, реализация отчетов
 * и т.д.
 * 
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public abstract class Comparator 
{
    protected static final byte COMPARED_AS_DOUBLE_EQUALS     = 1;
    protected static final byte COMPARED_AS_DOUBLE_LESS       = 2;
    protected static final byte COMPARED_AS_DOUBLE_MORE       = 3;
    protected static final byte COMPARED_AS_STRING_EQUALS     = 4;
    protected static final byte COMPARED_AS_STRING_NOT_EQUALS = 5;
    
    protected final String POSSIBILITY_FLAG_POSITIVE_VALUE = "YES";
    protected final Double eps = 0.001;
    
    
    /**
     * Метод проверяет совпадение уставок с точночтью eps, если их возможно 
     * преобразовать в числа с плавающей точкой, либо сравнивает как строки, 
     * если преобразование невозможно.
     * 
     * @param setpointOne Строка, содержащаяя первую сравниваемую уставку
     * @param setpointTwo Строка, содержащаяя вторую сравниваемую уставку
     * @return Boolean
     */
    protected byte _compare(String setpointOne, String setpointTwo)
    {
        try //Пытаемся сравенить уставки как Double с точночтью eps:
        {
            //Если первая уставка больше (второй + eps):
            if (Double.parseDouble(setpointOne.replace(',', '.')) - Double.parseDouble(setpointTwo.replace(',', '.')) > this.eps)
                return COMPARED_AS_DOUBLE_MORE; 
            
            //Если (первая - eps) уставка меньше второй
            else if (Double.parseDouble(setpointOne.replace(',', '.')) - Double.parseDouble(setpointTwo.replace(',', '.')) < -this.eps)
                return COMPARED_AS_DOUBLE_LESS; 
            else 
                return COMPARED_AS_DOUBLE_EQUALS;
            
        } catch (Exception e){ //Если сравнить как Double не удалось, сравниваем как строки:
            
            if (!setpointOne.equals(setpointTwo)) return COMPARED_AS_STRING_NOT_EQUALS; 
            else return COMPARED_AS_STRING_EQUALS;
        }//catch
    }//compareSetpoints
}//Logic
