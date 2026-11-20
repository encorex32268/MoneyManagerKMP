import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        InitKoinKt.doInitKoin(config: nil)
    }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

	var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea(.keyboard).ignoresSafeArea(.container)
        }
	}
}

