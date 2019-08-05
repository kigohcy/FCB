package com.hitrust.bank.auth;

public class AuthenticationSoapProxy implements com.hitrust.bank.auth.AuthenticationSoap {
  private String _endpoint = null;
  private com.hitrust.bank.auth.AuthenticationSoap authenticationSoap = null;
  
  public AuthenticationSoapProxy() {
    _initAuthenticationSoapProxy();
  }
  
  public AuthenticationSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initAuthenticationSoapProxy();
  }
  
  private void _initAuthenticationSoapProxy() {
    try {
      authenticationSoap = (new com.hitrust.bank.auth.AuthenticationLocator()).getAuthenticationSoap();
      if (authenticationSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)authenticationSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)authenticationSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (authenticationSoap != null)
      ((javax.xml.rpc.Stub)authenticationSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.hitrust.bank.auth.AuthenticationSoap getAuthenticationSoap() {
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap;
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse getAuthentication(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.getAuthentication(request);
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse authenticationLogin(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.authenticationLogin(request);
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse authenticationTx(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.authenticationTx(request);
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse queryErrorNo(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.queryErrorNo(request);
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse unlock(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.unlock(request);
  }
  
  public com.hitrust.bank.auth.AuthenticationResponse authenticationCLogin(com.hitrust.bank.auth.AuthenticationRequest request) throws java.rmi.RemoteException{
    if (authenticationSoap == null)
      _initAuthenticationSoapProxy();
    return authenticationSoap.authenticationCLogin(request);
  }
  
  
}