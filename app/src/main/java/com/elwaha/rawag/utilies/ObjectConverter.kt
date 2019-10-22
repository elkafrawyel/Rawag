package com.elwaha.rawag.utilies

import com.beust.klaxon.Klaxon
import com.elwaha.rawag.data.models.UserModel

class ObjectConverter {

    fun saveUser(userModel: UserModel): String {
        return Klaxon().toJsonString(userModel)
    }

    fun getUser(userString: String): UserModel {
        return Klaxon().parse(userString)!!
    }
}