package ru.sakhalinenergy.alarmtripsettings.models.provider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Locale;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import jxl.WorkbookSettings;
import jxl.Workbook;
import jxl.Sheet;
import jxl.Cell;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.WriteException;


/**
 * 
 * 
 */
public class XlsDataProvider
{
   
   public static final String DRIVER_NAME = "sun.jdbc.odbc.JdbcOdbcDriver";
   public static final String DRIVER_TYPE = "jdbc:odbc:Driver={MicroSoft Excel Driver (*.xls)}";
   public final String filePath;
   public final String connectionString;
   public Connection connection;
   
   
   /**
    * 
    * 
    */
    public XlsDataProvider(String file_path) throws SQLException
    {
        this.filePath = file_path;
        this.connectionString = DRIVER_TYPE + ";DBQ=" + file_path + ";ReadOnly=False;";
    }//XlsDataProvider
   
    
    /**
     * 
     * 
     * 
     */
    public void connect() throws SQLException
    {
        this.connection = DriverManager.getConnection(connectionString);
    }//checkConnection
    
    
    /**
     * 
     * 
     * 
     */
    public void disconnect() throws SQLException
    {
        this.connection.close();
    }//disconnect
    
    
    /**
     * Метод возвращает статус ODBC - соединения с книгой MS Excel.
     * 
     * @throws SQLException
     * @return true, если соединение открыто, иначе false 
     */
    public boolean isConnected() throws SQLException
    {
        return !this.connection.isClosed();
    }//isConnectionClosed
    
    
    /**
     * 
     * 
     */
    public List<HashMap> selectQuery(String query) throws SQLException
    {
         //Connection connection = DriverManager.getConnection(this.CONNECTION_STRING);
         Statement statement = this.connection.createStatement();
         ResultSet recordset = statement.executeQuery(query);
         
         //Получаем массив имен полей рекордсета:
         ResultSetMetaData recordsetMetadata = recordset.getMetaData(); 
         List columnNames = new ArrayList();
         for (int i = 1; i<=recordsetMetadata.getColumnCount(); i++)
         {
             columnNames.add(recordsetMetadata.getColumnName(i));
         }//for
         
         List result = new ArrayList();
         
         while (recordset.next()) {
             HashMap row = new HashMap();
             
             for (int i=1; i<=columnNames.size(); i++)
             {
                 row.put((String)columnNames.get(i-1), recordset.getString(i));
             }//for
             
             result.add(row);
         }//while
         
         recordset.close();
         statement.close();
         
         return result;
    }//query
    
    
    /**
     * 
     * 
     */
    public void updateQuery(String query) throws SQLException
    {
         Statement statement = this.connection.createStatement();
         statement.executeUpdate(query);
         statement.close();
    }//update
    
    
    /**
     * 
     * 
     */
    public void createDatabase(String tableName) throws IOException, WriteException
    {
        String filename = this.filePath;
        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);
        WritableSheet s =  workbook.createSheet(tableName, 0);
        
