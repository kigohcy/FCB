/**
 * @(#)AbstractMnthCrdtCont.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 月額度累計 (MNTH_CRDT_CONT)
 * 
 * Modify History:
 *  v1.00, 2017/10/18, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.model.base;

import java.io.Serializable;

import com.hitrust.framework.model.BaseCommand;

public class AbstractMnthCrdtCont extends BaseCommand implements Serializable {

    private static final long serialVersionUID = -2839335137146794991L;
    
    // =============== KEY ===============
    private AbstractMnthCrdtCont.Id id; // 複合 KEY
    
    // =============== Table Attribute ===============
    private String custId;      // 身份證字號
    private String ecId;        // 平台代碼
    private String ecUser;      // 平台會員代碼
    private String realAcnt;    // 存款帳號
    private Long mnthCont;      // 月累計金額
    private String custSerl;    // 會員服務序號
    
    public AbstractMnthCrdtCont.Id getId() {
        return id;
    }
    public void setId(AbstractMnthCrdtCont.Id id) {
        this.id = id;
    }
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    public String getEcId() {
        return ecId;
    }
    public void setEcId(String ecId) {
        this.ecId = ecId;
    }
    public String getEcUser() {
        return ecUser;
    }
    public void setEcUser(String ecUser) {
        this.ecUser = ecUser;
    }
    public String getRealAcnt() {
        return realAcnt;
    }
    public void setRealAcnt(String realAcnt) {
        this.realAcnt = realAcnt;
    }
    public Long getMnthCont() {
        return mnthCont;
    }
    public void setMnthCont(Long mnthCont) {
        this.mnthCont = mnthCont;
    }
    public String getCustSerl() {
        return custSerl;
    }
    public void setCustSerl(String custSerl) {
        this.custSerl = custSerl;
    }
    
    // =============== 複合 KEY ===============
    public static class Id implements Serializable {
        
        private static final long serialVersionUID = 5102298865847962867L;

        // =============== Table Attribute ===============
        private String acntIndt;    // 帳號識別碼
        private String trnsMnth;    // 交易月份
        
        // =============== Getter & Setter ===============
        public String getAcntIndt() {
            return acntIndt;
        }
        public void setAcntIndt(String acntIndt) {
            this.acntIndt = acntIndt;
        }
        public String getTrnsMnth() {
            return trnsMnth;
        }
        public void setTrnsMnth(String trnsMnth) {
            this.trnsMnth = trnsMnth;
        }
    }
}
