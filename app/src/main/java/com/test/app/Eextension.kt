package com.test.app

import android.content.Context
import android.widget.Toast
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.NumberFormat
import java.util.*

fun Throwable.printErrorStackTrace(context: Context) {
    Toast.makeText(context, this.message, Toast.LENGTH_LONG).show()
}

fun Throwable.printErrorStackTrace(
    context: Context,
    onApiBadRequestException: ((errorBody: String) -> Unit)? = null,
    onApiInternalServerErrorException: ((message: String) -> Unit)? = null,
    onNetworkErrorException: (() -> Unit)? = null
) = printErrorStackTrace(
    onApiBadRequestException = onApiBadRequestException,
    onApiInternalServerErrorException = {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        onApiInternalServerErrorException?.invoke(it)
    },
    onNetworkErrorException = {
        Toast.makeText(context, "Lost Connection", Toast.LENGTH_LONG).show()
        onNetworkErrorException?.invoke()
    }
)

private fun Throwable.printErrorStackTrace(
    onApiBadRequestException: ((errorBody: String) -> Unit)? = null,
    onApiInternalServerErrorException: ((message: String) -> Unit)? = null,
    onNetworkErrorException: (() -> Unit)? = null
) {
    Timber.e(this, this.message)
    if (this is HttpException) {
        if (this.code() == 400) {
            onApiBadRequestException?.invoke(this.response()?.errorBody()?.string() ?: "")
        } else {
            onApiInternalServerErrorException?.invoke(this.message())
        }
    } else if (
        arrayListOf(
            SocketTimeoutException::class,
            ConnectException::class,
            UnknownHostException::class
        ).contains(this::class)
    ) {
        onNetworkErrorException?.invoke()
    }
}

fun String.toString(default: String): String {
    return if (this.isNotBlank()) this else default
}

fun Float?.toPrice(): String {
    return StringFormat.CURRENCY.format(this ?: 0)
}

fun Float?.toNumber(): String {
    return StringFormat.NUMBER.format(this ?: 0)
}

fun Double?.toPrice(): String {
    return StringFormat.CURRENCY.format(this ?: 0)
}

fun Double?.toNumber(): String {
    return StringFormat.NUMBER.format(this ?: 0)
}

object StringFormat {
    val CURRENCY: NumberFormat =
        NumberFormat.getCurrencyInstance()
            .also {
                it.maximumFractionDigits = 1
//                it.isGroupingUsed = false
                it.currency = Currency.getInstance("egp")
//                it.currency = Currency.getInstance(ContextWrapper.CURRENT)
            }

    val NUMBER: NumberFormat = NumberFormat.getNumberInstance()
}
