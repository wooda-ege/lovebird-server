package com.lovebird.vo.key

data class PublicKey(
	val kid: String,
	val kty: String,
	val alg: String,
	val use: String,
	val n: String,
	val e: String
)
