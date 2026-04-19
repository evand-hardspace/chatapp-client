package com.evandhardspace.core.data.networking

import okhttp3.Interceptor
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

internal class LoopbackHostRewriteInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = normalizeRequestBody(chain.request())
        val response = chain.proceed(request)
        val body = response.body

        val contentType = body.contentType()
        if (contentType.isJsonOrText().not()) return response

        val rawBody = body.string()
        val normalizedBody = platformNormalizeLoopbackHostsInResponsePayload(rawBody)

        return response.newBuilder()
            .body(normalizedBody.toResponseBody(contentType))
            .build()
    }

    private fun normalizeRequestBody(request: okhttp3.Request): okhttp3.Request {
        val requestBody = request.body ?: return request

        val normalizedRequestBody = when (requestBody) {
            is MultipartBody -> normalizeMultipartBody(requestBody)
            is FormBody -> normalizeFormBody(requestBody)
            else -> normalizeTextBody(requestBody)
        }

        if (normalizedRequestBody === requestBody) return request

        return request.newBuilder()
            .method(request.method, normalizedRequestBody)
            .build()
    }

    private fun normalizeTextBody(requestBody: RequestBody): RequestBody {
        val contentType = requestBody.contentType()
        if (contentType.isJsonOrText().not()) return requestBody

        val buffer = Buffer().also { requestBody.writeTo(it) }
        val rawBody = buffer.readUtf8()
        val normalizedBody = platformNormalizeLoopbackHostsInRequestPayload(rawBody)
        if (normalizedBody == rawBody && requestBody.isOneShot().not()) return requestBody

        return normalizedBody.toRequestBody(contentType)
    }

    private fun normalizeFormBody(requestBody: FormBody): RequestBody {
        var isChanged = false
        val normalizedFormBody = FormBody.Builder()
        for (index in 0 until requestBody.size) {
            val encodedName = requestBody.encodedName(index)
            val encodedValue = requestBody.encodedValue(index)
            val normalizedValue = platformNormalizeLoopbackHostsInRequestPayload(encodedValue)
            if (normalizedValue != encodedValue) {
                isChanged = true
            }
            normalizedFormBody.addEncoded(encodedName, normalizedValue)
        }

        return if (isChanged) normalizedFormBody.build() else requestBody
    }

    private fun normalizeMultipartBody(requestBody: MultipartBody): RequestBody {
        var isChanged = false
        val normalizedMultipart = MultipartBody.Builder().setType(requestBody.type)

        requestBody.parts.forEach { part ->
            val normalizedPartBody = normalizeTextBody(part.body)
            if (normalizedPartBody !== part.body) {
                isChanged = true
            }
            normalizedMultipart.addPart(part.headers, normalizedPartBody)
        }

        return if (isChanged) normalizedMultipart.build() else requestBody
    }

    private fun MediaType?.isJsonOrText(): Boolean {
        val isJson = this?.subtype?.contains("json", ignoreCase = true) == true
        val isText = this?.type.equals("text", ignoreCase = true)
        return isJson || isText
    }
}
