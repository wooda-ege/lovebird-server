package com.lovebird.common.utils

import org.springframework.stereotype.Component

@Component
class FilenameFormatter {

	fun generateProfileImageName(filename: String, memberId: String): String {
		return memberId + "-profile" + getFileExtension(filename)
	}

	fun generateDiaryImageNames(imageNames: List<String>, memberId: String, diaryId: String): List<String> {
		var i = 1
		return imageNames.map { "%s_%s-%d%s".format(memberId, diaryId, i++, getFileExtension(it)) }
	}

	private fun getFileExtension(originalFileName: String): String {
		return "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
	}
}
