package com.example.RG.SearchAdapter;

public class SearchData {

private String grid,grimage,grname,grdisc,gruserName,onoff,groupwd,created_at;

    public SearchData(String grid, String grimage, String grname, String grdisc, String gruserName, String onoff, String groupwd, String created_at) {
        this.grid = grid;
        this.grimage = grimage;
        this.grname = grname;
        this.grdisc = grdisc;
        this.gruserName = gruserName;
        this.onoff = onoff;
        this.groupwd = groupwd;
        this.created_at = created_at;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getGrimage() {
        return grimage;
    }

    public void setGrimage(String grimage) {
        this.grimage = grimage;
    }

    public String getGrname() {
        return grname;
    }

    public void setGrname(String grname) {
        this.grname = grname;
    }

    public String getGrdisc() {
        return grdisc;
    }

    public void setGrdisc(String grdisc) {
        this.grdisc = grdisc;
    }

    public String getGruserName() {
        return gruserName;
    }

    public void setGruserName(String gruserName) {
        this.gruserName = gruserName;
    }

    public String getOnoff() {
        return onoff;
    }

    public void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    public String getGroupwd() {
        return groupwd;
    }

    public void setGroupwd(String groupwd) {
        this.groupwd = groupwd;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
