//
//  BannerAdView.swift
//  iosApp
//
//  Created by 陳立翰 on 2024/10/21.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import GoogleMobileAds

struct BannerAdView: UIViewRepresentable {
    var adUnitID: String
    
    func makeUIView(context: Context) -> GADBannerView {
        let bannerView = GADBannerView()
        
        bannerView.adUnitID = adUnitID
        let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene
        if let rootViewController = windowScene?.windows.first?.rootViewController {
            bannerView.rootViewController = rootViewController
        }
    
        bannerView.load(GADRequest())
        return bannerView
    }
    
    func updateUIView(_ uiView: GADBannerView, context: Context) {
        
    }
}
