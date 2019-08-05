/*
 * @(#)DBExec.java
 *
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2004/07/20, Jackie Yang
 *   1) First release.
 */
package com.hitrust.acl.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Databse executor for access DBMS data.
 *
 * @author  Jackie Yang
 * @version 1.00, 2004/07/20
 */
public class DBExec {
	   //LOG4J
	static Logger LOG = Logger.getLogger(DBExec.class);

	// Public variable for reference
	public	Connection			connection	= null;
	public	Statement			statement	= null;
	public	ResultSet			resultSet	= null;
	public	ResultSetMetaData	metaData	= null;

    // The variables for null value
	private	String	nullString	= "";
	private	int		nullInt		= 0;
	private	long	nullLong	= 0;
	private	short	nullShort	= 0;
	private	double	nullDouble	= 0;
	
	private int		parameterCount = 0; //v6.00, 增加參數數量

	/**
	 * Constructor for JSP, must setConnection() before using.
	 */
	public DBExec() {}

	/**
	 * Constructor with DB connection.
	 * @param conn - DB Connection
	 */
	public DBExec(Connection conn) {
		this.connection = conn;
	}

	/**
	 * Set the connection for the execute SQL command.
	 * @param conn - DB Connection
	 */
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	/**
	 * Set value for returned when the string field wasNull().
	 * @param nval - null column return value
	 */
	public void setNullString(String nval) {
		this.nullString = nval;
	}

	/**
	 * Set value for returned when the integer field wasNull().
	 * @param nval - null column return value
	 */
	public void setNullInt(int nval) {
		this.nullInt = nval;
	}

	/**
	 * Set value for returned when the long field wasNull().
	 * @param nval - null column return value
	 */
	public void setNullLong(long nval) {
		this.nullLong = nval;
	}

	/**
	 * Set value for returned when the short field wasNull().
	 * @param nval - null column return value
	 */
	public void setNullShort(short nval) {
		this.nullShort = nval;
	}

	/**
	 * Set value for returned when the double field wasNull().
	 * @param nval - null column return value
	 */
	public void setNullDouble(double nval) {
		this.nullDouble= nval;
	}

	/**
	 * Get parameter count form SQL statement
	 * @return the parementCount
	 * @since v6.00
	 */
	public int getParameterCount() {
		return parameterCount;
	}

	/**
	 * Creates a Statement for sending SQL statements.
	 * @return the statement
	 * @throws SQLException - if connection error
	 */
	public Statement createStatement() throws SQLException {
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement.close();

		// Create statement
		statement = connection.createStatement();
		return statement;
	}

	/**
	 * Creates a Statement with the given type and concurrency
	 * @param type - Result set type; see ResultSet.TYPE_XXX
	 * @param concur - concurrency type; see ResultSet.CONCUR_XXX
	 * @return the statement
	 */
	public Statement createStatement(int type, int concur) throws SQLException {
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement.close();

		// Create statement
		statement = connection.createStatement(type, concur);
		return statement;
	}

	/**
	 * Create PrepareStatement for exeute server sql
	 * @param sql - sql statment with '?' for setting
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// v1.05
		sql = checkSQL(sql);
		
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement= null;

		// Create Prepare statement
		statement = connection.prepareStatement(sql);
		return (PreparedStatement)statement;
	}

	/**
	 * Creates a PreparedStatement object with the given type and concurrency
	 * @param sql - sql statment with '?' for setting
	 * @param type - Result set type; see ResultSet.TYPE_XXX
	 * @param concur - concurrency type; see ResultSet.CONCUR_XXX
	 */
	public PreparedStatement prepareStatement(String sql, int type, int concur) throws SQLException {
		// v1.05
		sql = checkSQL(sql);
		
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement.close();

		// Create Prepare statement
		statement = connection.prepareStatement(sql, type, concur);
		return (PreparedStatement)statement;
	}

