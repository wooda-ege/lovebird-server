package com.lovebird.api.service.couple

import com.lovebird.api.dto.param.couple.CoupleLinkParam
import com.lovebird.api.dto.response.couple.CoupleCodeResponse
import com.lovebird.api.dto.response.couple.CoupleLinkResponse
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleCodeReader
import com.lovebird.domain.repository.writer.CoupleCodeWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleCodeService(
	private val coupleService: CoupleService,
	private val coupleCodeWriter: CoupleCodeWriter,
	private val coupleCodeReader: CoupleCodeReader
) {

	private val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz"

	@Transactional
	fun generateCodeIfNotExist(user: User): CoupleCodeResponse {
		val coupleCode = findByUser(user) ?: save(user, generateCode())

		return CoupleCodeResponse(coupleCode.code, coupleCode.getRemainSeconds())
	}

	@Transactional
	fun linkCouple(param: CoupleLinkParam): CoupleLinkResponse {
		val coupleCode = findByUser(param.user) ?: throw LbException(ReturnCode.WRONG_PARAMETER)
		coupleService.saveAll(param.user, coupleCode.user)

		return CoupleLinkResponse(coupleCode.id!!)
	}

	private fun save(user: User, code: String): CoupleCode {
		return coupleCodeWriter.save(CoupleCode(user, code))
	}

	private fun delete(coupleCode: CoupleCode) {
		coupleCodeWriter.delete(coupleCode)
	}

	private fun generateCode(): String {
		var builder: StringBuilder

		do {
			builder = StringBuilder(8)

			for (i in 0..7) {
				val character = (Math.random() * ALPHA_NUMERIC_STRING.length).toInt()
				builder.append(ALPHA_NUMERIC_STRING[character])
			}
		} while (!existsByCode(builder.toString()))

		return builder.toString()
	}

	private fun findByUser(user: User): CoupleCode? {
		return coupleCodeReader.findByUser(user)?.let {
			if (it.isExpired()) {
				delete(it)
				null
			} else {
				it
			}
		}
	}

	private fun existsByCode(code: String): Boolean {
		return coupleCodeReader.existsByCode(code)
	}
}
