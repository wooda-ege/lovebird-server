package com.lovebird.api.provider

import org.springframework.stereotype.Component

@Component
class FilenameProvider {

	fun generateProfileImageName(filename: String, userId: Long): String {
		return "%n-profile".format(userId) + getFileExtension(filename)
	}

	fun generateDiaryImageNames(imageNames: List<String>, userId: Long, diaryId: Long): List<String> {
		var i = 1
		return imageNames.map { "%n_%n-%d%s".format(userId, diaryId, i++, getFileExtension(it)) }
	}

	private fun getFileExtension(originalFileName: String): String {
		return "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
	}
}
