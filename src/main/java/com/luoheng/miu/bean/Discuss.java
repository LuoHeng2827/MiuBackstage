package com.luoheng.miu.bean;

import java.util.Date;

public class Discuss {
    private String id;
    private String authorMail;
    private String content;
    private Date createDate;
    private int likeCount;
    private DiscussImage discussPicture;

    public Discuss(){

    }

    public Discuss(String authorMail, String content, Date createDate){
        this.authorMail = authorMail;
        this.content=content;
        this.createDate=createDate;
        this.likeCount=0;
    }

    public Discuss(String authorMail, String content, Date createDate, int likeCount){
        this(authorMail, content, createDate);
        this.likeCount=likeCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorMail() {
        return authorMail;
    }

    public void setAuthorMail(String authorMail) {
        this.authorMail = authorMail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public DiscussImage getDiscussPicture() {
        return discussPicture;
    }

    public void setDiscussPicture(DiscussImage discussPicture) {
        this.discussPicture = discussPicture;
    }
}
