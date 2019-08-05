/**
 * AuthenticationSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hitrust.bank.auth;

public interface AuthenticationSoap extends java.rmi.Remote {
    public com.hitrust.bank.auth.AuthenticationResponse getAuthentication(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
    public com.hitrust.bank.auth.AuthenticationResponse authenticationLogin(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
    public com.hitrust.bank.auth.AuthenticationResponse authenticationTx(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
    public com.hitrust.bank.auth.AuthenticationResponse queryErrorNo(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
    public com.hitrust.bank.auth.AuthenticationResponse unlock(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
    public com.hitrust.bank.auth.AuthenticationResponse authenticationCLogin(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException;
}
