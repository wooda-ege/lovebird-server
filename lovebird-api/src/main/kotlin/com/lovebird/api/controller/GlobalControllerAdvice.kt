package com.lovebird.api.controller

import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.common.response.ApiResponse
import org.springframework.http.converter.HttpMessageNotReadableException
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
	fun handleLbException(e: LbException): ApiResponse<Void> {
		return ApiResponse.fail(e.getReturnCode())
	}

	@ExceptionHandler(
		value = [
			HttpMessageNotReadableException::class,
			MethodArgumentNotValidException::class,
			HttpRequestMethodNotSupportedException::class,
			MissingServletRequestParameterException::class,
			MethodArgumentTypeMismatchException::class
		]
	)
	fun handleRequestException(e: Exception): ApiResponse<Void> {
		return ApiResponse.fail(ReturnCode.WRONG_PARAMETER)
	}

	@ExceptionHandler(
		value = [SQLException::class]
	)
	fun handleServerException(e: SQLException): ApiResponse<Void> {
		return ApiResponse.error(ReturnCode.INTERNAL_SERVER_ERROR)
	}
}
