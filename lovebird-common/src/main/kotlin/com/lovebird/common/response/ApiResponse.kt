package com.lovebird.common.response

import com.lovebird.common.enums.ReturnCode

data class ApiResponse<T>(
	val code: String,
	val message: String,
	val data: T?
) {

	companion object {
		fun <T> success(data: T? = null): ApiResponse<T> {
			return ApiResponse(
				ReturnCode.SUCCESS.code,
				ReturnCode.SUCCESS.message,
				data
			)
		}

		fun <T> fail(returnCode: ReturnCode, data: T? = null): ApiResponse<T> {
			return ApiResponse(returnCode.code, returnCode.message, data)
		}

		fun <T> error(returnCode: ReturnCode): ApiResponse<T> {
			return ApiResponse(returnCode.code, returnCode.message, null)
		}
	}
}