	/**
	 * Create Callablestatement for exeute server stored procedure
	 * @param sql - Stored procedure call statment with '?' for setting
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement.close();

		// Create Prepare statement
		statement = connection.prepareCall(sql);
		return (CallableStatement)statement;
	}

	/**
	 * Creates a CallableStatement object with the given type and concurrency
	 * @param sql - sql statment with '?' for setting
	 * @param type - Result set type; see ResultSet.TYPE_XXX
	 * @param concur - concurrency type; see ResultSet.CONCUR_XXX
	 */
	public CallableStatement prepareCall(String sql, int type, int concur) throws SQLException {
		// Clear result set before new statement
		this.clearResult();
		if(statement != null) statement.close();

		// Create Prepare statement
		statement = connection.prepareCall(sql, type, concur);
		return (CallableStatement)statement;
	}

	/**
	 * Clear PrepareStatement current parameter values
	 */
	public void clearParameters() throws SQLException {
		((PreparedStatement)statement).clearParameters();
	}

	/**
	 * Execute sql statement with PrepareStatement object
	 */
	public int execute() throws SQLException {
		// Clear result set before execute SQL
		this.clearResult();

		// Execute sql
		if(((PreparedStatement)statement).execute()) {
			resultSet = statement.getResultSet();
			metaData  = resultSet.getMetaData() ;
			return -1;
		}
		return statement.getUpdateCount();
	}

	/**
	 * Execute sql statement with Statement object
	 * @param sql - sql statment for execute
	 */
	public int execute(String sql) throws SQLException {
		// Clear result set before execute SQL
		this.clearResult();

		// Execute sql
		if(statement.execute(sql)) {
			resultSet = statement.getResultSet();
			metaData  = resultSet.getMetaData() ;
			return -1;
		}
		return statement.getUpdateCount();
	}

	/**
	 * Execute query sql statement with PrepareStatement object
	 */
	public ResultSet executeQuery() throws SQLException {
		// Clear result set before execute SQL
		this.clearResult();

		// Execute sql
		resultSet = ((PreparedStatement)statement).executeQuery();
		metaData  = resultSet.getMetaData();
		return resultSet;
	}

	/**
	 * Execute query sql statement with Statement object
	 * @param sql - sql statment for execute
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		// Clear result set before execute SQL
		this.clearResult();

		// Execute sql
		resultSet = statement.executeQuery(sql);
		metaData  = resultSet.getMetaData();
		return resultSet;
	}

	/**
	 * Execute update sql statement with Statement object
	 */
	public int executeUpdate() throws SQLException {
		return ((PreparedStatement)statement).executeUpdate();
	}

	/**
	 * Execute upadte sql statement with PrepareStatement object
	 * @param sql - sql statment for execute
	 */
	public int executeUpdate(String sql) throws SQLException {
		return statement.executeUpdate(sql);
	}

	/**
	 * add batch update sql statement to Statement object
	 * @param sql - sql statment for execute
	 */
	public void addBatch(String sql) throws SQLException {
		statement.addBatch(sql);
	}

	/**
	 * Execute batch update sql statement with Statement object
	 */
	public int[] executeBatch() throws SQLException {
		return statement.executeBatch();
	}

	/**
	 * Query one Object(first row & first column) with PrepareStatement object
	 */
	public Object queryObject() throws SQLException {
		// Execute query
		this.executeQuery();

		// Get result
		if(!this.next()) return null;
		return this.getObject(1);
	}

	/**
	 * Query one Object(first row & first column) with Statement object
	 * @param sql - sql statment for execute
	 */
	public Object queryObject(String sql) throws SQLException {
		// Execute query
		this.executeQuery(sql);

		// Get result
		if(!this.next()) return null;
		return this.getObject(1);
	}

	/**
	 * Query one String(first row & first column) with PrepareStatement object
	 */
	public String queryString() throws SQLException {
		// Execute query
		this.executeQuery();

		// Get result
		if(!this.next()) return nullString;
		return this.getString(1);
	}

	/**
	 * Query one String(first row & first column) with Statement object
	 * @param sql - sql statment for execute
	 */
	public String queryString(String sql) throws SQLException {
		// Execute query
		this.executeQuery(sql);

		// Get result
		if(!this.next()) return nullString;
		return this.getString(1);
	}

	/**
	 * Query one int(first row & first column) with PrepareStatement object
	 */
	public int queryInt() throws SQLException {
		// Execute query
		this.executeQuery();

		// Get result
		if(!this.next()) return nullInt;
		return this.getInt(1);
	}

