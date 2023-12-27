### 📖 프로젝트 소개
>#### 개발 배경
현대 사회에서 운동과 건강한 생활 습관의 중요성이 증가하고 있다. 많은 사람들이 운동 목표를 세우지만, 지속성과 동기 유지에 어려움을 겪고 있다. 이에, 사용자들이 서로의 운동 성과를 공유하고 독려할 수 있는 플랫폼 '오운완'을 개발하게 되었다.
>#### 작품 설명
'오운완'은 사용자가 일일 운동 목표를 설정하고 이를 커뮤니티와 공유할 수 있는 안드로이드 모바일 애플리케이션이다. 이 앱은 사용자 간 상호작용을 통해 서로를 독려하고 건강한 생활 습관을 형성하도록 도와준다.

--------

### ⛰️ 프로젝트 목표
>#### 사용자 중심의 서비스 구조 설계
- 사용자가 운동을 수행하고, 이를 인증하며 커뮤니티와 공유할 수 있는 안드로이드 애플리케이션 개발.
- 직관적이고 사용자 친화적인 인터페이스 제공을 목표로 함.
>#### 안정적인 백엔드 시스템 구축
- Spring Framework를 활용하여 안정적이고 확장 가능한 백엔드 서버 구축.
- 사용자 인증, 데이터 관리 및 실시간 업데이트를 위한 RESTful API 설계 및 구현.
>#### 모바일 앱 개발
- 안드로이드 스튜디오를 이용하여 모바일 앱 개발.
- 사용자의 운동 데이터를 효율적으로 관리하고 표시하는 기능 구현.
>#### 사용자 인증 및 보안 강화
- JWT(Jason Web Tokens)를 이용한 안전한 사용자 인증 및 세션 관리.
- 사용자 데이터 보호 및 애플리케이션 보안을 위한 철저한 보안 조치 구현.
  
--------

### 👨🏻‍💻 기술스택
|Category|Stack|
|------|---|
|클라이언트|Android|
|서버|Spring Framework, Docker|
|데이터베이스|MariaDB|
|클라우드 호스팅|클라우드타입|
|기타|JWT 인증, Retrofit|

--------

### 🗺️ 시스템 구성도 
![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/2d495dc8-fd47-49fa-bbe8-efa825932fa6)

### 🖥 ️주요 기능
<details>
<summary>회원가입 및 로그인</summary>
<div markdown="1">

  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/e0e992c7-f3da-4258-a3dd-b71543f0e6ac)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/afa97eab-9820-4704-870e-73fd9c2dc0c7)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/68eb624b-40e7-4466-8f79-e9b8feb04f1e)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/9d888ca8-00d8-45c3-959e-108d14f95983)

  - 회원가입 및 로그인 프로세스는 JWT를 사용하여 안전하게 인증한다.
  - 사용자는 아이디, 비밀번호 및 사용자 이름으로 계정을 생성할 수 있다.
</div>
</details>

<details>
<summary>운동 인증 게시판</summary>
<div markdown="1">

  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/2e8296bf-9a16-4c4f-be39-174379ffe285)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/48f1c80a-fc66-4cd5-8440-14f4e5e3e187)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/cbf49048-ff10-4e0e-8b35-31e206f24798)

  - 사용자는 운동 인증 게시판을 통해 자신의 운동을 인증하고, 다른 사용자의 인증 게시글을 조회할 수 있다.
  - 게시글 작성 및 삭제 기능이 포함되어 있다.
</div>
</details>

<details>
<summary>댓글 시스템</summary>
<div markdown="1">

  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/adbbc38e-bf8c-49ad-ac8b-bce4c71eb50f)
  ![image](https://github.com/joon6093/Today-Fit-Complete/assets/118044367/e4ffe83e-9c58-4d3c-bd42-eb72a2151239)
  
  - 사용자는 게시글에 댓글을 달아 서로의 운동을 격려할 수 있다.
  - 댓글 작성, 조회 및 삭제 기능이 구현되어 있다.
</div>
</details>

--------

### ✍🏻 프로젝트 후기
&nbsp;이번 '오운완' 프로젝트는 개인적으로 진행한 매우 의미 있는 경험이었다. 혼자서 프로젝트를 수행하며, 주된 목표는 서버-클라이언트 간의 통신을 직접 구현하고 이를 통해 백엔드와 프론트엔드 간의 상호작용을 깊이 있게 이해하는 것이었다.

&nbsp;Spring Framework를 사용한 백엔드 개발과 안드로이드 앱을 통한 프론트엔드 개발 사이의 연결 고리를 이해하기 위해 노력했다. 특히, RESTful API 설계 및 구현 과정에서 서버와 클라이언트 간 데이터 교환의 메커니즘을 실제로 경험하고 학습하는 것이 중요했다. 이 과정은 저에게 네트워크 통신의 기본 원리와 백엔드 시스템의 핵심 기능에 대한 깊은 이해를 제공했다.

&nbsp;또한, JWT 인증의 구현은 이 프로젝트의 중요한 부분이었다. 백엔드에서 안전한 인증 시스템을 구축하고, 프론트엔드에서 이를 효과적으로 처리하는 방법을 탐구했다. 이는 사용자 인증과 데이터 보안에 대한 중요한 학습 기회였으며, 실제 앱 개발에서 보안이 얼마나 중요한지를 몸소 느낄 수 있었다.

&nbsp;프로젝트를 독립적으로 진행하면서, 스스로 문제를 해결하는 능력과 자기주도적인 학습 능력을 키울 수 있었다. 모든 단계를 혼자 계획하고 실행하면서, 프로젝트 관리의 중요성과 동시에 개발자로서의 전문성을 한 단계 더 성장시킬 수 있는 기회였다.

&nbsp;이 경험을 통해, 서버와 클라이언트 간의 통신, 보안 메커니즘의 이해, 그리고 전체적인 애플리케이션의 흐름을 파악하는 데 좋은 경험을 하였으며, 이는 앞으로 백엔드 개발에 큰 도움이 될 것 같다.

--------
### 👀 추가 자료
[최종 보고서 링크](https://github.com/joon6093/Today-Fit-Complete/tree/main/Document)

[피그마 링크](https://www.figma.com/file/xqGYqddGos8ltJRJgDrNxt/%EC%98%A4%EC%9A%B4%EC%99%84?type=design&node-id=0%3A1&mode=design&t=wLypSzfExjMexgRc-1)
