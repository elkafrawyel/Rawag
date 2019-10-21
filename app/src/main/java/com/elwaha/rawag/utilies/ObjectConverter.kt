package com.elwaha.rawag.utilies

import com.beust.klaxon.Klaxon
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.storage.local.PreferencesHelper

class ObjectConverter(private val preferencesHelper: PreferencesHelper) {

    fun saveUser(userModel: UserModel) {
        preferencesHelper.user = Klaxon().toJsonString(userModel)
//        return userString
    }

    fun getUser(): UserModel? {
        return Klaxon().parse<UserModel>(preferencesHelper.user)
    }
}