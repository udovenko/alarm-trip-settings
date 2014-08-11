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
 * Implements data provider for MS Excel books based on mixed ODBC access and 
 * JXL library methods.
 * 
 * @author Denis Udovenko
 * @version 1.0.7
 */
public class XlsDataProvider
{
   
   public static final String DRIVER_NAME = "sun.jdbc.odbc.JdbcOdbcDriver";
   public static final String DRIVER_TYPE = "jdbc:odbc:Driver={MicroSoft Excel Driver (*.xls)}";
   
   public final String filePath;
   public final String connectionString;
   
   public Connection connection;
   
   
   /**
    * Public constructor. Sets up file path to MS Excel book and connection 
    * string.
    * 
    * @throws SQLException
    * @param filePath Path to MS Excel book
    */
    public XlsDataProvider(String filePath) throws SQLException
    {
        this.filePath = filePath;
        this.connectionString = DRIVER_TYPE + ";DBQ=" + filePath + ";ReadOnly=False;";
    }// XlsDataProvider
   
    
    /**
     * Connects to current MS Excel book.
     * 
     * @throws SQLException
     */
    public void connect() throws SQLException
    {
        this.connection = DriverManager.getConnection(connectionString);
    }// connect
    
    
    /**
     * Disconnects from current MS Excel book.
     * 
     * @throws SQLException
     */
    public void disconnect() throws SQLException
    {
        this.connection.close();
    }// disconnect
    
    
    /**
     * Returns status of current MS Excel book ODBC connection.
     * 
     * @throws SQLException
     * @return True if connection is open, else false 
     */
    public boolean isConnected() throws SQLException
    {
        return !this.connection.isClosed();
    }// isConnected
    
    
    /**
     * c and returns result as a
     * list of hashes.
     * 
     * @throws SQLException
     * @param query Select query string
     * @return Result records set as list of hashes
     */
    public List<HashMap> selectQuery(String query) throws SQLException
    {
         Statement statement = this.connection.createStatement();
         ResultSet recordset = statement.executeQuery(query);
         
         // Get records set field names array:
         ResultSetMetaData recordsetMetadata = recordset.getMetaData(); 
         List columnNames = new ArrayList();
         for (int i = 1; i <= recordsetMetadata.getColumnCount(); i++)
         {
             columnNames.add(recordsetMetadata.getColumnName(i));
         }// for
         
         List result = new ArrayList();
         
         while (recordset.next()) 
         {
             HashMap row = new HashMap();
             
             for (int i=1; i<=columnNames.size(); i++)
             {
                 row.put((String)columnNames.get(i-1), recordset.getString(i));
             }// for
             
             result.add(row);
         }// while
         
         recordset.close();
         statement.close();
         
         return result;
    }// selectQuery
    
    
    /**
     * Executes update query to current MS Excel book.
     * 
     * @throws SQLException
     * @param query Update query string
     */
    public void updateQuery(String query) throws SQLException
    {
         Statement statement = this.connection.createStatement();
         statement.executeUpdate(query);
         statement.close();
    }// update
    
    
    /**
     * Creates MS Excel book and its initial sheet with given name.
     * 
     * @throws IOException
     * @throws WriteException
     * @param tableName Initial sheet name
     */
    public void createDatabase(String tableName) throws IOException, WriteException
    {
        String filename = this.filePath;
        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);
        workbook.createSheet(tableName, 0);
        
