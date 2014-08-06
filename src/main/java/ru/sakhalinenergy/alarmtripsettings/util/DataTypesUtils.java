package ru.sakhalinenergy.alarmtripsettings.util;


/**
 * Класс - библиотека небольших утилит для чтения и преобразования типов данных.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class DataTypesUtils 
{
    
    /**
     * Метод читает массив из двух байт как беззнаковое короткое целое и 
     * возвращает его в виде обычного 4-байтового целого.
     * 
     * @throws  RuntimeException
     * @param   bytes             Входной массив из 2 байтов
     * @return  Обычное целое, представляющее беззнаковое короткое
     */
    public static int readBytesAsUnsignedShort(byte[] bytes)
    {
        if (bytes.length != 2) throw new RuntimeException("Method readBytesAsUnsignedShort: Wrong parameter length!");
        else return ((bytes[0] & 0xFF) << 0) | ((bytes[1] & 0xFF) << 8);
    }//readBytesAsUnsignedShort
    
    
    /**
     * Метод читает массив из 4 байт как беззнаковое длинное целое, 
     * предворительно проверяя длинну массива.
     * 
     * @param   bytes  Массив байт, который необходимо прочитать как число
     * @return  Беззнаковое целое, записанное в Long, если длина входного массива корректна 
     */
    public static long readAsUnsignedLong(byte[] bytes)
    {
        if (bytes.length == 4)
        {
            long value = 
                ((bytes[0] & 0xFF) <<  0) |
                ((bytes[1] & 0xFF) <<  8) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[3] & 0xFF) << 24);
            
            return value;
        } else {
        
            throw new RuntimeException("Method readAsUnsignedLong: Wrong parameter length!");
        }//else
    }//readAsUnsignedLong
}//DataTypesUtils
