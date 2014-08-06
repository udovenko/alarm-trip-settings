package ru.sakhalinenergy.alarmtripsettings.util;


/**
 * Класс - библиотека небольших строковых утилит для задач парсинга номеров
 * тагов и инстансов бэкапов проектов.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class StringUtils 
{
    
    /**
     * Метод находит первый нечисловой символ в строке. Если строка состоит 
     * только из числовых символов, метод возвращает -1.
     * 
     * @param   highstack  Строка, в котрой ищем первый нечисловой символ
     * @return  void
     */
    public static int findFirstNonNumeric(String highstack)
    {
        for (int i=0; i<highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c < '0' || c > '9')
            {
                return i;
            }//if
        }//for
        
        return -1;
    }//findFirstNonNumeric
    
    
    /**
     * Метод находит последний нечисловой символ в строке. Если строка состоит 
     * только из числовых символов, метод возвращает -1.
     * 
     * @param   highstack  Строка, в котрой ищем первый нечисловой символ
     * @return  void
     */
    public static int findLastNonNumeric(String highstack)
    {
        int lastIndex = -1;
        
        for (int i=0; i<highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c < '0' || c > '9')
            {
                lastIndex = i;
            }//if
        }//for
        
        return lastIndex;
    }//findLastNonNumeric
    
    
    /**
     * Метод находит первый числовой символ в строке. Если строка состоит 
     * только из буквенных символов, метод возвращает -1.
     * 
     * @param   highstack  Строка, в котрой ищем первый числовой символ
     * @return  void
     */
    public static int findFirstNumeric(String highstack)
    {
        for (int i=0; i<highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c >= '0' && c <= '9')
            {
                return i;
            }//if
        }//for
        
        return -1;
    }//findFirstNumeric
    
    
    /**
     * Метод находит последний числовой символ в строке. Если строка состоит 
     * только из буквенных символов, метод возвращает -1.
     * 
     * @param   highstack  Строка, в котрой ищем первый числовой символ
     * @return  void
     */
    public static int findLastNumeric(String highstack)
    {
        int lastIndex = -1;
        
        for (int i=0; i<highstack.length(); i++)
        {
            char c = highstack.charAt(i);
            if (c >= '0' && c <= '9')
            {
                lastIndex = i;
            }//if
        }//for
        
        return lastIndex;
    }//findLastNumeric
    
    
    /**
     * Метод реализует буквенно-числовой инкрмент строки, т.е., например, строка
     * 124V3F становится 124V3G.
     * 
     * @param   original  Исходная строка
     * @param   minDigit  Начальный символ диапазона
     * @param   maxDigit  Конечный символ диапазона
     * @return  String
     */
    public static String incrementString(String original, char minDigit, char maxDigit)
    {
        StringBuilder buf = new StringBuilder(original);
        int index = buf.length() -1;
        
        while(index >= 0)
        {
            char c = buf.charAt(index);
            c++;
       
            //overflow, carry one:
            if(c > maxDigit) 
            { 
                buf.setCharAt(index, minDigit);
                index--;
                continue;
            }//if
            
            buf.setCharAt(index, c);
            return buf.toString();
        }//while
        
        //overflow at the first "digit", need to add one more digit:
        buf.insert(0, minDigit);
        
        return buf.toString();
    }//incrementString
    
    
    /**
     * Метод реализует буквенно-числовой декремент строки, т.е., например, 
     * строка 124V3G становится 124V3F.
     * 
     * @param   original  Исходная строка
     * @param   minDigit  Начальный символ диапазона
     * @param   maxDigit  Конечный символ диапазона
     * @return  String
     */
    public static String decrementString(String original, char minDigit, char maxDigit)
    {
        StringBuilder buf = new StringBuilder(original);
        int index = buf.length() -1;
        
        while(index >= 0)
        {
            char c = buf.charAt(index);
            c--;
       
            //overflow, carry one:
            if(c < minDigit) 
            { 
                buf.setCharAt(index, maxDigit);
                index--;
                continue;
            }//if
            
            buf.setCharAt(index, c);
            return buf.toString();
        }//whilw
        
        //overflow at the first "digit", need to add one more digit:
        buf.insert(0, maxDigit);
        
        return buf.toString();
    }//incrementString
}//StringUtils
