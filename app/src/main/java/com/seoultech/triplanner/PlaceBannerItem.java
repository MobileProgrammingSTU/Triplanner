package com.seoultech.triplanner;

public class PlaceBannerItem {
    private int imgRes;
    private String titleStr;
    private String typeStr;

    public PlaceBannerItem(int img, String title, String type) {
        this.imgRes = img;
        this.titleStr = title;
        this.typeStr = type;
    }

    public int getImg() {
        return this.imgRes;
    }
    public String getTitle() {
        return this.titleStr;
    }
    public String getType() {
        return this.typeStr;
    }

    public void setImg(int img) {
        imgRes = img;
    }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setType(String type) {
        typeStr = type;
    }
}
