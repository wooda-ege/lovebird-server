package com.lovebird.common.util

import com.lovebird.common.util.RandomUtils.generateTimeBasedUUID

object FilenameUtils {

	fun generateProfileImageName(imageName: String, providerId: String): String {
		return "%s-profile%s".format(providerId, getFileExtension(imageName))
	}

	fun generateDiaryImageNames(imageNames: List<String>, userId: Long): List<String> {
		val diaryUUID: String = generateTimeBasedUUID()
		var i = 1
		return imageNames.map { "%d_%s-%d%s".format(userId, diaryUUID, i++, getFileExtension(it)) }
	}

	private fun getFileExtension(originalFileName: String): String {
		return "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
	}
}
