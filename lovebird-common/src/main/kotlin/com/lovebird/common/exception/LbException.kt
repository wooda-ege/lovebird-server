package com.lovebird.common.exception

import com.lovebird.common.enums.ReturnCode

class LbException(returnCode: ReturnCode) : RuntimeException() {

	private val code: String
	private val msg: String
	private val returnCode: ReturnCode

	init {
		this.returnCode = returnCode
		this.code = returnCode.code
		this.msg = returnCode.message
	}

	fun getReturnCode(): ReturnCode = this.returnCode

	fun getCode(): String = this.code

	fun getMsg(): String = this.msg
}
