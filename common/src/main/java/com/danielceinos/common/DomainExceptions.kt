package com.danielceinos.common

open class AppException

class NetworkException(val errCode: Int): AppException()

