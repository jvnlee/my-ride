# My Ride

운전자(Driver)와 탑승자(Rider) 매칭 플랫폼 서버

> 서울에 살다보니 당연하게 느껴졌던 카카오 택시와 우버 같은 서비스 외에도, 일부 지방에서는 지역 전용 택시 매칭 서비스가 함께 운영되고 있습니다. 우연히 고향에 내려갔다가 사용해본 지역 자체 서비스가 생각보다 너무 느리고 불편하다고 느껴 직접 만들어보기로 했습니다.

탑승자(Rider)가 여정(Trip)을 요청하기부터 목적지까지 도착하여 여정을 마치기까지의 플로우를 중점적으로 구현

> 실시간성을 위해 WebSocket 기반 STOMP 통신을, 위치 기반 서비스를 위해 Redis GeoSpatial 관련 기능을 적극 활용

&nbsp;

# Tech Stacks

- Java 17

- Spring Boot 3.4.1, Spring Data JPA, Spring WebSocket

- MySQL, Redis

- Docker

&nbsp;

# ERD

![erd](https://github.com/user-attachments/assets/d9f3b1ee-7300-4317-9caf-e78e780f46e0)