	/**
	 * Query one int(first row & first column) with Statement object
	 * @param sql - sql statment for execute
	 */
	public int queryInt(String sql) throws SQLException {
		// Execute query
		this.executeQuery(sql);

		// Get result
		if(!this.next()) return nullInt;
		return this.getInt(1);
	}

	/**
	 * Query one long(first row & first column) with PrepareStatement object
	 */
	public long queryLong() throws SQLException {
		// Execute query
		this.executeQuery();

		// Get result
		if(!this.next()) return nullLong;
		return this.getLong(1);
	}

	/**
	 * Query one long(first row & first column) with Statement object
	 * @param sql - sql statment for execute
	 */
	public long queryLong(String sql) throws SQLException {
		// Execute query
		this.executeQuery(sql);

		// Get result
		if(!this.next()) return nullLong;
		return this.getLong(1);
	}

	/**
	 * Moves the cursor down one row from its current position
	 */
	public boolean next() throws SQLException {
		return resultSet.next();
	}

	/**
	 * Moves the cursor to the previous row from its current position
	 */
	public boolean previous() throws SQLException {
		return resultSet.previous();
	}

	/**
	 * Moves the cursor to the given row number in this ResultSet
	 */
	public boolean absolute(int row) throws SQLException {
		return resultSet.absolute(row);
	}

	/**
	 * Moves the cursor a relative number of rows
	 */
	public boolean relative(int rows) throws SQLException {
		return resultSet.relative(rows);
	}

	/**
	 * Return the field is null that last getXXX() called
	 */
	public boolean wasNull() throws SQLException {
		return resultSet.wasNull();
	}

	/**
	 * Get the number of columns in this ResultSet object
	 */
	public int getColumnCount() throws SQLException {
		return metaData.getColumnCount();
	}

	/**
	 * Get the designated column's name
	 */
	public String getColumnName(int columnIndex) throws SQLException {
		return metaData.getColumnName(columnIndex);
	}

	/**
	 * Get the whole column's names
	 */
	public String[] getColumnNames() throws SQLException {
		String[] columnNames = new String[ metaData.getColumnCount() ];
		for(int i=1; i<= columnNames.length; i++) {
			columnNames[i-1] = metaData.getColumnName(i);
		}
		return columnNames;
	}

