import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {

    let appGraph: AppGraph

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            metroViewModelFactory: appGraph.dependencies.metroViewModelFactory,
            deeplinkManager: appGraph.dependencies.deeplinkProcessor,
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let appGraph: AppGraph

    var body: some View {
        ComposeView(appGraph: appGraph)
            .ignoresSafeArea()
    }
}



