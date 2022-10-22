package com.example.RG.post;

public class GrPostData {


    private String grpidx,gr_idx,grpimage, grpdisc, grpwriter,grpcreated_at,grvideourl;


    public GrPostData(String grpidx, String gr_idx, String grpimage, String grpdisc, String grpwriter, String grpcreated_at, String grvideourl) {
        this.grpidx = grpidx;
        this.gr_idx = gr_idx;
        this.grpimage = grpimage;
        this.grpdisc = grpdisc;
        this.grpwriter = grpwriter;
        this.grpcreated_at = grpcreated_at;
        this.grvideourl = grvideourl;
    }

    public String getGrpidx() {
        return grpidx;
    }

    public void setGrpidx(String grpidx) {
        this.grpidx = grpidx;
    }

    public String getGr_idx() {
        return gr_idx;
    }

    public void setGr_idx(String gr_idx) {
        this.gr_idx = gr_idx;
    }

    public String getGrpimage() {
        return grpimage;
    }

    public void setGrpimage(String grpimage) {
        this.grpimage = grpimage;
    }

    public String getGrpdisc() {
        return grpdisc;
    }

    public void setGrpdisc(String grpdisc) {
        this.grpdisc = grpdisc;
    }

    public String getGrpwriter() {
        return grpwriter;
    }

    public void setGrpwriter(String grpwriter) {
        this.grpwriter = grpwriter;
    }

    public String getGrpcreated_at() {
        return grpcreated_at;
    }

    public void setGrpcreated_at(String grpcreated_at) {
        this.grpcreated_at = grpcreated_at;
    }

    public String getGrvideourl() {
        return grvideourl;
    }

    public void setGrvideourl(String grvideourl) {
        this.grvideourl = grvideourl;
    }
}
