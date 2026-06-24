package com.yudzeen.gweatherexercise.repository.auth

open class AuthException(message: String): Exception(message)

class UserNotFoundException(message: String): AuthException(message)
class UserAlreadyExistsException(message: String): AuthException(message)
class InvalidEmailException(message: String): AuthException(message)
class InvalidPasswordException(message: String): AuthException(message)
class InvalidConfirmPasswordException(message: String): AuthException(message)