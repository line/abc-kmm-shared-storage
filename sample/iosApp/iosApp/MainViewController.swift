//
//  MainViewController.swift
//  iosApp
//
//  Created by HanSJin on 2021/03/23.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
//import shared

class MainViewController: UIViewController, PopupPresentable {

    @IBOutlet var labels: [UILabel]!
    @IBOutlet weak var clearButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        AppData.someInt = 200
        AppData.someFloat = 201.0
        AppData.someDouble = 202.0
        AppData.someBool = false
        AppData.someString = "I'm Some String"
        AppData.secureString = "I'm Encrypted String"

        updateView()
    }

    private func updateView() {
        labels[0].text = "Int: \(AppData.someInt)"
        labels[1].text = "Float: \(AppData.someFloat)"
        labels[2].text = "Double: \(AppData.someDouble)"
        labels[3].text = "Bool: \(AppData.someBool)"
        labels[4].text = "String: \(AppData.someString)"
        labels[5].text = "Secure String: \(AppData.secureString)"
        
        /* Secure */
        labels[6].text = "All Data: \(AppData.getAllData())"
        labels[7].text = "All Secure Data: \(AppData.getAllSecureData())"
    }

    @IBAction func clearStorage(_ sender: Any) {
        AppData.clearAllStorage()
        updateView()
        showPopup(title: "", msg: "Clear Storage")
    }
}

protocol PopupPresentable {
    func showPopup(title: String, msg: String)
}

extension PopupPresentable where Self: UIViewController {
    func showPopup(title: String, msg: String) {
        let alert = UIAlertController(title: title, message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}
