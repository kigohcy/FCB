/*
 * @(#)GenericBean.java
 *
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2004/08/03, Jackie Yang
 *   1) First release
 *  v1.01, 2017/09/27, Eason Hsu
 *   1) TSBACL-161, [Fortify] Null Dereference
 */
package com.hitrust.acl.dao;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.util.DOM;

/**
 * Generic Business Object store in DB
 *
 * @author Jackie Yang
 * @version v1.00, 2004/08/03
 *
 */
abstract public class GenericBean implements Cloneable,Serializable{
    // log4J category
	static Logger LOG = Logger.getLogger(GenericBean.class);

    // The tags of XML document for store this object
    static private final String OBJECT_TAG   = "BeanObject";
    static private final String TABLE_TAG    = "table";
    static private final String FIELD_TAG    = "field";
    static private final String OBJ_TAG      = "object";
    static private final String OBJS_TAG     = "objects";
    static private final String OBJS_OBJ_TAG = "obj";

    // private attriutes
    protected Connection connection        = null; // Connection for accessing DB
    private   Document   objectDoc         = null; // Document to store this obejct
    private   Element    docRoot           = null; // Document to store this obejct

    /**
     * Constructor
     */
    public GenericBean() {
        // build the empty document
        this.objectDoc = DOM.newDoc();

        // create the document root node
        this.docRoot = this.objectDoc.createElement(OBJECT_TAG);
        this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));
        this.objectDoc.appendChild(docRoot);
    }

    /**
     * Constructor with XML document in byte[].
     * @param xmlBytes - XML documet in byte[].
     */
    public GenericBean(byte[] xmlBytes) throws DBException {
        try {
			// build the document from the byte[]
			this.objectDoc = DOM.loadDoc(xmlBytes);

			// get the document root node
			this.docRoot = this.objectDoc.getDocumentElement();
		} catch (UtilException e) {
			throw new DBException(e.getMessage());
		}
        
        /*
         * [IMPORTANT NOTE]
         *  1) the inherit child class must add this.fromXML()
         *     within this construtor.
         *
         *  2) if the inherit child class has object attributes,
         *     it must implemt objectToXML() & objectFromXML() and
         *     must add this.objectFromXML() within this construtor.
         */
    }

    /**
     * Put primitive attributes into the document.
     */
    abstract protected void toXML();

    /**
     * Set primitive attributes from the document.
     */
    abstract protected void fromXML();

    /**
     * Put object attributes into the document.
     */
    protected void objectToXML() {};

    /**
     * Set object attributes from the document.
     */
    protected void objectFromXML() {};

    /**
     * Set DB Connection to this obejct for accessing DB.
     * @param conn - the DB connection
     */
    final public void setConnection(Connection conn) {
        this.connection = conn;
    }

    /**
     * Get this object XML document byte[]
     * @return the document of the this object
     */
    final public Document getDocument() {
        // put primitive & object attributes into the document
        this.toXML();
        this.objectToXML();

        // return the document
        return this.objectDoc;
    }

    /**
     * Set document to this object doc
     */
    final public void setDocument(Document daoBeanDoc) {
    	this.objectDoc = daoBeanDoc;
    	this.docRoot   = this.objectDoc.getDocumentElement();
    	
    	this.fromXML();
    	this.objectFromXML();
    }

    /**
     * Get this object XML document byte[]
     * @return the byte[] of the this object in XML format
     * @throws DBException
     */
    final public byte[] getBytes() throws DBException {
        // put primitive & object attributes into the document
        this.toXML();
        this.objectToXML();

        try {
			// transform document to byte[]
			return DOM.getDocBytes(this.objectDoc);
		} catch (UtilException e) {
			throw new DBException(e.getMessage());
		}
    }

    /**
     * load attriutes by DBExce.
     * @param exec - the DBExec object that has get the result
     * @throws DBException
     */
    final public void execLoad(DBExec exec) throws DBException {
        // set to field definition to document
        this.toXML();

        // set document fields' value
        this.setFieldFromDB(exec);

        // set object primitive attriutes vaue from document
        this.fromXML();
    }

    /**
     * Query count by "KEY" in database,
     * to determine if this data on dataBase is exist.
     * Use this method ,after construct a bean by key and give the bean a connection
     */
    public boolean isExist() throws DBException {
        // put primitive attributes into the document
        this.toXML();

        // Processing
        DBExec exec = new DBExec(this.connection);
        try {
            // prepare SQL statement
            exec.prepareStatement(this.getExistSql());

            // set where key field values for execute
            this.setSqlWhere(exec, 1);

            // execute sql,
            exec.executeQuery();
            exec.next();
            if(exec.getInt(1) == 0) {
                return false;
            } else if(exec.getInt(1) == 1) {
                return true;
            } else {
                throw new DBException("DBP_MORE_ONE");
            }

            // set object's attributes from oject document

        } catch (SQLException e) {        	
        	throw new DBException(e.getMessage(), "DB_QUERY");
		} finally {
            // Close executor
            exec.close();

        } // try
    }

    /**
     * load attriutes from DB SELECT.
     * @throws DBException
     */
    public void load() throws DBException {
        // put primitive attributes into the document
        this.toXML();

        // Processing
        DBExec exec = new DBExec(this.connection);
        try {
            // prepare SQL statement
        	String sql = this.getLoadSql();
            exec.prepareStatement(sql);

            // set where key field values for execute
            this.setSqlWhere(exec, 1);
            // execute sql, if row not exist throw exeception
            exec.executeQuery();
            if(!exec.next()) {
                throw new DBException("DB_QRY");
            }

            // set document field node from query result
            this.setFieldFromDB(exec);

            // set object's attributes from oject document
            this.fromXML();

        } catch (SQLException e) {
			throw new DBException(e.getMessage(), "DB_QUERY");
		} finally {
            // Close executor
            exec.close();

        } // try
    }

    /**
     * Insert object into DB.
     * @throws DBException
     */
    public void insert() throws DBException {
        // put primitive attributes into the document
        this.toXML();

        // Processing
        DBExec exec = new DBExec(this.connection);
        try {
            // Prepare SQL statement
            exec.prepareStatement(this.getInsertSql());

            // Set field values for execute
            this.setSqlFields(exec, 1, true);
            
            // execute sql & check result
            if(exec.executeUpdate() < 1) {
                throw new DBException("DB_INS");
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage(), "DB_INS");
		} finally {
            // Close executor
            exec.close();

        } // try
    }

    /**
     * Update object in DB.
     * @throws DBException
     */
    public void update() throws DBException {
        // put primitive attributes into the document
        this.toXML();

        // Processing
        DBExec exec = new DBExec(this.connection);
        try {
            // Prepare SQL statement
            exec.prepareStatement(this.getUpdateSql());

            // Set update field values for execute
            int pno = this.setSqlFields(exec, 1, false);

            // set where key field values for execute
            this.setSqlWhere(exec, pno);

            // execute sql & check result
            if(exec.executeUpdate() < 1) {
                throw new DBException("DB_UPD");
            }

        } catch (SQLException e) {
			throw new DBException(e.getMessage(), "DB_UPD");
		} finally {
            // Close executor
            exec.close();

        } // try
    }

    /**
     * Delete object from DB
     * @throws DBException
     */
    public void delete() throws DBException {
        // put primitive attributes into the document
        this.toXML();

        // Processing
        DBExec exec = new DBExec(this.connection);
        try {
            // Prepare SQL statement
            exec.prepareStatement(this.getDeleteSql());

            // set where key field values for execute
            this.setSqlWhere(exec, 1);

            // execute sql & check result
            if(exec.executeUpdate() < 1) {
                throw new DBException("DB_DEL");
            }

        } catch (SQLException e) {
			throw new DBException(e.getMessage(), "DB_DEL");
		} finally {
            // Close executor
            exec.close();

        } // try
    }

    /**
     * Put table name into document.
     * @param tbname - table name of object in DB
     */
    final protected void putTableName(String tbname) {
        // Check is node exist ?
        if(this.getTableName() != null) {
            return;
        }

        // new node
        Element element = this.objectDoc.createElement(TABLE_TAG);
        element.setAttribute("name", tbname);

        // add to the document root element
        this.docRoot.appendChild(this.objectDoc.createTextNode("\t"));
        this.docRoot.appendChild(element);
        this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));
    }

    /**
     * Get table name from document.
     * @return the table name in DB of this object
     */
    final protected String getTableName() {
        // get NodeList of tag name is "table"
        NodeList list = this.objectDoc.getElementsByTagName(TABLE_TAG);
        if(list.getLength() == 0) {
            return null;
        }
        Element element = (Element) list.item(0);

        // return the element name attriute
        return element.getAttribute("name");
    }

    /**
     * Put field value into document.
     * @param fldname - field name of object
     * @param fldvalue - the field value
     * @param dtype - data type of the field (S:String/I:integer/L:long/D:doule/B:byte[])
     * @param iskey - is the field is key (Y/N)
     */
    final protected void putField(String fldname, String fldvalue, String dtype,
        String iskey) {
        // Check field value is null
        if(fldvalue == null) {
            fldvalue = "";

            // check if the node exist
        }
        Element element = this.getElement(FIELD_TAG, fldname);
        if(element == null) {
            // new node
            element = this.objectDoc.createElement(FIELD_TAG);
            element.setAttribute("name", fldname);
            element.setAttribute("value", fldvalue);
            element.setAttribute("dtype", dtype);
            element.setAttribute("iskey", iskey);

            // add to the document root element
            this.docRoot.appendChild(this.objectDoc.createTextNode("\t"));
            this.docRoot.appendChild(element);
            this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));

        } else {
            // set node attribute
            element.setAttribute("value", fldvalue);
        }
    }

    // int
    final protected void putField(String fldname, int fldvalue, String dtype,
        String iskey) {
        this.putField(fldname, Integer.toString(fldvalue), dtype, iskey);
    }

    // long
    final protected void putField(String fldname, long fldvalue, String dtype,
        String iskey) {
        this.putField(fldname, Long.toString(fldvalue), dtype, iskey);
    }

    // double
    final protected void putField(String fldname, double fldvalue, String dtype,
        String iskey) {
        this.putField(fldname, Double.toString(fldvalue), dtype, iskey);
    }

    // byte[]
    final protected void putField(String fldname, byte[] fldvalue, String dtype,
        String iskey) {
        if(fldvalue == null) {
            this.putField(fldname, "", dtype, iskey);
        } else {
            this.putField(fldname, new String(fldvalue), dtype, iskey);
        }
    }
    

    /**
     * Put associated field value into document.
     * @param fldname - field name of object
     * @param fldvalue - the field value
     * @param dtype - data type of the field (S:String/I:integer/L:long/D:doule/B:byte[])
     */
    final protected void putAssociatedField(String fldname, String fldvalue, String dtype) {
        // Check field value is null
        if(fldvalue == null) {
            fldvalue = "";

            // check if the node exist
        }
        Element element = this.getElement(FIELD_TAG, fldname);
        if(element == null) {
            // new node
            element = this.objectDoc.createElement(FIELD_TAG);
            element.setAttribute("name", fldname);
            element.setAttribute("value", fldvalue);
            element.setAttribute("dtype", dtype);
            element.setAttribute("iskey", "N");
            element.setAttribute("associated", "Y");

            // add to the document root element
            this.docRoot.appendChild(this.objectDoc.createTextNode("\t"));
            this.docRoot.appendChild(element);
            this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));

        } else {
            // set node attribute
            element.setAttribute("value", fldvalue);
        }
    }

    // int
    final protected void putAssociatedField(String fldname, int fldvalue, String dtype) {
        this.putAssociatedField(fldname, Integer.toString(fldvalue), dtype);
    }

    // long
    final protected void putAssociatedField(String fldname, long fldvalue, String dtype) {
        this.putAssociatedField(fldname, Long.toString(fldvalue), dtype);
    }

    // double
    final protected void putAssociatedField(String fldname, double fldvalue, String dtype) {
        this.putAssociatedField(fldname, Double.toString(fldvalue), dtype);
    }

    // byte[]
    final protected void putAssociatedField(String fldname, byte[] fldvalue, String dtype) {
        if(fldvalue == null) {
            this.putAssociatedField(fldname, "", dtype);
        } else {
            this.putAssociatedField(fldname, new String(fldvalue), dtype);
        }
    }    
    

    /**
     * Get field value from the document.
     * @param fldname - field name of object
     * @return the XML element's attribute of the specfic attribute
     */
    final protected String getFieldString(String fldname) {
        // get element of tag name is "field" & its 'name' is fldname
        Element element = this.getElement(FIELD_TAG, fldname);
        if(element == null) {
            return null;
        }

        // return the element value attriute
        return element.getAttribute("value");
    }

    // int
    final protected int getFieldInt(String fldname) {
        String fldvalue = getFieldString(fldname);
        if(fldvalue == null || fldvalue.trim().equals("")) {
            return 0;
        }
        return Integer.parseInt(fldvalue);
    }

    // long
    final protected long getFieldLong(String fldname) {
        String fldvalue = getFieldString(fldname);
        if(fldvalue == null || fldvalue.trim().equals("")) {
            return 0;
        }
        return Long.parseLong(fldvalue);
    }

    // double
    final protected double getFieldDouble(String fldname) {
        String fldvalue = getFieldString(fldname);
        if(fldvalue == null || fldvalue.trim().equals("")) {
            return 0;
        }
        return Double.parseDouble(fldvalue);
    }

    // byte[]
    final protected byte[] getFieldBytes(String fldname) {
        String fldvalue = getFieldString(fldname);
        if(fldvalue == null) {
            return null;
        }
        return fldvalue.getBytes();
    }

    /**
     * Put an object into an XML document
     * @param objID - the object ID in the parent class
     * @param obj - the object
     * @throws DBException
     */
    final protected void putObject(String objID, GenericBean obj) {       
        
		// check if the node exist
        Element element = this.getElement(OBJ_TAG, objID);
        // check object is null        
        if (obj == null) {
        	if (element!=null) {
        		this.docRoot.removeChild(element);
        	}
        	return;
        } 
        // get object value in xml format
        String objValue = null;
		try {
			objValue = new String(obj.getBytes(), APSystem.getSystemDefaultEncoding());			
		} catch (UnsupportedEncodingException e) {
			// never happen
			LOG.error("UnsupportedEncodingException : " + e.getMessage());
		} catch (DBException e) {
			// never happen
			LOG.error("DBException : " + e.getMessage());
		}		
		// get object class name
		String objClass = obj.getClass().getName();

        if(element == null) {        	
            // new node
            element = this.objectDoc.createElement(OBJ_TAG);
            element.setAttribute("name" , objID);
            element.setAttribute("class", objClass);
            element.setAttribute("value", objValue);

            // add to the document root element
            this.docRoot.appendChild(this.objectDoc.createTextNode("\t"));
            this.docRoot.appendChild(element);
            this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));

        } else {
            // set node attribute
            element.setAttribute("value", objValue);
        }
    }

    /**
     * Get object from a document by a particular object ID.
     * @param objID - the object ID in the parent class
     */
    final protected GenericBean getObject(String objID) {
        // check if element exist in document
        Element element = this.getElement(OBJ_TAG, objID);
        if (element == null) {
            return null;
        }
        
        // check value of the element
        if (element.getAttribute("value")==null && 
        		"".equals(element.getAttribute("value"))) {
        	return null;
        }

        // get the class constructor which argument is byte[]
        Class cls = null;
		try {
			cls = Class.forName(element.getAttribute("class"));
		} catch (ClassNotFoundException e) {
			// never happen
			LOG.error("ClassNotFoundException : " + e.getMessage());
			return null;
		}
		// new object from the xml byte[]
		GenericBean bean = null;		
        try {
			byte[] objBytes = element.getAttribute("value").getBytes(APSystem.getSystemDefaultEncoding());
			bean = (GenericBean) cls.newInstance();
			bean.setDocument(DOM.loadDoc(objBytes));
		} catch (UnsupportedEncodingException ex) {
			// never happen
			LOG.error("UnsupportedEncodingException : " + ex.getMessage());
		} catch (UtilException ex) {
			// never happen
			LOG.error("UtilException : " + ex.getMessage());
		} catch (InstantiationException ex) {
			// never happen
			LOG.error("InstantiationException : " + ex.getMessage());			
		} catch (IllegalAccessException ex) {
			// never happen
			LOG.error("IllegalAccessException : " + ex.getMessage());
		}
        
        return bean;
    }

    /**
     * put object array into an XML document.
     * @param objsID - the object array ID in the parent class
     * @param objs - the object array
     * @throws DBException
     */
    final protected void putObjectList(String objsID, GenericBean[] objs) {

        // check if the node exist remove it & its children
        Element objsElement = this.getElement(OBJS_TAG, objsID);
        if(objsElement != null) {
            this.docRoot.removeChild(objsElement);
        }
        
        // check if the objects is empty
        if(objs == null) {
            return;
        } 

        // new node
        objsElement = this.objectDoc.createElement(OBJS_TAG);
        objsElement.setAttribute("name", objsID);
        objsElement.setAttribute("class", objs.getClass().getName());

        // add to the document root element
        this.docRoot.appendChild(this.objectDoc.createTextNode("\t"));
        this.docRoot.appendChild(objsElement);
        this.docRoot.appendChild(this.objectDoc.createTextNode("\n"));

        // get each object value in xml format
        int count = 0;
        for(int i = 0; i < objs.length; i++) {
        	// check object is null
        	if (objs[i] == null) {
        		continue;
        	} else {
        		count++;
        	}
        	
        	String objValue = null;
            try {
				objValue = new String(objs[i].getBytes(), APSystem.getSystemDefaultEncoding());
			} catch (UnsupportedEncodingException e) {
				// never happen
				LOG.debug("UnsupportedEncodingException  : " + e.getMessage());
			} catch (DBException e) {
				// never happen
				LOG.debug("DBException  : " + e.getMessage());
			} 
            // new node
            Element element = this.objectDoc.createElement(OBJS_OBJ_TAG);
            element.setAttribute("value", objValue);

            // add to the object node
            objsElement.appendChild(this.objectDoc.createTextNode("\t"));
            objsElement.appendChild(element);
            objsElement.appendChild(this.objectDoc.createTextNode("\n"));
        }
        // set length attribute of objects
        objsElement.setAttribute("length", ""+count);
    }

    // v1.01, 修正 Fortify 白箱掃描( [Fortify] Null Dereference), 移除未使用 Method

    /**
     * Get the element with tag name & its name attriute.
     * @param tagName - the tag name to be get
     * @param nameAttr - the name attriute value
     */
    private Element getElement(String tagName, String nameAttr) {
        // get nodes of tag name is tagName
        NodeList list = this.objectDoc.getElementsByTagName(tagName);

        // parsing each node in the nodelist
        for(int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            String name = element.getAttribute("name");
            if(name.equalsIgnoreCase(nameAttr)) {
                return element;
            }
        }

        // not found
        return null;
    }

    /************************************************************
     * SQL statement for load(),insert(),update(),delete()
     ************************************************************/
    private String sqlSelect = null;
    private String sqlInsert = null;
    private String sqlUpdate = null;
    private String sqlDelete = null;
    private String sqlExist = null;

    // SQL for isExist()
    private String getExistSql() {
        if(this.sqlExist == null) {
            this.setupStatements();
        }
        return this.sqlExist;
    }

    // SQL for load()
    private String getLoadSql() {
        if(this.sqlSelect == null) {
            this.setupStatements();
        }
        return this.sqlSelect;
    }

    // SQL for insert()
    private String getInsertSql() {
        if(this.sqlInsert == null) {
            this.setupStatements();
        }
        return this.sqlInsert;
    }

    // SQL for update()
    private String getUpdateSql() {
        if(this.sqlUpdate == null) {
            this.setupStatements();
        }
        return this.sqlUpdate;
    }

    // SQL for delete()
    private String getDeleteSql() {
        if(this.sqlDelete == null) {
            this.setupStatements();
        }
        return this.sqlDelete;
    }

    // Setup all SQL statements, must be execute only one time.
    private void setupStatements() {
        // initial the sql statements to epty string
        String select = "";
        String insert = "";
        String values = "";
        String update = "";
        String where = "";

        // parsing each fields in XML document for building sql statements
        NodeList fieldList = this.objectDoc.getElementsByTagName(FIELD_TAG);
        for(int i = 0; i < fieldList.getLength(); i++) {
        	// check if associated
        	Element element = (Element) fieldList.item(i);
        	String associated = element.getAttribute("associated");
        	if ("Y".equals(associated)) {
        		continue;
        	}
            // get field attributes from document            
            String name = element.getAttribute("name");
            String iskey = element.getAttribute("iskey");
            // add comma if not the first one
            if(i > 0) {
                select += ", ";
                insert += ", ";
                values += ", ";
            }
            // set the specific string
            select += name;
            insert += name;
            values += "?";
            // check is the key field
            if(iskey.equalsIgnoreCase("Y")) {
                // key for where
                if(where.length() > 0) {
                    where += " AND ";
                }
                where += "(" + name + " = ?)";
            } else {
                // nonkey for updadt
                if(update.length() > 0) {
                    update += ", ";
                }
                update += name + "=?";
            }
        }

        // select
        this.sqlSelect =
            "SELECT " + select
            + "\nFROM " + this.getTableName()
            + "\nWHERE " + where;
        // insert
        this.sqlInsert =
            "INSERT INTO " + this.getTableName() + "(" + insert + ")"
            + "\nVALUES (" + values + ")";
        // update
        this.sqlUpdate =
            "UPDATE " + this.getTableName()
            + "\nSET " + update
            + "\nWHERE " + where;
        // delete
        this.sqlDelete =
            "DELETE FROM " + this.getTableName()
            + "\nWHERE " + where;
        this.sqlExist =
            "SELECT count(*) "
            + "\nFROM " + this.getTableName()
            + "\nWHERE " + where;

    }

    // set the WHERE field value for SQL PreparedStatement.
    private int setSqlWhere(DBExec exec, int pno) throws DBException {
        // Set where key field values for execute
        NodeList fieldList = this.objectDoc.getElementsByTagName(FIELD_TAG);
        for(int i = 0; i < fieldList.getLength(); i++) {
        	// check if associated
        	Element element = (Element) fieldList.item(i);
        	String associated = element.getAttribute("associated");
        	if ("Y".equals(associated)) {
        		continue;
        	}
            // get field attributes from document            
            String iskey = element.getAttribute("iskey");

            // check is key field
            if(iskey.equalsIgnoreCase("Y")) {
                String value = element.getAttribute("value");
                String dtype = element.getAttribute("dtype");

                // set statement parametr by data type
                this.setParameter(exec, pno, dtype, value);
                pno++;
            }
        }
        return pno;
    }

    // set the INSERT field value for SQL PreparedStatement.
    private int setSqlFields(DBExec exec, int pno, boolean all) throws DBException {
        // Set field values for execute
        NodeList fieldList = this.objectDoc.getElementsByTagName(FIELD_TAG);

        for(int i = 0; i < fieldList.getLength(); i++) {
        	// check if associated
        	Element element = (Element) fieldList.item(i);
        	String associated = element.getAttribute("associated");
        	if ("Y".equals(associated)) {
        		continue;
        	}        	
            // get field attributes from document
            String value = element.getAttribute("value");
            String dtype = element.getAttribute("dtype");
            String iskey = element.getAttribute("iskey");
            
            // check is key field
            if(all || !iskey.equalsIgnoreCase("Y")) {
                // set statement parametr by data type
                this.setParameter(exec, pno, dtype, value);
                pno++;
            }
        }
        return pno;
    }

    // set preparestatement parameter value
    private void setParameter(DBExec exec, int pno, String dtype,
        String value) throws DBException {
        try {
			// set statement parametr by data type
			if(dtype.equalsIgnoreCase("SF")) {
			    //if the value is "" and it is foreign key
			    //we need to put null into database.
			    if(value.equals("")) {
			        exec.setString(pno, null);
			    } else {
			        exec.setString(pno, value);
			    }
			} else if(dtype.equalsIgnoreCase("S")) {
			    exec.setString(pno, value);
			} else if(dtype.equalsIgnoreCase("I")) {
			    exec.setInt(pno, Integer.parseInt(value));

			} else if(dtype.equalsIgnoreCase("L")) {
			    exec.setLong(pno, Long.parseLong(value));

			} else if(dtype.equalsIgnoreCase("D")) {
			    exec.setDouble(pno, Double.parseDouble(value));

			} else if(dtype.equalsIgnoreCase("B")) {
			    exec.setBytes(pno, value.getBytes());

			} else {
			    exec.setString(pno, value);

			}
		} catch (NumberFormatException e) {
			throw new DBException(e.getMessage());
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
    }

    /**
     * set document field node value from DBExce.
     * @param exec - the DBExec object that has executed & ResultSet within it
     * @throws DBException
     */
    private void setFieldFromDB(DBExec exec) throws DBException {
        try {
			// parsing each fields in XML document & set from query result
			NodeList fieldList = this.objectDoc.getElementsByTagName(FIELD_TAG);
			for(int i = 0; i < fieldList.getLength(); i++) {
	        	// check if associated
	        	Element element = (Element) fieldList.item(i);
	        	String associated = element.getAttribute("associated");
	        	if ("Y".equals(associated)) {
	        		continue;
	        	}				
			    // get field attributes from document
			    String name = element.getAttribute("name");
			    String dtype = element.getAttribute("dtype");
			    String iskey = element.getAttribute("iskey");

			    // put field from result
			    if (exec.isColumnExist(name)) { // check column exist
			    	if(dtype.equalsIgnoreCase("B")) {
				        this.putField(name, new String(exec.getBytes(name)), dtype, iskey);
				    } else {
				        this.putField(name, exec.getString(name), dtype, iskey);
				    }	
			    }			    
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
    }
    
    
	public Object clone() throws CloneNotSupportedException {    
		// �I�s Object.clone();  
		return super.clone();
	}

}
