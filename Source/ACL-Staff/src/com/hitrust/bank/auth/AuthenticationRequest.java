/**
 * AuthenticationRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hitrust.bank.auth;

public class AuthenticationRequest  implements java.io.Serializable {
    private java.lang.String channelId;

    private java.lang.String txID;

    private java.lang.String inputParams;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(
           java.lang.String channelId,
           java.lang.String txID,
           java.lang.String inputParams) {
           this.channelId = channelId;
           this.txID = txID;
           this.inputParams = inputParams;
    }


    /**
     * Gets the channelId value for this AuthenticationRequest.
     * 
     * @return channelId
     */
    public java.lang.String getChannelId() {
        return channelId;
    }


    /**
     * Sets the channelId value for this AuthenticationRequest.
     * 
     * @param channelId
     */
    public void setChannelId(java.lang.String channelId) {
        this.channelId = channelId;
    }


    /**
     * Gets the txID value for this AuthenticationRequest.
     * 
     * @return txID
     */
    public java.lang.String getTxID() {
        return txID;
    }


    /**
     * Sets the txID value for this AuthenticationRequest.
     * 
     * @param txID
     */
    public void setTxID(java.lang.String txID) {
        this.txID = txID;
    }


    /**
     * Gets the inputParams value for this AuthenticationRequest.
     * 
     * @return inputParams
     */
    public java.lang.String getInputParams() {
        return inputParams;
    }


    /**
     * Sets the inputParams value for this AuthenticationRequest.
     * 
     * @param inputParams
     */
    public void setInputParams(java.lang.String inputParams) {
        this.inputParams = inputParams;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AuthenticationRequest)) return false;
        AuthenticationRequest other = (AuthenticationRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.channelId==null && other.getChannelId()==null) || 
             (this.channelId!=null &&
              this.channelId.equals(other.getChannelId()))) &&
            ((this.txID==null && other.getTxID()==null) || 
             (this.txID!=null &&
              this.txID.equals(other.getTxID()))) &&
            ((this.inputParams==null && other.getInputParams()==null) || 
             (this.inputParams!=null &&
              this.inputParams.equals(other.getInputParams())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getChannelId() != null) {
            _hashCode += getChannelId().hashCode();
        }
        if (getTxID() != null) {
            _hashCode += getTxID().hashCode();
        }
        if (getInputParams() != null) {
            _hashCode += getInputParams().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AuthenticationRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "AuthenticationRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("channelId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ChannelId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("txID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "TxID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "InputParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