	/**
	 * Gets the value of the column in the current row as String
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public Object getObject(String columnName) throws SQLException {
		return resultSet.getObject(columnName);
	}

	/**
	 * Gets the value of the column in the current row as String
	 * @param columnIndex - the index in select order, first is 1
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return resultSet.getObject(columnIndex);
	}

	/**
	 * Gets the value of the column in the current row as String
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public String getString(String columnName) throws SQLException {
		String column = resultSet.getString(columnName);
		if(resultSet.wasNull()) return nullString;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as String
	 * @param columnIndex - the index in select order, first is 1
	 */
	public String getString(int columnIndex) throws SQLException {
		String column = resultSet.getString(columnIndex);
		if(resultSet.wasNull()) return nullString;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as int
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public int getInt(String columnName) throws SQLException {
		int column = resultSet.getInt(columnName);
		if(resultSet.wasNull()) return nullInt;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as int
	 * @param columnIndex - the index in select order, first is 1
	 */
	public int getInt(int columnIndex) throws SQLException {
		int column = resultSet.getInt(columnIndex);
		if(resultSet.wasNull()) return nullInt;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as long
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public long getLong(String columnName) throws SQLException {
		long column = resultSet.getLong(columnName);
		if(resultSet.wasNull()) return nullLong;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as long
	 * @param columnIndex - the index in select order, first is 1
	 */
	public long getLong(int columnIndex) throws SQLException {
		long column = resultSet.getLong(columnIndex);
		if(resultSet.wasNull()) return nullLong;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as short
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public short getShort(String columnName) throws SQLException {
		short column = resultSet.getShort(columnName);
		if(resultSet.wasNull()) return nullShort;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as short
	 * @param columnIndex - the index in select order, first is 1
	 */
	public short getShort(int columnIndex) throws SQLException {
		short column = resultSet.getShort(columnIndex);
		if(resultSet.wasNull()) return nullShort;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as double
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public double getDouble(String columnName) throws SQLException {
		double column = resultSet.getDouble(columnName);
		if(resultSet.wasNull()) return nullDouble;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as double
	 * @param columnIndex - the index in select order, first is 1
	 */
	public double getDouble(int columnIndex) throws SQLException {
		double column = resultSet.getDouble(columnIndex);
		if(resultSet.wasNull()) return nullDouble;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as byet[]
	 * @param columnName - the SELECT column name or 'AS' name
	 */
	public byte[] getBytes(String columnName) throws SQLException {
		byte[] column = resultSet.getBytes(columnName);
		if(resultSet.wasNull()) return null;
		return column;
	}

	/**
	 * Gets the value of the column in the current row as byte[]
	 * @param columnIndex - the index in select order, first is 1
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		byte[] column = resultSet.getBytes(columnIndex);
		if(resultSet.wasNull()) return null;
		return column;
	}


    /**
     * Set the Strin g value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param value - the value would be set to the PreparedStatement
     */
    public void setString(int index, String value) throws SQLException {
        ((PreparedStatement)statement).setString(index, value);
    }

    /**
     * Set the int value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param value - the value would be set to the PreparedStatement
     */
    public void setInt(int index, int value) throws SQLException {
        ((PreparedStatement)statement).setInt(index, value);
    }

    /**
     * Set the long value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param value - the value would be set to the PreparedStatement
     */
    public void setLong(int index, long value) throws SQLException {
        ((PreparedStatement)statement).setLong(index, value);
    }

    /**
     * Set the double value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param value - the value would be set to the PreparedStatement
     */
    public void setDouble(int index, double value) throws SQLException {
        ((PreparedStatement)statement).setDouble(index, value);
    }

    /**
     * Set the byte[] value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param value - the value would be set to the PreparedStatement
     */
    public void setBytes(int index, byte[] value) throws SQLException {
        ((PreparedStatement)statement).setBytes(index, value);
    }

    /**
     * Set the null value to the PreparedStatement
     * @param index - the index in PreparedStatement order, first is 1
     * @param sqlType - the SQL type code defined in java.sql.Types 
     */
    public void setNull(int index, int sqlType) throws SQLException {
        ((PreparedStatement)statement).setNull(index, sqlType);
    }


	/**
	 * Clear ResultSetMetaData & ResultSet
	 */
	private void clearResult() throws SQLException {
		metaData = null;
		if(resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
	}

	/**
	 * Clear result and set null to connection.
	 */
	public void close() {
		try {
			// Check connection has opened ?
			if(connection == null || connection.isClosed()) return;

			// Clear result set & close statement for release resource
			this.clearResult();
			if(statement != null) {
				statement.close();
				statement = null;
			}
			connection = null;
		} catch (SQLException ex) {
			// Ingore error
		}
	}
	
	/**
	 * Check column exist
	 * @param column
	 * @return
	 */
	public boolean isColumnExist(String column) {
		
		try {
			if (resultSet != null) {
				return (resultSet.findColumn(column) > 0)? true : false;
			}
		} catch (SQLException e) {
			// column not exist
			return false;
		}
		return false;
	}

	/**
	 * Finalize - return connection when constructor
	 */
	protected void finalize() throws Throwable {
		//this.close();
		//super.finalize();
	}
	
	/**
	 * FUBONCR-2019 使用 Microsoft JDBC Driver 4.0 for SQL Server 發生DBException錯誤(必須宣告純量變 @P0and)
	 * 在問號前後加空白
	 * @param sql
	 * @return sql
	 * @since 1.05
	 * v6.00, 計算 where condition 的參數數量
	 */
	private String checkSQL(String sql) {
		//LOG.debug("--debug b prepareStatement:\n" + sql);
		//v6.00, Reset SQL parement count
		this.parameterCount = 0;
		
		if (sql != null && sql.indexOf("?") != -1) {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new StringReader(sql));
			int value = -1;
			try {
				while ((value = br.read()) != -1) {
					char c = (char) value;
					if ('?' == c) {
						sb.append(' ').append('?').append(' ');
						//v6.00, 累計 SQL 參數
						this.parameterCount++;
					} else {
						sb.append(c);
					}
				}
			} catch (IOException e) {}
			sql = sb.toString();
			//LOG.debug("--debug a prepareStatement:\n" + sql);
		}
		return sql;
	}
}
