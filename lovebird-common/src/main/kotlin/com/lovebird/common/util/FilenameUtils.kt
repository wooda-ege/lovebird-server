package com.lovebird.common.util

import com.lovebird.common.util.RandomUtils.generateTimeBasedUUID

object FilenameUtils {

	fun generateProfileImageName(imageName: String): String {
		val uuid: String = generateTimeBasedUUID()
		return "%s-profile%s".format(uuid, getFileExtension(imageName))
	}

	fun generateDiaryImageNames(imageNames: List<String>, userId: Long): List<String> {
		val uuid: String = generateTimeBasedUUID()
		var i = 1
		return imageNames.map { "%d-%s-%d%s".format(userId, uuid, i++, getFileExtension(it)) }
	}

	private fun getFileExtension(originalFileName: String): String {
		return "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
	}
}
