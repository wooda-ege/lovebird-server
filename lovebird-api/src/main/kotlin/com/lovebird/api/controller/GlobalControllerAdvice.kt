package com.lovebird.api.controller

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.sql.SQLException

@RestControllerAdvice
class GlobalControllerAdvice {

	@ExceptionHandler(LbException::class)
	fun handleLbException(e: LbException): ResponseEntity<ApiResponse<Unit>> {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(e.getReturnCode()))
	}

	@ExceptionHandler(
		value = [
			HttpMessageNotReadableException::class,
			HttpRequestMethodNotSupportedException::class,
			MissingServletRequestParameterException::class,
			MethodArgumentTypeMismatchException::class
		]
	)
	fun handleRequestException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(ReturnCode.WRONG_PARAMETER))
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun badRequestExHandler(bindingResult: BindingResult): ResponseEntity<ApiResponse<*>> {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(
				ApiResponse.fail(
					ReturnCode.WRONG_PARAMETER,
					bindingResult.fieldErrors
				)
			)
	}

	@ExceptionHandler(
		value = [SQLException::class]
	)
	fun handleServerException(e: SQLException): ResponseEntity<ApiResponse<Unit>> {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.error(ReturnCode.INTERNAL_SERVER_ERROR))
	}
}
