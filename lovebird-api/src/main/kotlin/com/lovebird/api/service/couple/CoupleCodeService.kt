package com.lovebird.api.service.couple

import com.lovebird.api.dto.param.couple.CoupleLinkParam
import com.lovebird.api.dto.response.couple.CoupleCodeResponse
import com.lovebird.api.dto.response.couple.CoupleLinkResponse
import com.lovebird.common.enums.ReturnCode
import com.lovebird.common.exception.LbException
import com.lovebird.common.util.RandomUtils.generateCode
import com.lovebird.domain.entity.CoupleCode
import com.lovebird.domain.entity.User
import com.lovebird.domain.repository.reader.CoupleCodeReader
import com.lovebird.domain.repository.writer.CoupleCodeWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleCodeService(
	private val coupleService: CoupleService,
	private val coupleCodeWriter: CoupleCodeWriter,
	private val coupleCodeReader: CoupleCodeReader,
	@Value("\${apple.test-code}")
	private val appleTestCode: String
) {

	@Transactional
	fun generateCodeIfNotExist(user: User): CoupleCodeResponse {
		val coupleCode: CoupleCode = findByUser(user) ?: save(user)
		return CoupleCodeResponse(coupleCode.code, coupleCode.getRemainSeconds())
	}

	@Transactional
	fun linkCouple(param: CoupleLinkParam): CoupleLinkResponse {
		if (param.coupleCode == appleTestCode) {
			return linkForAppleTest(param.user)
		}

		val coupleCode: CoupleCode = findByCode(param.coupleCode)
		coupleService.saveAllWithValidation(param.user, coupleCode.user)

		return CoupleLinkResponse(coupleCode.user.id!!)
	}

	@Transactional
	fun deleteByUser(user: User) {
		findByUser(user)?.let { delete(it) }
	}

	private fun linkForAppleTest(user: User): CoupleLinkResponse {
		coupleService.saveAll(user, user)
		return CoupleLinkResponse(user.id!!)
	}

	private fun getCode(): String {
		var code: String

		do {
			code = generateCode()
		} while (existsByCode(code))

		return code
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

	private fun findByCode(code: String): CoupleCode {
		return coupleCodeReader.findByCode(code).let {
			if (it.isExpired()) {
				delete(it)
				throw LbException(ReturnCode.WRONG_COUPLE_CODE)
			}
			it
		}
	}

	private fun save(user: User): CoupleCode {
		return coupleCodeWriter.save(CoupleCode(user, getCode()))
	}

	private fun delete(coupleCode: CoupleCode) {
		coupleCodeWriter.delete(coupleCode)
	}

	private fun existsByCode(code: String): Boolean {
		return coupleCodeReader.existsByCode(code)
	}
}
