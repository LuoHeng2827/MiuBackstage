package com.luoheng.miu.bean;

public class DiscussImage {
    private String discussId;
    private String imageUrl;

    public DiscussImage() {
    }

    public DiscussImage(String discussId, String imageUrl) {
        this.discussId = discussId;
        this.imageUrl = imageUrl;
    }

    public String getDiscussId() {
        return discussId;
    }

    public void setDiscussId(String discussId) {
        this.discussId = discussId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
