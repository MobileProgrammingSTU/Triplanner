package com.seoultech.triplanner.Model;

/*
    앞으로 우리가 사용할 데이터의 형식입니다.
    장소를 하나 추가할때 마다 아래의 정보가 필요합니다
    배너 하나에 이 정보들을 담고 있다고 생각하면 됩니다
    img :배너(장소) 이미지 경로 정보, 지금은 (int)R.drawable.filename 으로 로컬경로를 받습니다
    title : 배너(장소)의 타이틀 정보, 게시물과 연동시, 게시물 제목 혹은 위치 정보가 들어갈수 있습니다
            (위치정보는 나중에 부제목 속성을 추가할 수 도 있습니다.)
    type : 배너(장소)의 타입 정보. 장소의 타입(att, rest, cafe) 3가지 중 하나 입니다. 배너의 태그에 적용됩니다.
*/

public class PlaceBannerItem {
    private Integer[] imgRes;
    private String titleStr;
    private String typeStr;
    private String userName;

    public PlaceBannerItem(Integer[] img, String title, String type) {
        this.imgRes = img;
        this.titleStr = title;
        this.typeStr = type;
    }

    public Integer[] getImg() {
        return this.imgRes;
    }
    public String getTitle() {
        return this.titleStr;
    }
    public String getType() {
        return this.typeStr;
    }

    public void setImg(Integer[] img) {
        imgRes = img;
    }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setType(String type) {
        typeStr = type;
    }
}
