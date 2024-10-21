//
//  AppDelegate.swift
//  iosApp
//
//  Created by 陳立翰 on 2024/10/21.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

import SwiftUI
import GoogleMobileAds

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        GADMobileAds.sharedInstance().start(completionHandler: nil)
        return true
    }
}
