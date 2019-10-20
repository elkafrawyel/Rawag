package com.elwaha.rawag.utilies


suspend fun <T : Any> safeApiCall(call: suspend () -> DataResource<T>, errorMessage: String): DataResource<T> {
    return try {
        call()
    } catch (e: Exception) {
        e.printStackTrace()
        DataResource.Error(errorMessage)
    }
}
