package com.seoultech.triplanner.Model;

/*
!주의 사항!
Firebase의 키는 항상 소문자로만 작성한다!!!
해당 아이템 클래스의 프로퍼티와 DB의 키를 매칭시키려면
프로퍼티명이 키의 이름을 포함해야한다
 */

import java.util.ArrayList;
import java.util.HashMap;

public class PostItem {
    private String pid;
    private String imgurl;
    private String title;
    private String Subtitle;
    private String Publisher;
    private String typeRegion;
    private String typePlace;
    private HashMap<String, String> images;
    private String Content;

    public PostItem() {
        images = new HashMap<>();
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
        return Publisher;
    }
    public void setPublisher(String Publisher) {
        this.Publisher = Publisher;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String fbTitle) {
        this.title = fbTitle;
    }

    public String getSubtitle() {
        return Subtitle;
    }
    public void setSubtitle(String Subtitle) {
        this.Subtitle = Subtitle;
    }

    public String getTypeRegion() { return typeRegion; }
    public void setTypeRegion(String typeRegion) { this.typeRegion = typeRegion; }

    public String getTypePlace() { return typePlace; }
    public void setTypePlace(String typePlace) { this.typePlace = typePlace; }

    public HashMap<String, String> getImages() { return images; }
    public void setImages(HashMap<String, String> listImgurl) { this.images = listImgurl; }

    public String getThumbnail() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(this.images.values());
        return list.get(0);
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
}
