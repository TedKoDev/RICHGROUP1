package com.example.RG.ChatroomAdapterData;

public class ChatroomDATA {

//    private String chroom_idx,chroom_img,chroom_name, chroom_lasttext,chroom_people,chroom_lasttime, chroom_unreadtext;
    private String chroom_idx; // 룸 idx
    private String chroom_name; // 룸 이름
    private String chroom_img; // 룸 이미지
    private String chroom_lastwriter; // 룸 마지막 작성자
    private String chroom_lasttext; // 마지막 채팅글
    private String chroom_lasttime; // 마지막 텍스트 입력시간
    private String msgcount; // 마지막 텍스트 입력시간
//    private String chroom_unreadtext; // 안읽은 텍스트 수


    public ChatroomDATA(String chroom_idx, String chroom_name, String chroom_img, String nick, String contents, String cmtime,String msgcount) {
        this.chroom_idx = chroom_idx;
        this.chroom_name = chroom_name;
        this.chroom_img = chroom_img;
        this.chroom_lastwriter = nick;
        this.chroom_lasttext = contents;
        this.chroom_lasttime = cmtime;
        this.msgcount = msgcount;
    }

    public String getChroom_idx() {
        return chroom_idx;
    }

    public void setChroom_idx(String chroom_idx) {
        this.chroom_idx = chroom_idx;
    }

    public String getChroom_name() {
        return chroom_name;
    }

    public void setChroom_name(String chroom_name) {
        this.chroom_name = chroom_name;
    }

    public String getChroom_img() {
        return chroom_img;
    }

    public void setChroom_img(String chroom_img) {
        this.chroom_img = chroom_img;
    }

    public String getChroom_lastwriter() {
        return chroom_lastwriter;
    }

    public void setChroom_lastwriter(String chroom_lastwriter) {
        this.chroom_lastwriter = chroom_lastwriter;
    }

    public String getChroom_lasttext() {
        return chroom_lasttext;
    }

    public void setChroom_lasttext(String chroom_lasttext) {
        this.chroom_lasttext = chroom_lasttext;
    }

    public String getChroom_lasttime() {
        return chroom_lasttime;
    }

    public void setChroom_lasttime(String chroom_lasttime) {
        this.chroom_lasttime = chroom_lasttime;
    }

    public String getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(String msgcount) {
        this.msgcount = msgcount;
    }
}