        workbook.write();
        workbook.close();
    }// createDatabase
    
 
    /**
     * Renames sheet with given index of current MS Excel book.
     * 
     * @throws IOException
     * @throws WriteException
     * @throws BiffException
     * @param newName New name of sheet
     * @param sheetIndex Sheet to be renamed identifier
     */
    public void renameTable(String newName, int sheetIndex)
        throws IOException, WriteException, BiffException
    {
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        
        // Create writable copy of current book:
        WritableWorkbook copyWorkbook = Workbook.createWorkbook(new File(this.filePath), workbook);
        
        // Get and rename sheet:
        WritableSheet worksheet = copyWorkbook.getSheet(sheetIndex);
        worksheet.setName(newName);
        
        // Write and close writable copy:
        copyWorkbook.write(); 
        copyWorkbook.close();
        
        // Close current book:
        workbook.close();
    }// renameTable
    
    
    /**
     * Returns list of sheet names within current book.
     * 
     * @throws IOException
     * @throws BiffException
     * @return Sheet names list
     */
    public List<String> getTables() throws IOException, BiffException
    {
        // Initialize result:
        List result = new ArrayList();
        
        // Get book:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        
        // Iterate sheets and add names to result:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            result.add(workbook.getSheet(i).getName());
        }// for
        
        return result;
    }// getTables
    
    
    /**
     * Returns a list of given sheet headers (field names).
     * 
     * @throws IOException
     * @throws BiffException
     * @param tableName Name of sheet which headers list will be received
     * @return List of given sheet headers (field names)
     */
    public List<String> getFields(String tableName) throws IOException, BiffException
    {
        // Get book and sheet:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        Sheet worksheet = workbook.getSheet(tableName); 
        
        List result = new ArrayList();
        int colunmsCount = worksheet.getColumns();
        
        // Iterate sheet's cells in first row:
        for (int i=0; i<colunmsCount; i++)
        {
            try
            {
                // If cell is not empty, add its value to result:
                Cell cell = worksheet.getCell(i, 0);
                String cellContent = cell.getContents();
                
                if (!cellContent.equals("")) result.add(cellContent);
                
            } catch(Exception exception) {}
        }// for
        
        return result;
    }// getFields
    
    
    /**
     * Sets up fields for given sheet of current MS Excel book.
     * 
     * @throws IOException
     * @throws WriteException
     * @throws BiffException
     * @param tableName Name of sheet which fields will be set
     * @param fields List of field names to be set
     */
    public void setFields(String tableName, List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        this._setFields(tableName, fields);
    }// setFields
    
    
    /**
     * Sets up fields for a first sheet of current MS Excel book.
     * 
     * @throws IOException
     * @throws WriteException
     * @throws BiffException
     * @param fields List of field names to be set
     */
    public void setFields(List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        this._setFields(null, fields);
    }// setFields
    
    
    /**
     * Internal method for setting up fields for given sheet of current MS Excel
     * book.
     * 
     * @throws IOException
     * @throws WriteException
     * @throws BiffException
     * @param tableName Name of sheet which fields will be set, if null - headers will be set for a first sheet
     * @param fields List of field names to be set
     */
    private void _setFields(String tableName, List<String> fields) 
        throws IOException, WriteException, BiffException
    {
        // Get workbook and sheet:       
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath));
        Sheet worksheet = (tableName == null) ? workbook.getSheet(0) : workbook.getSheet(tableName); 
                    
        boolean headersIsAlreadyDefined = true;
        
        // Check if sheet already has such fields set:
        for (int i = 0; i < fields.size(); i++)
        {
            try
            {
                Cell cell = worksheet.getCell(i, 0);
                if (!cell.getContents().equals(fields.get(i))) headersIsAlreadyDefined = false;
            
            } catch(Exception exception) {
                
                headersIsAlreadyDefined = false;
            }// catch
        }// for
        
        // If headers not set, create them:
        if (!headersIsAlreadyDefined)
        {
            // Create writable copy of current book:
            WritableWorkbook copyWorkbook = Workbook.createWorkbook(new File(this.filePath), workbook);
        
            // Get sheet and paste a new row:
            WritableSheet copyWorksheet = (tableName == null) ? copyWorkbook.getSheet(0) : copyWorkbook.getSheet(tableName); 
            copyWorksheet.insertRow(0);
            
            // Set up header cells values:
            for (int i=0; i<fields.size(); i++)
            {
                Label label = new Label(i, 0, fields.get(i));
                copyWorksheet.addCell(label);
            }// for
        
            // Write and close book copy:
            copyWorkbook.write(); 
            copyWorkbook.close();
        }// if
        
        // Close current book:
        workbook.close();
    }// setFieldsHeaders
    
    
    /**
     * Creates a copy of current book and resets all its not empty cells format
     * to a text by adding spaces before each cell value. Very slow for large 
     * MS Excel books and requires memory proportional to double size of initial
     * book.
     * 
     * @throws IOException
     * @throws WriteException
     * @throws BiffException
     * @param copyFilePath Path, where a copy with reset format will be created
     */
    public void createDatabaseCopyWithResetFormat(String copyFilePath)
        throws IOException, WriteException, BiffException
    {
        // Get current book and create output one:
        Workbook currentWorkbook = Workbook.getWorkbook(new File(this.filePath));
        WritableWorkbook outputWorkbook = Workbook.createWorkbook(new File(copyFilePath), currentWorkbook);
        
        // Get current book sheets list:
        List<String> sheets = getTables();
        String tempSheetName;
        String tempContent;
            
        // Iterate received sheets:
        for (int sheetIndex = 0; sheetIndex < sheets.size(); sheetIndex++)
        {
            tempSheetName = sheets.get(sheetIndex);
            int columnsCount = outputWorkbook.getSheet(tempSheetName).getColumns();
            int rowsCount = outputWorkbook.getSheet(tempSheetName).getRows();
                
            // Iterate sheet rows:
            for (int rowIndex = 1; rowIndex < rowsCount; rowIndex++)
            {
                // Iterate cells in current row:
                for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++)
                {
                    tempContent = outputWorkbook.getSheet(tempSheetName).getCell(columnIndex, rowIndex).getContents();
                    Label label = new Label(columnIndex, rowIndex, " " + tempContent);
                    outputWorkbook.getSheet(tempSheetName).addCell(label);
                }// for
            }// for
        }// for
        
        // Write data of new book and close both books:
        currentWorkbook.close();
        outputWorkbook.write();
        outputWorkbook.close();
    }// createDatabaseCopyWithResetFormat
}// XlsDataProvider
