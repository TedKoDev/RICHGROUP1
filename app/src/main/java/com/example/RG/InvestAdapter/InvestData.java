package com.example.RG.InvestAdapter;

public class InvestData {

    private String invidx;//데이터 idx
    private String invkind;//분류1
    private String invname;//품명2
    private String invavg;//평가손익3
    private String invgetper;//수익률4
    private String invcount;//보유수량5
    private String invbuyavg;//매수평균가 6
    private String invnowprice; //평가금액7
    private String invbuyprice;//매수금액8

    public String getInvidx() {
        return invidx;
    }

    public void setInvidx(String invidx) {
        this.invidx = invidx;
    }

    public String getInvkind() {
        return invkind;
    }

    public void setInvkind(String invkind) {
        this.invkind = invkind;
    }

    public String getInvname() {
        return invname;
    }

    public void setInvname(String invname) {
        this.invname = invname;
    }

    public String getInvavg() {
        return invavg;
    }

    public void setInvavg(String invavg) {
        this.invavg = invavg;
    }

    public String getInvgetper() {
        return invgetper;
    }

    public void setInvgetper(String invgetper) {
        this.invgetper = invgetper;
    }

    public String getInvcount() {
        return invcount;
    }

    public void setInvcount(String invcount) {
        this.invcount = invcount;
    }

    public String getInvbuyavg() {
        return invbuyavg;
    }

    public void setInvbuyavg(String invbuyavg) {
        this.invbuyavg = invbuyavg;
    }

    public String getInvnowprice() {
        return invnowprice;
    }

    public void setInvnowprice(String invnowprice) {
        this.invnowprice = invnowprice;
    }

    public String getInvbuyprice() {
        return invbuyprice;
    }

    public void setInvbuyprice(String invbuyprice) {
        this.invbuyprice = invbuyprice;
    }

    public InvestData(String invidx, String invkind, String invname, String invavg, String invgetper, String invcount, String invbuyavg, String invnowprice, String invbuyprice) {
        this.invidx = invidx;
        this.invkind = invkind;
        this.invname = invname;
        this.invavg = invavg;
        this.invgetper = invgetper;
        this.invcount = invcount;
        this.invbuyavg = invbuyavg;
        this.invnowprice = invnowprice;
        this.invbuyprice = invbuyprice;
    }
}
