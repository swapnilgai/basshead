package com.org.basshead.utils.interactor

import androidx.compose.runtime.Composable
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.error_unknown
import com.org.basshead.utils.core.UiText
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.exceptions.UnknownRestException
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.stringResource

sealed class InteractorException : Exception() {
    data class RequestTimeout(val msg: UiText) : InteractorException()
    data class Unauthorized(val msg: UiText) : InteractorException()
    data class BadRequestRest(val msg: UiText) : InteractorException()
    data class ServerError(val msg: UiText) : InteractorException()
    data class Unknown(val msg: UiText) : InteractorException()
    data class HttpRequestError(val msg: UiText) : InteractorException()
    data class NotFound(val msg: UiText) : InteractorException()
}

fun Exception.toInteractorException(): InteractorException {
    return when(this) {
        is HttpRequestException -> InteractorException.HttpRequestError(UiText.StringResourceId(Res.string.error_unknown))
        is UnauthorizedRestException -> InteractorException.Unauthorized(UiText.StringResourceId(Res.string.error_unknown))
        is BadRequestRestException -> InteractorException.BadRequestRest(UiText.StringResourceId(Res.string.error_unknown))
        is NotFoundRestException -> InteractorException.NotFound(UiText.StringResourceId(Res.string.error_unknown))
        is UnknownRestException -> InteractorException.Unknown(UiText.StringResourceId(Res.string.error_unknown))
        is SupabaseEncodingException -> InteractorException.ServerError(UiText.StringResourceId(Res.string.error_unknown))
        else -> InteractorException.Unknown(UiText.StringResourceId(Res.string.error_unknown))
    }
}
