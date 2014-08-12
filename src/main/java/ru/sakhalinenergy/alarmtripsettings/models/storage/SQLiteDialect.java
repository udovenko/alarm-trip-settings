package ru.sakhalinenergy.alarmtripsettings.models.storage;
 
import java.sql.Types;
import org.hibernate.Hibernate; 
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.VarArgsSQLFunction;


/**
 * Hibernate dialect for SQLite databases.
 * 
 * @author Virasak
 * @version 1.0.3
 */
public class SQLiteDialect extends Dialect
{
    
    /**
     * Public constructor. Registers column type and functions constants.
     */
    public SQLiteDialect() 
    {
        registerColumnType(Types.BIT, "integer");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.NUMERIC, "numeric");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BOOLEAN, "integer");

        registerFunction("concat", new VarArgsSQLFunction(Hibernate.STRING, "", "||", ""));
        registerFunction("mod", new SQLFunctionTemplate( Hibernate.INTEGER, "?1 % ?2" ));
        registerFunction("substr", new StandardSQLFunction("substr", Hibernate.STRING));
        registerFunction("substring", new StandardSQLFunction( "substr", Hibernate.STRING ));
    }// SQLiteDialect
 
    
    /**
     * Specifies identity columns feature presence.
     * 
     * @return Identity columns feature presence 
     */
    @Override
    public boolean supportsIdentityColumns() 
    {
        return true;
    }// supportsIdentityColumns 
 

    /**
     * Specifies data type identity column feature presence.
     *
     * @return Data type identity column feature presence
     */
    @Override
    public boolean hasDataTypeInIdentityColumn() 
    {
        // As specify in NHibernate dialect:
        return false; 
    }// hasDataTypeInIdentityColumn
 
 
    /**
     * Returns identity column data type string.
     * 
     * @return Identity column data type string
     */
    @Override
    public String getIdentityColumnString() 
    {
        return "integer";
    }// getIdentityColumnString
 
    
    /**
     * Returns identity selection query string.
     * 
     * @return Identity selection query string
     */
    @Override
    public String getIdentitySelectString() 
    {
        return "select last_insert_rowid()";
    }// getIdentitySelectString
 
    
    /**
     * Returns LIMIT keyword feature presence.
     * 
     * @return LIMIT keyword feature presence
     */
    @Override
    public boolean supportsLimit() 
    {
        return true;
    }// supportsLimit
    
    
    /**
     * Returns query limit string.
     * 
     * @param query Initial query string
     * @param hasOffset Offset presence flag
     * @return Query limit string
     */
    @Override
    protected String getLimitString(String query, boolean hasOffset) 
    {
        return new StringBuffer(query.length() + 20).
            append(query).
            append(hasOffset ? " limit ? offset ?" : " limit ?").
            toString();
    }// getLimitString
 
    
    /**
     * Specifies support of temporary tables.
     * 
     * @return Temporary tables feature presence
     */
    @Override
    public boolean supportsTemporaryTables() 
    {
        return true;
    }// supportsTemporaryTables
    
 
    /**
     * Specifies create temporary table query string.
     * 
     * @return Create temporary table query string
     */
    @Override
    public String getCreateTemporaryTableString() 
    {
        return "create temporary table if not exists";
    }// getCreateTemporaryTableString
 
    
    /**
     * Specifies drop temporary table after use feature presence.
     * 
     * @return Drop temporary table after use feature presence
     */
    @Override
    public boolean dropTemporaryTableAfterUse() 
    {
        return false;
    }// dropTemporaryTableAfterUse
 
    
    /**
     * Specifies current timestamp selection feature presence.
     * 
     * @return Current timestamp selection feature presence
     */
    @Override
    public boolean supportsCurrentTimestampSelection() 
    {
        return true;
    }// supportsCurrentTimestampSelection
    
 
    /**
     * Specifies if current time stamp select string is callable.
     * 
     * @return Current time stamp select string is callable flag
     */
    @Override
    public boolean isCurrentTimestampSelectStringCallable()
    {
        return false;
    }// isCurrentTimestampSelectStringCallable
 
    
    /**
     * Returns current timestamp selection string.
     * 
     * @return Current timestamp selection string
     */
    @Override
    public String getCurrentTimestampSelectString() 
    {
        return "select current_timestamp";
    }// getCurrentTimestampSelectString
 
    
    /**
     * Specifies union all feature presence.
     * 
     * @return Union all feature presence
     */
    @Override
    public boolean supportsUnionAll() 
    {
        return true;
    }// supportsUnionAll
    
    
    /**
     * Specifies alter table feature presence.
     * 
     * @return Alter table feature presence
     */
    @Override
    public boolean hasAlterTable() 
    {
        // As specify in NHibernate dialect:
        return false; 
    }// hasAlterTable
 
    
    /**
     * Specifies drop constraints feature presence.
     * 
     * @return Drop constraints feature presence
     */
    @Override
    public boolean dropConstraints() 
    {
        return false;
    }// dropConstraints
 
    
    /**
     * Returns add column query string.
     * 
     * @return Add column query string
     */
    @Override
    public String getAddColumnString() 
    {
        return "add column";
    }// getAddColumnString
    
 
    /**
     * Returns message for update query.
     * 
     * @return Message for update query
     */
    @Override
    public String getForUpdateString() 
    {
        return "";
    }// getForUpdateString
 
    
    /**
     * Specifies outer join update feature presence.
     * 
     * @return Outer join update feature presence
     */
    @Override
    public boolean supportsOuterJoinForUpdate() 
    {
        return false;
    }// supportsOuterJoinForUpdate
    
    
    /**
     * Returns an exception for drop foreign key query.
     * 
     * @return Exception for drop foreign key query
     */
    @Override
    public String getDropForeignKeyString() 
    {
        throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect");
    }// getDropForeignKeyString
    
 
    /**
     * Returns an exception for add foreign key constraint query.
     * 
     * @param constraintName Constraint name
     * @param foreignKey Array of foreign key field names
     * @param referencedTable Referenced table name
     * @param primaryKey Array of primary key field names
     * @param referencesPrimaryKey Specifies is constraint references primary key
     * @return Exception for add foreign key constraint query
     */
    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, 
        String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) 
    {
        throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
    }// getAddForeignKeyConstraintString
 
    
    /**
     * Returns an exception for add primary key constraint query.
     * 
     * @param constraintName Constraint name
     * @return Exception for add primary key constraint query
     */
    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) 
    {
        throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
    }// getAddPrimaryKeyConstraintString
 
    
    /**
     * Specifies "if exists" checking before table name feature presence.
     * 
     * @return "if exists" checking before table name feature presence
     */
    @Override
    public boolean supportsIfExistsBeforeTableName() 
    {
        return true;
    }// supportsIfExistsBeforeTableName
 
    
    /**
     * Specifies cascade delete feature presence.
     * 
     * @return Cascade delete feature presence
     */
    public boolean supportsCascadeDelete() 
    {
        return false;
    }// supportsCascadeDelete
}// SQLiteDialect
