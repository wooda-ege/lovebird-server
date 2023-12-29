# Lovebird - 커플 공유 일기 앱 <a href="https://apps.apple.com/kr/app/lovebird-%EC%BB%A4%ED%94%8C-%EA%B3%B5%EC%9C%A0-%EC%9D%BC%EA%B8%B0-%EC%95%B1/id6462698149"><img src="https://github.com/wooda-ege/lovebird-server/assets/56003992/266fec4b-ad69-4f3c-af5c-5870dedc5c15" align="left" width="100"></a>

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fwooda-ege%2Flovebird-server&count_bg=%2328DBE6&title_bg=%232D3540&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![Github Action](https://github.com/wooda-ege/lovebird-server/actions/workflows/ci-develop.yml/badge.svg)](https://github.com/depromeet/street-drop-server/actions)

<br>

## Download

- #### [App Store Download](https://apps.apple.com/kr/app/lovebird-%EC%BB%A4%ED%94%8C-%EA%B3%B5%EC%9C%A0-%EC%9D%BC%EA%B8%B0-%EC%95%B1/id6462698149)
- #### [Play Store Download (준비중)](https://play.google.com/store/lovebird)

## Introduction

![Introduction](https://github.com/wooda-ege/lovebird-server/assets/56003992/19188376-d3b9-48dc-a83f-9f6d8ed11093)

## Developers

|                                                                                                                                                                                                                                                  komment                                                                                                                                                                                                                                                  |                                                                                                                                                                                                                                                          T-Dragon                                                                                                                                                                                                                                                          |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/56003992?v=4" width="100" height="100" style="border-radius: 50%;"><br/><a href="https://www.linkedin.com/in/hyunseok-ko-326b62254" target="_blank"><img src="https://img.shields.io/badge/Hyunseok Ko-%230077B5.svg?style=for-the-socail&logo=linkedin&logoColor=white"/></a><br/><a href="https://github.com/lcomment" target="_blank"><img src="https://img.shields.io/badge/lcomment-181717?style=for-the-social&logo=github&logoColor=white"/></a> | <img src="https://avatars.githubusercontent.com/u/86272688?v=4" width="100" height="100" style="border-radius: 50%;"><br/><a href="https://www.linkedin.com/in/%ED%83%9C%EC%9A%A9-%EA%B9%80-76a31228a" target="_blank"><img src="https://img.shields.io/badge/Taeyong Kim-%230077B5.svg?style=for-the-social&logo=linkedin&logoColor=white"/></a><br/><a href="https://github.com/YongsHub" target="_blank"><img src="https://img.shields.io/badge/YongsHub-181717?style=for-the-social&logo=github&logoColor=white"/></a> |


## Trouble Shooting

- #### [(2023.12.29) Kotlin DSL과 Kotest로 Rest Docs 리팩토링 하기](https://komment.dev/posts/(Kotlin)-Kotlin-DSL%EA%B3%BC-Kotest%EB%A1%9C-Rest-Docs-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-%ED%95%98%EA%B8%B0/)
- #### [(2023.12.15) Query Plan 분석을 통해 적절한 인덱스를 생성하자](https://komment.dev/posts/(Spring-Boot)-Query-Plan-%EB%B6%84%EC%84%9D%EC%9D%84-%ED%86%B5%ED%95%B4-%EC%A0%81%EC%A0%88%ED%95%9C-%EC%9D%B8%EB%8D%B1%EC%8A%A4%EB%A5%BC-%EC%83%9D%EC%84%B1%ED%95%98%EC%9E%90/)

## Directory Structure

```
├── .github
├── lovebird-api
├── lovebird-common
├── lovebird-domain
├── lovebird-external 
│   ├── fcm
│   ├── s3
│   └── web-client
├── lovebird-infra
│   ├── logging
│   └── monitoring
└── lovebird-security
```

[//]: # (## Multi Module Structure)

## Tech Stack

- #### Language
    - zulu-openjdk:19.0.2
    - Kotlin 1.9.10
- #### Framework
    - Spring Boot 3.1.5
    - Gradle 8.4.0
- #### ORM
    - Spring Data JPA

- #### Authorization
    - Spring Security
    - OIDC, OAuth2.0
    - JWT

- #### Test
  - Kotest 5.8.0
  - MockK 1.13.8
  - Spring Rest Docs 3.3.2

- #### Database
    - PostgreSQL 14

- #### AWS
  - EC2 (Ubuntu 20.04)
  - S3
  - RDS (PostgreSQL 14)
  - CodeDeploy
  - Route53

- #### ETC
  - Docker
  - Nginx
  - certbot
  - Firebase Cloud Messaging
