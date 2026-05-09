import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    let appGraph: AppGraph

    init() {
        self.appGraph = IOSAppGraphKt.createAppGraph()
    }
    var body: some Scene {
        WindowGroup {
            ContentView(appGraph: appGraph)
                .onOpenURL { url in
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}
