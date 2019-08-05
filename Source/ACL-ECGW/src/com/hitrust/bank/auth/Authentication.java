/**
 * Authentication.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hitrust.bank.auth;

public interface Authentication extends javax.xml.rpc.Service {
    public java.lang.String getAuthenticationSoapAddress();

    public com.hitrust.bank.auth.AuthenticationSoap getAuthenticationSoap() throws javax.xml.rpc.ServiceException;

    public com.hitrust.bank.auth.AuthenticationSoap getAuthenticationSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
