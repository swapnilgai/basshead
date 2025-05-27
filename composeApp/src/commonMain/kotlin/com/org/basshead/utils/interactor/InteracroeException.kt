package com.org.basshead.utils.interactor

import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.error_unknown
import com.org.basshead.utils.core.UiText
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.exceptions.UnknownRestException

sealed class InteractorException(open val msg: UiText) : Exception() {
    data class RequestTimeout(override val msg: UiText) : InteractorException(msg)
    data class Unauthorized(override val msg: UiText) : InteractorException(msg)
    data class BadRequestRest(override val msg: UiText) : InteractorException(msg)
    data class ServerError(override val msg: UiText) : InteractorException(msg)
    data class Unknown(override val msg: UiText) : InteractorException(msg)
    data class HttpRequestError(override val msg: UiText) : InteractorException(msg)
    data class NotFound(override val msg: UiText) : InteractorException(msg)
}

fun Exception.toInteractorException(): InteractorException {
    return when (this) {
        is HttpRequestException -> InteractorException.HttpRequestError(UiText.StringResourceId(Res.string.error_unknown))
        is UnauthorizedRestException -> InteractorException.Unauthorized(UiText.StringResourceId(Res.string.error_unknown))
        is BadRequestRestException -> InteractorException.BadRequestRest(UiText.StringResourceId(Res.string.error_unknown))
        is NotFoundRestException -> InteractorException.NotFound(UiText.StringResourceId(Res.string.error_unknown))
        is UnknownRestException -> InteractorException.Unknown(UiText.StringResourceId(Res.string.error_unknown))
        is SupabaseEncodingException -> InteractorException.ServerError(UiText.StringResourceId(Res.string.error_unknown))
        else -> InteractorException.Unknown(UiText.StringResourceId(Res.string.error_unknown))
    }
}
