package com.org.basshead.utils.interactor

sealed class InteractorException : Exception() {
    data class RequestTimeout(val msg: String) : InteractorException()
    data class Unauthorized(val msg: String) : InteractorException()
    data class BadRequestRest(val msg: String) : InteractorException()
    data class ServerError(val msg: String) : InteractorException()
    data class Unknown(val msg: String) : InteractorException()
}

fun Exception.toInteractorException(): InteractorException {
    return InteractorException.Unknown(this.message!!)
    // TODO add mapping error from supabase/ ktor client
}
