package com.lovebird.enums

enum class ReturnCode(val code: String, val message: String) {
	// 성공 Response
	SUCCESS("1000", "요청에 성공하셨습니다."),

	// 토큰 관련
	NOT_EXIST_BEARER_SUFFIX("1100", "Bearer 접두사가 포함되지 않았습니다."),
	WRONG_JWT_TOKEN("1101", "잘못된 jwt 토큰입니다."),

	// 어드민 - 유저 관련
	WRONG_PROVIDER("1200", "잘못된 인증 제공자 입니다."),
	DUPLICATE_SIGN_UP("1201", "중복된 회원가입 입니다."),
	NOT_EXIST_USER("1202", "존재하지 않는 회원 입니다."),

	// 클라이언트 에러
	WRONG_PARAMETER("8000", "잘못된 파라미터 입니다."),

	// 서버 에러
	INTERNAL_SERVER_ERROR("9998", "내부 서버 에러 입니다."),
	EXTERNAL_SERVER_ERROR("9999", "외부 서버 에러 입니다."),
}
