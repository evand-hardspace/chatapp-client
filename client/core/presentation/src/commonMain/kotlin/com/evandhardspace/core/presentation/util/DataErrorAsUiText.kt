package com.evandhardspace.core.presentation.util

import chatapp.client.core.presentation.generated.resources.Res
import chatapp.client.core.presentation.generated.resources.error_bad_request
import chatapp.client.core.presentation.generated.resources.error_conflict
import chatapp.client.core.presentation.generated.resources.error_disk_full
import chatapp.client.core.presentation.generated.resources.error_forbidden
import chatapp.client.core.presentation.generated.resources.error_no_internet
import chatapp.client.core.presentation.generated.resources.error_not_found
import chatapp.client.core.presentation.generated.resources.error_payload_too_large
import chatapp.client.core.presentation.generated.resources.error_request_timeout
import chatapp.client.core.presentation.generated.resources.error_serialization
import chatapp.client.core.presentation.generated.resources.error_server
import chatapp.client.core.presentation.generated.resources.error_service_unavailable
import chatapp.client.core.presentation.generated.resources.error_too_many_requests
import chatapp.client.core.presentation.generated.resources.error_unable_to_send_message
import chatapp.client.core.presentation.generated.resources.error_unauthorized
import chatapp.client.core.presentation.generated.resources.error_unknown
import com.evandhardspace.core.domain.util.DataError

fun DataError.asUiText(): UiText = when (this) {
    DataError.Local.DiskFull -> Res.string.error_disk_full
    DataError.Local.NotFound -> Res.string.error_not_found
    DataError.Local.Unknown -> Res.string.error_unknown
    DataError.Remote.BadRequest -> Res.string.error_bad_request
    DataError.Remote.RequestTimeout -> Res.string.error_request_timeout
    DataError.Remote.Unauthorized -> Res.string.error_unauthorized
    DataError.Remote.Forbidden -> Res.string.error_forbidden
    DataError.Remote.NotFound -> Res.string.error_not_found
    DataError.Remote.Conflict -> Res.string.error_conflict
    DataError.Remote.TooManyRequests -> Res.string.error_too_many_requests
    DataError.Remote.NoInternet -> Res.string.error_no_internet
    DataError.Remote.PayloadTooLarge -> Res.string.error_payload_too_large
    DataError.Remote.ServerError -> Res.string.error_server
    DataError.Remote.ServiceUnavailable -> Res.string.error_service_unavailable
    DataError.Remote.Serialization -> Res.string.error_serialization
    DataError.Remote.Unknown -> Res.string.error_unknown
    DataError.ConnectionError.NotConnected -> Res.string.error_no_internet
    DataError.ConnectionError.MessageSendFailed -> Res.string.error_unable_to_send_message
}.asUiText()
