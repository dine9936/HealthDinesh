package com.healthmantra.myapplication;

public class TransactionMo {
    public String txnamount;
    public String txnid;
    public String orderid;
    public String txntime;
    public String txnStatus;
    public String bankName;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public TransactionMo() {
    }

    public TransactionMo(String type, String bankName, String txnStatus, String txnamount, String txnid, String orderid, String txntime) {
        this.txnamount = txnamount;
        this.txnid = txnid;
        this.orderid = orderid;
        this.txntime = txntime;
        this.txnStatus = txnStatus;
        this.bankName = bankName;
        this.type = type;
    }

    public String getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(String txnamount) {
        this.txnamount = txnamount;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTxntime() {
        return txntime;
    }

    public void setTxntime(String txntime) {
        this.txntime = txntime;
    }
}
