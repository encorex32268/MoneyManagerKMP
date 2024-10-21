import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    
    init() {
        MainViewControllerKt.IOSBanner = { adUnitId -> UIViewController in
                    let adBannerView = VStack {
                        BannerAdView(adUnitID: adUnitId) // 使用傳入的 adUnitId
                    }
                    return UIHostingController(rootView: adBannerView)
                }
       }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }

}



