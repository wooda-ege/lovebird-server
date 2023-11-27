package com.lovebird.common.response

import com.lovebird.enums.ReturnCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

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

		fun <T> created(data: T? = null): ResponseEntity<ApiResponse<T>> {
			return ResponseEntity.status(HttpStatus.CREATED).body(success(data))
		}

		fun <T> fail(returnCode: ReturnCode): ApiResponse<T> {
			return ApiResponse(returnCode.code, returnCode.message, null)
		}
	}
}
