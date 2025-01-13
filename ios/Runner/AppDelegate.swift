import Flutter
import UIKit

@main
@objc class AppDelegate: FlutterAppDelegate {

private let CHANNEL = "com.example.change_icon/change_icon"


  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
      if let flutterViewController = window?.rootViewController as? FlutterViewController {
            let methodChannel = FlutterMethodChannel(name: CHANNEL, binaryMessenger: flutterViewController.binaryMessenger)
            methodChannel.setMethodCallHandler { [weak self] (call: FlutterMethodCall, result: FlutterResult) in
                if call.method == "changeIcon" {
                    guard let iconName = call.arguments as? String else {
                        result(FlutterError(code: "INVALID_ARGUMENT", 
                                         message: "Icon name is required", 
                                         details: nil))
                        return
                    }
                    
                    let model = Model()
                    if let icon = Icon(rawValue: iconName) {
                        model.setAlternateAppIcon(icon: icon)
                        result(true)
                    } else {
                        result(FlutterError(code: "INVALID_ICON", 
                                         message: "Icon not found", 
                                         details: nil))
                    }
                } else {
                    result(FlutterMethodNotImplemented)
                }
            }
        }
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
