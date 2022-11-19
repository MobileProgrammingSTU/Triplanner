package com.seoultech.triplanner.Model;

/*
!주의 사항!
Firebase의 키는 항상 소문자로만 작성한다!!!
해당 아이템 클래스의 프로퍼티와 DB의 키를 매칭시키려면
프로퍼티명이 키의 이름을 포함해야한다
 */

public class PostItem {
    private String pid;
    private String imgurl;
    private String title;
    private String fbSubtitle;
    private String fbPublisher;
    private String typeRegion;
    private String typePlace;

    public PostItem() {

    }

    public String getPid() {
        return pid;
    }
    public void setPid(String fbPID) {
        this.pid = fbPID;
    }

    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String fbImgurl) {
        this.imgurl = fbImgurl;
    }

    public String getPublisher() {
        return fbPublisher;
    }
    public void setPublisher(String fbPublisher) {
        this.fbPublisher = fbPublisher;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String fbTitle) {
        this.title = fbTitle;
    }

    public String getSubtitle() {
        return fbSubtitle;
    }
    public void setSubtitle(String fbSubtitle) {
        this.fbSubtitle = fbSubtitle;
    }

    public String getTypeRegion() { return typeRegion; }
    public void setTypeRegion(String typeRegion) { this.typeRegion = typeRegion; }

    public String getTypePlace() { return typePlace; }
    public void setTypePlace(String typePlace) { this.typePlace = typePlace; }

}
