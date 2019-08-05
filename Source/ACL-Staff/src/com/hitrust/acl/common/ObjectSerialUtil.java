/**
 * @(#)ObjectSerialUtil.java
 * Copyright(c)2007 HiTRUST Incorporated.All rights reserved.
 * Description :對java對象的序列化操作
 * create History:
 * v1.00, 2013/09/06, Krystal Lyu
 *  1) First release
 * v1.01, 2016/10/26, Ada Chen
 *  1) TSBACL-119, 修正白箱掃瞄問題
 */
package com.hitrust.acl.common;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.bank.model.AclCommand;
import com.hitrust.framework.model.Command;


public class ObjectSerialUtil {
    
    /**
     * Logging utiltity
     */
//    protected static Log LOG = LogFactory.getLog(ObjectSerialUtil.class.getName());
    private static Category LOG = Logger.getLogger(ObjectSerialUtil.class);
    

    /**
     * 將 command 轉為 xml
     * @author	Allen Wu
     * @param	command
     * @return	byte[]
     * @throws	Exception
     */
    public static byte[] objectXmlEncoder(Object command) throws Exception {
    	
//        //去除延遲加載的Collection對象的Hiber封裝
//        LOG.debug("before unPackCollection");
//        unPackCollection(command);
//        LOG.debug("after unPackCollection");
//    	
//    	XStream xstream = new XStream();
//    	String result = xstream.toXML(command); //object to xml
//    	return result.getBytes("UTF-8");		//TODO 編碼是否要用設定檔
    	
    	//创建XML文件对象输出类实例 
		ByteArrayOutputStream  fos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(fos);
		//去除延遲加載的Collection對象的Hiber封裝
		unPackCollection(command);
		//对象序列化输出到XML文件 
		encoder.writeObject(command);
		encoder.flush();
		//关闭序列化工具 
		encoder.close();
		byte[] bytes = fos.toByteArray();
		System.out.println("xml length ="+bytes.length);
		//关闭输出流 
		fos.close();
		return bytes;
    	
    }
    
    /**
     * 將 xml 轉為 command
     * @author	Allen Wu
     * @param	command
     * @return	byte[]
     * @throws	Exception
     */
    public static Object objectXmlDecoder(byte[] bytes) throws Exception {
    	
//    	XStream xstream = new XStream();
//    	String result = new String(bytes,"UTF-8");	//TODO 編碼是否要用設定檔
//    	Object obj = xstream.fromXML(result); // xml to object
    	
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		XMLDecoder decoder = new XMLDecoder(bi);
		Object obj = decoder.readObject();
		bi.close();
		decoder.close();
		return obj;
//    	return obj;
    }
    
    
    /**
     * 功能：將java对象序列化為字節流
     * @author Mark Chen
     * @param  Object command
     * @return byte[]
     * @throws Exception
     */
    public static byte[] objectToBytes(Object command)throws Exception{
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        //去除延遲加載的Collection對象的Hiber封裝
        unPackCollection(command);
        out.writeObject(command);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        LOG.debug("pure byte length="+bytes.length);
        return bytes;
    }
    
    /**
     * 功能：由字節流反序列化java对象
     * @author Mark Chen
     * @param  String objSource
     * @return List
     * @throws Exception
     */
    public static Object bytesToObject(byte[] bytes)throws Exception{
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        
        // We use LookAheadObjectInputStream instead of InputStream
        ObjectInputStream oi = new LookAheadObjectInputStream(bi);

     	// use the protected ObjectInputStream to read object safely
        Object obj = (Command)oi.readObject();
        
        bi.close();
        oi.close();
        return obj;
    }
    
    /**
     * 功能：將Hibernate封裝過的Collection對象還原成原始狀態
     * @author Mark Chen
     * @param  Object command
     * @throws Exception
     */
    private static void unPackCollection(Object command)throws Exception{
        Class superClass = command.getClass().getSuperclass();
        Field[] fields = superClass.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Field field = fields[i];
            if(field.getType().getName().equals("java.util.List")){
LOG.debug("fieldType: java.util.List");
                String getMethodName="get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
                String setMethodName="set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
LOG.debug("getMethodName: " + getMethodName);
LOG.debug("setMethodName: " + setMethodName);
                try {
                    Method getMethod =superClass.getMethod(getMethodName, null);
                    List packedList = (List)getMethod.invoke(command, null);
                    if (packedList == null) {
                        LOG.debug("packedList is null!");
                    } else {
                        List upPackedList = new ArrayList();
                        Iterator it = packedList.iterator();
                        while(it.hasNext()){
                            upPackedList.add(it.next());
                        }
                        Method setMethod=superClass.getMethod(setMethodName,new Class[]{List.class} );
                        setMethod.invoke(command, new Object[]{upPackedList});
                    }
                } catch (Exception ex) {
                    LOG.error("Failed to unPackCollection: " + ex.toString());
                }
            }
            if(field.getType().getName().equals("java.util.Set")){
LOG.debug("fieldType: java.util.Set");
                String getMethodName="get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
                String setMethodName="set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
LOG.debug("getMethodName: " + getMethodName);
LOG.debug("setMethodName: " + setMethodName);
                try {
                    Method getMethod =superClass.getMethod(getMethodName, null);
                    Set packedSet = (Set)getMethod.invoke(command, null);
                    if (packedSet == null) {
                        LOG.debug("packedSet is null!");
                    } else {
                        Set upPackedSet = new HashSet();
                        Iterator it = packedSet.iterator();
                        while(it.hasNext()){
                            upPackedSet.add(it.next());
                        }
                        Method setMethod=superClass.getMethod(setMethodName,new Class[]{Set.class} );
                        setMethod.invoke(command, new Object[]{upPackedSet});
                    }
                } catch (Exception ex) {
                    LOG.error("Failed to unPackCollection: " + ex.toString());
                }
            }
            if(field.getType().getName().equals("java.util.Map")){
LOG.debug("fieldType: java.util.Map");
                String getMethodName="get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
                String setMethodName="set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
LOG.debug("getMethodName: " + getMethodName);
LOG.debug("setMethodName: " + setMethodName);
                try {
                    Method getMethod =superClass.getMethod(getMethodName, null);
                    Map packedMap = (Map)getMethod.invoke(command, null);
                    if (packedMap == null) {
                        LOG.debug("packedMap is null!");
                    } else {
                        Map upPackedMap = new HashMap();
                        Iterator it = packedMap.keySet().iterator();
                        while(it.hasNext()){
                            Object key = it.next();
                            upPackedMap.put(key,packedMap.get(key));
                        }
                        Method setMethod=superClass.getMethod(setMethodName,new Class[]{Map.class} );
                        setMethod.invoke(command, new Object[]{upPackedMap});
                    }
                } catch (Exception ex) {
LOG.error("Failed to unPackCollection: " + ex.toString());
                }
            }
        }
    }
}
  
