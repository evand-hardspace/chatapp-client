package com.evandhardspace.core.data.networking

import com.evandhardspace.client.core.data.BuildKonfig
import io.ktor.http.Url

internal fun platformNormalizeLoopbackHostsInResponsePayload(rawPayload: String): String {
    if (BuildKonfig.FLAVOR != "local") return rawPayload

    val targetHost = UrlConstants
        .BaseUrlHttp
        .runCatching { Url(this) }.getOrNull()?.host ?: return rawPayload

    return rawPayload
        .replace("://localhost", "://$targetHost")
        .replace("://127.0.0.1", "://$targetHost")
        .replace("://[::1]", "://$targetHost")
        .replace("://::1", "://$targetHost")
}

internal fun platformNormalizeLoopbackHostsInRequestPayload(rawPayload: String): String {
    if (BuildKonfig.FLAVOR != "local") return rawPayload

    return rawPayload
        .replace("://10.0.2.2", "://localhost")
        .replace("://127.0.0.1", "://localhost")
        .replace("://[::1]", "://localhost")
        .replace("://::1", "://localhost")
}

