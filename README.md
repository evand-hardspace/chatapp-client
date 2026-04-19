# Chatapp Client

Kotlin Multiplatform chat client.

## Demo
![Application Demo](demo.gif)

## Backend

This client works with the Chatapp API backend:

- https://github.com/evand-hardspace/chatapp-api

## Local Flavor

The project uses `flavor=local` in `gradle.properties` for local development.

- iOS local API: `http://localhost:8080/api`
- Android local API: `http://10.0.2.2:8080/api` (Android emulator host mapping)
- iOS local WS: `ws://localhost:8080/ws`
- Android local WS: `ws://10.0.2.2:8080/ws`

## Local Testing

1. Start backend locally (`chatapp-api`) on port `8080`.
2. Ensure `flavor=local` in `gradle.properties`.
3. Run Android app from `client/composeApp` on emulator.
4. Run iOS app from `iosApp/iosApp.xcodeproj` on simulator.

### Test Deeplinks:

* Android:
```shell
adb <-s device> shell am start -W -a android.intent.action.VIEW -d "<deeplink>" com.evandhardspace.chatapp
```

* iOS:
```shell
xcrun simctl openurl booted "<deeplink>"
```

## TODO

- Add push notifications
- Add desktop target
- Add WASM target
- Optimization