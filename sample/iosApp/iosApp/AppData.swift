//
//  UserDefaultWrapper.swift
//  iosApp
//
//  Created by HanSJin on 2021/04/12.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

struct AppData {

    @UserDefaultWrapper(key: "SomeInt", default: 0)
    static var someInt: Int

    @UserDefaultWrapper(key: "SomeFloat", default: 0)
    static var someFloat: Float

    @UserDefaultWrapper(key: "SomeDouble", default: 0)
    static var someDouble: Double

    @UserDefaultWrapper(key: "SomeBool", default: false)
    static var someBool: Bool

    @UserDefaultWrapper(key: "SomeString", default: "")
    static var someString: String

    @KeyChainWrapper(key: "SomeSecure", default: "")
    static var secureString: String

    /* Print All Storage */
    static func getAllData() -> [String: Any] {
        return SharedStorage.Companion().getAll()
    }
    static func getAllSecureData() -> [String: Any] {
        return SharedStorage.Companion().getAllSecure()
    }

    /* Clear Storage */
    static func clearAllStorage() {
        SharedStorage.Companion().clearAllStorage()
    }
    static func clearSharedStorage() {
        SharedStorage.Companion().clearSharedStorage()
    }
    static func clearSecureStorage() {
        SharedStorage.Companion().clearSecureStorage()
    }
}

@propertyWrapper
struct UserDefaultWrapper<Value> {
    let key: String
    let `default`: Value

    var wrappedValue: Value {
        get { SharedStorage.Companion().load(key: key, default: `default`) as? Value ?? `default` }
        set { SharedStorage.Companion().save(value: newValue, forKey: key) }
    }
}

@propertyWrapper
struct KeyChainWrapper {
    let key: String
    let `default`: String

    var wrappedValue: String {
        get { SharedStorage.Companion().secureLoad(key: key, default: `default`) }
        set { SharedStorage.Companion().secureSave(value: newValue, forKey: key) }
    }
}
