import UIKit
import SwiftUI
import ComposeApp
import GoogleMobileAds
import AppTrackingTransparency

struct ComposeView: UIViewControllerRepresentable {
    
    init() {
        MainViewControllerKt.IOSBanner = { adUnitId -> UIViewController in
                    let adBannerView = VStack {
                        BannerAdView(adUnitID: adUnitId)
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
        Group{
            ComposeView().ignoresSafeArea(.keyboard)
        }.onAppear {
            self.requestAppTrackingTransparencyAuthorization()
        }
    }
    
    func requestAppTrackingTransparencyAuthorization() {
        GADMobileAds.sharedInstance().requestConfiguration.tagForUnderAgeOfConsent = true
           if #available(iOS 14.5, *) {
               DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                   ATTrackingManager.requestTrackingAuthorization { (status) in
                       GADMobileAds.sharedInstance().start(completionHandler: nil)
                   }
               }
               
            } else {
                GADMobileAds.sharedInstance().start(completionHandler: nil)
            }
        }


}




