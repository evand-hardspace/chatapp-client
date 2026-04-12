package com.evandhardspace.core.data.networking

import com.evandhardspace.client.core.data.BuildKonfig

actual object UrlConstants {
    actual val BaseUrlHttp: String = BuildKonfig.BASE_URL
    actual val BaseUrlWS: String = BuildKonfig.BASE_URL_WEB_SOCKET
}
