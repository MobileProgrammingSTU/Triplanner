package com.seoultech.triplanner.Model;

/*
   사용자 계정 정보 모델 클래스
 */
public class UserAccount {

    private String emailId;     // email 아이디
    private String passwd;      // 비밀번호
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String imageurl;

    // Firebase 의 경우 빈 생성자를 만들지 않으면 데이터를 조회할 때 오류가 난다고 한다.
    public UserAccount() {}

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
