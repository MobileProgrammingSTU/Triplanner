package com.seoultech.triplanner.Model;

/*
    앞으로 우리가 사용할 데이터의 형식입니다.
    장소를 하나 추가할때 마다 아래의 정보가 필요합니다
    배너 하나에 이 정보들을 담고 있다고 생각하면 됩니다
    img :배너(장소) 이미지 경로 정보, 지금은 (int)R.drawable.filename 으로 로컬경로를 받습니다
    title : 배너(장소)의 타이틀 정보, 게시물과 연동시, 게시물 제목 혹은 위치 정보가 들어갈수 있습니다
            (위치정보는 나중에 부제목 속성을 추가할 수 도 있습니다.)
    type : 배너(장소)의 타입 정보. 장소의 타입(att, rest, cafe) 3가지 중 하나 입니다. 배너의 태그에 적용됩니다.

    !!!!!!!!!!!!!!!!!해당 파일은 PostItem 으로 대체합니다!!!!!!!!!!!!!!!!!!!!!!!!!
*/

public class PlaceBannerItem {

    private int pbThumbnail;       // 게시글의 메인(보이는) 이미지
    private Integer [] pbImgRes;    // 게시글의 서브 이미지
    private String pbMainTitle;     // 메인 제목
    private String pbSubTitle;      // 서브 제목
    private String pbType;          // 타입: att, cafe, ...
    private String pbUserName;
    private String pbBearing;   // 방위: 북 or 남
    private String pbContent;
    private String pbAddress;   // 주소. 해당 기능은 우선 보류

    public PlaceBannerItem(int pbThumbnail, Integer[] pbImgRes, String pbMainTitle,
                           String pbSubTitle, String pbType, String pbUserName,
                           String pbBearing, String pbContent) {
        this.pbThumbnail = pbThumbnail;
        this.pbImgRes = pbImgRes;
        this.pbMainTitle = pbMainTitle;
        this.pbSubTitle = pbSubTitle;
        this.pbType = pbType;
        this.pbUserName = pbUserName;
        this.pbBearing = pbBearing;
        this.pbContent = pbContent;
    }

    public int getPbThumbnail() {
        return pbThumbnail;
    }

    public void setPbThumbnail(int pbThumbnail) {
        this.pbThumbnail = pbThumbnail;
    }

    public Integer[] getPbImgRes() {
        return pbImgRes;
    }

    public void setPbImgRes(Integer[] pbImgRes) {
        this.pbImgRes = pbImgRes;
    }

    public String getPbMainTitle() {
        return pbMainTitle;
    }

    public void setPbMainTitle(String pbMainTitle) {
        this.pbMainTitle = pbMainTitle;
    }

    public String getPbSubTitle() {
        return pbSubTitle;
    }

    public void setPbSubTitle(String pbSubTitle) {
        this.pbSubTitle = pbSubTitle;
    }

    public String getPbType() {
        return pbType;
    }

    public void setPbType(String pbType) {
        this.pbType = pbType;
    }

    public String getPbUserName() {
        return pbUserName;
    }

    public void setPbUserName(String pbUserName) {
        this.pbUserName = pbUserName;
    }

    public String getPbBearing() {
        return pbBearing;
    }

    public void setPbBearing(String pbBearing) {
        this.pbBearing = pbBearing;
    }

    public String getPbContent() {
        return pbContent;
    }

    public void setPbContent(String pbContent) {
        this.pbContent = pbContent;
    }

    public String getPbAddress() {
        return pbAddress;
    }

    public void setPbAddress(String pbAddress) {
        this.pbAddress = pbAddress;
    }
}
