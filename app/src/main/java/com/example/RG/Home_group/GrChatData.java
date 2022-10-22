package com.example.RG.Home_group;

public class GrChatData {
    private String nick;
    private String roomnumber;
    private String comments;
    private int viewType;
    private String time;


    public GrChatData(String nick, String roomnumber, String comments, int viewType, String time) {
        this.nick = nick;
        this.roomnumber = roomnumber;
        this.comments = comments;
        this.viewType = viewType;
        this.time = time;
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}