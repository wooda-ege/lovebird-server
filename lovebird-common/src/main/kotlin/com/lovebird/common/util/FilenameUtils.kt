package com.lovebird.common.util

object FilenameUtils {

	fun generateProfileImageName(filename: String, userId: Long): String {
		return "%d-profile%s".format(userId, getFileExtension(filename))
	}

	fun generateDiaryImageNames(imageNames: List<String>, userId: Long, diaryId: Long): List<String> {
		var i = 1
		return imageNames.map { "%d_%d-%d%s".format(userId, diaryId, i++, getFileExtension(it)) }
	}

	private fun getFileExtension(originalFileName: String): String {
		return "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
	}
}
