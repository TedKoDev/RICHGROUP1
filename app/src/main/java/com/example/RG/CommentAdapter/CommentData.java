package com.example.RG.CommentAdapter;

public class CommentData {

    private String content, writer_idx, commentidx,write_at ;

    public CommentData(String content, String writer_idx, String commentidx, String write_at) {
        this.content = content;
        this.writer_idx = writer_idx;
        this.commentidx = commentidx;
        this.write_at = write_at;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter_idx() {
        return writer_idx;
    }

    public void setWriter_idx(String writer_idx) {
        this.writer_idx = writer_idx;
    }

    public String getCommentidx() {
        return commentidx;
    }

    public void setCommentidx(String commentidx) {
        this.commentidx = commentidx;
    }

    public String getWrite_at() {
        return write_at;
    }

    public void setWrite_at(String write_at) {
        this.write_at = write_at;
    }
}
