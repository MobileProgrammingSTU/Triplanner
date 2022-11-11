package com.seoultech.triplanner.Model;

public class PostItem {
    private String postid;
    private String postimage;
    private Integer[] imgRes;
    private String titleStr;
    private String subtitleStr;
    private String typeStr;
    private String publisher;

    public PostItem(String postid, String postimage, String title, String subtitle, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.publisher = publisher;
        this.titleStr = title;
        this.subtitleStr = subtitle;
    }

    public PostItem() {
    }

    public String getPostid() {
        return postid;
    }
    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }
    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer[] getImg() {
        return imgRes;
    }
    public void setImg(Integer[] img) {
        this.imgRes = img;
    }

    public String getTitle() {
        return titleStr;
    }
    public void setTitle(String title) {
        this.titleStr = title;
    }

    public String getSubtitle() {
        return subtitleStr;
    }
    public void setSubtitle(String subtitleStr) {
        this.subtitleStr = subtitleStr;
    }

    public String getType() {
        return typeStr;
    }
    public void setType(String type) {
        this.typeStr = type;
    }

}