        workbook.write();
        workbook.close();
    }//createDatabase
    
 
    /**
     * Метод пререименовывает лист книги (таблицу базы данных в смысле драйвера
     * ODBC), заданных числовым индексом.
     * 
     * @access  public
     * @throws  IOException
     * @throws  WriteException
     * @throws  BiffException
     * @return  void
     */
    public void renameTable(String newName, int sheetIndex)
        throws IOException, WriteException, BiffException
    {
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        
        //Создаем записываемую копию текущей книги:
        WritableWorkbook copyWorkbook = Workbook.createWorkbook(new File(this.filePath), workbook);
        //Получаем лист и переименовываем:
        WritableSheet worksheet = copyWorkbook.getSheet(sheetIndex);
        worksheet.setName(newName);
        
        //Записываем и сохранаяем:
        copyWorkbook.write(); 
        copyWorkbook.close();
        
        //Закрываем исходный документ:
        workbook.close();
    }//renameTable
    
    
    /**
     * Метод получает список имен листов текущей книги (таблиц базы данных в
     * смысле драйвера ODBC).
     * 
     * @access  public
     * @throws  IOException
     * @throws  BiffException
     * @return  List<String>
     */
    public List<String> getTables() 
        throws IOException, BiffException
    {
        //Инициализируем результат:
        List result = new ArrayList();
        
        //Получаем книгу:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        
        //Обходим список листов книги и добавляем их имена в результат:
        for (int i=0; i<workbook.getNumberOfSheets(); i++)
        {
            result.add(workbook.getSheet(i).getName());
        }//for
        
        return result;
    }//getTables
    
    
    /**
     * Метод возвращает список заголовков листа текущей книги (названий 
     * полей в смысле драйвера ODBC).
     * 
     * @access  public
     * @param   tableName  Имя листа, для которого тполучаем набор полей
     * @return  void
     */
    public List<String> getFields(String tableName)
        throws IOException, BiffException
    {
        //Получаем книгу и лист:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        Sheet worksheet = workbook.getSheet(tableName); 
        
        //Иницитализируем рабочие переменные:
        List result = new ArrayList();
        int colunmsCount = worksheet.getColumns();
        
        //Перебираем первые ячейки всех колонок листа:
        for (int i=0; i<colunmsCount; i++)
        {
            try
            {
                //Если ячейка не пустая, добавляем ее значение в результат:
                Cell cell = worksheet.getCell(i, 0);
                String cellContent = cell.getContents();
                if (!cellContent.equals(""))
                {
                    result.add(cellContent);
                }//if
            } catch(Exception e) {
                
            }//catch
        }//for
        
        return result;
    }//getFields
    
    
    /**
     * 
     * 
     */
    public void setFields(String tableName, List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        this._setFields(tableName, fields);
    }//setFields
    
    
    /**
     * 
     * 
     */
    public void setFields(List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        this._setFields(null, fields);
    }//setFields
    
    
    /**
     * 
     * 
     */
    private void _setFields(String tableName, List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        //Проверяем, нет ли у листа такого заголовка на данный момент:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        Sheet worksheet = (tableName == null) ? workbook.getSheet(0) : workbook.getSheet(tableName); 
                    
        java.lang.Boolean headersIsAlreadyDefined = true;
        
        //Обходим ячейки первой строки и сравниваем их с заданным списком заголовков:
        for (int i=0; i<fields.size(); i++)
        {
            try
            {
                Cell cell = worksheet.getCell(i, 0);
                if (!cell.getContents().equals(fields.get(i)))
                {
                    headersIsAlreadyDefined = false;
                }//if
            } catch(Exception e) {
                headersIsAlreadyDefined = false;
            }//catch
        }//for
        
        //Если заголовки не заданы, создаем их:
        if (!headersIsAlreadyDefined)
        {
            //Создаем записываемую копию текущей книги:
            WritableWorkbook copyWorkbook = Workbook.createWorkbook(new File(this.filePath), workbook);
        
            //Получаем лист и вставляем строку:
            WritableSheet copyWorksheet = (tableName == null) ? copyWorkbook.getSheet(0) : copyWorkbook.getSheet(tableName); 
            copyWorksheet.insertRow(0);
            
            //Задаем значения ячеек-заголовков:
            for (int i=0; i<fields.size(); i++)
            {
                Label label = new Label(i, 0, fields.get(i));
                copyWorksheet.addCell(label);
            }//for
        
            //Записываем и сохранаяем:
            copyWorkbook.write(); 
            copyWorkbook.close();
        }//if
        
        //Закрываем исходный документ:
        workbook.close();
    }//setFieldsHeaders
    
    
    /**
     * Метод создает копию текущей книги по заданному пути и сбрасывает флормат 
     * всех значащих ячеек на текстовый путем добавления пробела перед значением
     * каждой ячейки. Для тяжелых книг работает долго, также возможен выход
     * за допустимый размер памяти.
     * 
     * @throws IOException, WriteException, BiffException
     * @param copyFilePath Путь к создаваемой копии текущей книги
     */
    public void createDatabaseCopyWithResetFormat(String copyFilePath) throws IOException, WriteException, BiffException
    {
        //Получаем книгу и лист:       
        Workbook currentWorkbook = Workbook.getWorkbook(new File(this.filePath));
        WritableWorkbook outputWorkbook = Workbook.createWorkbook(new File(copyFilePath), currentWorkbook);
        
        //Получаем список листов текущей книги:
        List<String> sheets = getTables();
        String tempSheetName;
        String tempContent;
            
        //Обходим все листы книги:
        for (int sheetIndex = 0; sheetIndex < sheets.size(); sheetIndex++)
        {
            tempSheetName = sheets.get(sheetIndex);
            int columnsCount = outputWorkbook.getSheet(tempSheetName).getColumns();
            int rowsCount = outputWorkbook.getSheet(tempSheetName).getRows();
                
            //Обходим все строки:
            for (int rowIndex = 1; rowIndex < rowsCount; rowIndex++)
            {
                //Обходим все ячейки в текущей строке:
                for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++)
                {
                    tempContent = outputWorkbook.getSheet(tempSheetName).getCell(columnIndex, rowIndex).getContents();
                    Label label = new Label(columnIndex, rowIndex, " " + tempContent);
                    outputWorkbook.getSheet(tempSheetName).addCell(label);
                }//for
            }//for
        }//for
        
        currentWorkbook.close();
        outputWorkbook.write();
        outputWorkbook.close();
    }//createDatabaseCopyWithResetFormat
}//XlsDataProvider
