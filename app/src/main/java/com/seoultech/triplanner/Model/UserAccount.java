package com.seoultech.triplanner.Model;

/*
   사용자 계정 정보 모델 클래스
 */
public class UserAccount {

    private String fbName;      // 닉네임
    private String fbEmail;     // email
    private String fbPassword;    // password
    private String fbIdToken;   // Firebase Uid (고유 토큰 정보)

    // Firebase 의 경우 빈 생성자를 만들지 않으면 데이터를 조회할 때 오류가 난다고 한다.
    public UserAccount() {}

    public String getFbEmail() {
        return fbEmail;
    }

    public void setFbEmail(String fbEmail) {
        this.fbEmail = fbEmail;
    }

    public String getFbPassword() {
        return fbPassword;
    }

    public void setFbPassword(String fbPassword) {
        this.fbPassword = fbPassword;
    }

    public String getFbIdToken() {
        return fbIdToken;
    }

    public void setFbIdToken(String fbIdToken) {
        this.fbIdToken = fbIdToken;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }
}
