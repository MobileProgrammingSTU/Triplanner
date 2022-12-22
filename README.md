# Triplanner


## 1. 개발 기간

* 2022.10.01 ~ 2022.12.10
* 3학년 2학기 모바일프로그래밍, 팀 프로젝트

<br>


## 2. 앱의 목적 및 사용 대상

### 앱의 목적

* 여행 정보를 공유하는 앱
* 여행을 꿈꾸지만 계획이 없는 사람들을 위한 앱

### 사용 대상

* 서울 지역 여행자
* 2030세대

<br>


## 3. 앱의 주요 기능

포스트 하나가 장소 하나의 의미를 가진다.

1. 방문했던 장소에 대한 포스트(게시글) 작성하기
2. 다른 사용자의 포스트를 보고 :heart: 누르기
3. :heart: 목록으로 나의 플랜(계획) 세우기

<br>


## 4. 시스템 구성

### 페이지 구성

1. 로그인 및 회원가입 페이지
2. 메인 페이지
3. 포스트 페이지
4. 포스트 작성하기 페이지
5. 플랜 만들기 페이지



### 메인 화면

<img width="303" alt="main" src="https://user-images.githubusercontent.com/80478750/209122834-d0816e34-9d9c-4820-8880-2b70c1250bf0.png">



* 홈: 홈 화면
* 포스트: 포스트(게시글) 작성
* 플랜: 나의 플랜 만들기
* 저장소: 포스트/플랜/좋아요
* 프로필: 사용자 프로필







### 시스템 구조

<img width="809" alt="pic1" src="https://user-images.githubusercontent.com/80478750/209159914-f3e3ccec-2145-4f1e-9973-5be95d996611.png">
<img width="805" alt="pic2" src="https://user-images.githubusercontent.com/80478750/209159922-b067c709-a8e9-4082-9db9-d58b6e9f6f16.png">





### Data 구조

#### 1. 포스트 Data 구조

Publisher는 포스트 작성자로, 현재 로그인한 사용자의 Name을 저장한다.

```java
public class PostItem {
    private String pid;
    private String title;
    private String Subtitle;
    private String Publisher;
    private String typeRegion;
    private String typePlace;
    private HashMap<String, String> images;
    private String content;
    private String thumbNail;
    private String planTime; 
}
```



#### 2. 플랜 Data 구조

fbPlacesByDay에 플랜을 만들면서 선택한 포스트(장소) Data를 쌓음

```java
public class PlanItem {
    private String fbPlanID;
    private String fbDateStart; // LocalDate -> String : 20XX-MM-dd
    private String fbDateEnd;
    private String fbPlanTitle;
    private String fbPlanType;
    private String fbThumbnail;
    private HashMap<String, ArrayList<PostItem>> fbPlacesByDay;
}
```



#### 3. 사용자 Data 구조

포스트의 :heart: 를 누르면 Likes 목록이 생성되어 추가

<img width="415" alt="pic3" src="https://user-images.githubusercontent.com/80478750/209161102-1c9f75e8-d829-418b-9229-04c4c138891e.png">





### UI / UX

#### 1. 화면 디자인 변경 과정

* 초기 디자인

<img width="412" alt="des1" src="https://user-images.githubusercontent.com/80478750/209163418-e5f50c54-aae1-45d2-b6ed-bda41843ad1b.png">



* 최종 디자인

<img width="270" alt="des2" src="https://user-images.githubusercontent.com/80478750/209163408-3457e930-8f5e-4de5-859f-1c988f24763e.png">



#### 2. 컬러 팔레트

<img width="287" alt="color" src="https://user-images.githubusercontent.com/80478750/209163673-2b8ba81c-c0e3-442a-a834-97c6e2f5dfe1.png">





#### 3. 폰트

<img width="246" alt="fonts" src="https://user-images.githubusercontent.com/80478750/209163681-8f422b72-5c3c-4629-83f3-80d1ff1f17a4.png">







<br>




## 5. 기타

* [Notion 회의록](https://www.notion.so/2022-458b5ba1e38f48a19cda4b8bfb2e06bf)
* [시연 영상](https://youtu.be/6edYmEHuWvk)



