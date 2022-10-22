package com.example.RG.homeadapter;

public class HomeData {

private String grid,grimage,grname,grpwd;

    public HomeData(String grid, String grimage, String grname, String grpwd) {
        this.grid = grid;
        this.grimage = grimage;
        this.grname = grname;
        this.grpwd = grpwd;
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

    public String getGrpwd() {
        return grpwd;
    }

    public void setGrpwd(String grpwd) {
        this.grpwd = grpwd;
    }
}
