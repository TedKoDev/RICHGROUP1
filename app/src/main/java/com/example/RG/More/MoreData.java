package com.example.RG.More;

public class MoreData {

    private String grid,grimage,grname,grdisc,gruserName,created_at;

    public MoreData(String grid, String grimage, String grname, String grdisc, String gruserName, String created_at) {
        this.grid = grid;
        this.grimage = grimage;
        this.grname = grname;
        this.grdisc = grdisc;
        this.gruserName = gruserName;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
