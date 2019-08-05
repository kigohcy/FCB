/**
 * AuthenticationLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hitrust.bank.auth;

import com.hitrust.bank.common.AuthenticationHelper;

public class AuthenticationLocator extends org.apache.axis.client.Service implements com.hitrust.bank.auth.Authentication {

    public AuthenticationLocator() {
    }


    public AuthenticationLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AuthenticationLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AuthenticationSoap
    //private java.lang.String AuthenticationSoap_address = "http://svaap1-test:7001/Authentication.asmx";
    private java.lang.String AuthenticationSoap_address = "";

    public java.lang.String getAuthenticationSoapAddress() {
        return AuthenticationSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuthenticationSoapWSDDServiceName = "AuthenticationSoap";

    public java.lang.String getAuthenticationSoapWSDDServiceName() {
        return AuthenticationSoapWSDDServiceName;
    }

    public void setAuthenticationSoapWSDDServiceName(java.lang.String name) {
        AuthenticationSoapWSDDServiceName = name;
    }

    public com.hitrust.bank.auth.AuthenticationSoap getAuthenticationSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
        	//get web service url
        	AuthenticationSoap_address = AuthenticationHelper.webServiceURL;
        	
            endpoint = new java.net.URL(AuthenticationSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAuthenticationSoap(endpoint);
    }

    public com.hitrust.bank.auth.AuthenticationSoap getAuthenticationSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hitrust.bank.auth.AuthenticationSoapStub _stub = new com.hitrust.bank.auth.AuthenticationSoapStub(portAddress, this);
            _stub.setPortName(getAuthenticationSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAuthenticationSoapEndpointAddress(java.lang.String address) {
        AuthenticationSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hitrust.bank.auth.AuthenticationSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hitrust.bank.auth.AuthenticationSoapStub _stub = new com.hitrust.bank.auth.AuthenticationSoapStub(new java.net.URL(AuthenticationSoap_address), this);
                _stub.setPortName(getAuthenticationSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("AuthenticationSoap".equals(inputPortName)) {
            return getAuthenticationSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "Authentication");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "AuthenticationSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AuthenticationSoap".equals(portName)) {
            setAuthenticationSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
