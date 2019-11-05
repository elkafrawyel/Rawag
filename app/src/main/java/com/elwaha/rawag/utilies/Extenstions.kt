package com.elwaha.rawag.utilies

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.DateFormat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elwaha.rawag.BuildConfig.DEBUG
import com.elwaha.rawag.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dmax.dialog.SpotsDialog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.changeLanguage() {
    var locale: Locale? = null
    if (Injector.getPreferenceHelper().language == null)
        setLanguageToDevice()
    else {
        when (Injector.getPreferenceHelper().language) {
            Constants.Language.ARABIC.value -> {
                locale = Locale("ar")
            }
            Constants.Language.ENGLISH.value -> {
                locale = Locale("en")
            }
        }
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }
}

fun Context.restartApplication() {
    val intent = Injector.getApplicationContext().packageManager.getLaunchIntentForPackage(
        Injector.getApplicationContext().packageName
    )
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}

fun Context.saveLanguage(language: Constants.Language) {
    Injector.getPreferenceHelper().language = language.value
}

fun Context.ifDeviceArabic(): Boolean {
    val deviceLocale = Locale.getDefault().language
    return deviceLocale != "en"
}

fun Context.setLanguageToDevice() {
    if (ifDeviceArabic())
        saveLanguage(Constants.Language.ARABIC)
    else
        saveLanguage(Constants.Language.ENGLISH)

    changeLanguage()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBarWithAction(
    message: String,
    actionTitle: String,
    rootView: View,
    dismiss: Boolean = true,
    action: () -> Unit
) {
    val snackBar: Snackbar? = if (dismiss)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    else
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)

    if (snackBar != null) {
        val view = snackBar.view
        val textView = view.findViewById<View>(R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        snackBar.setAction(actionTitle) {
            action.invoke()
            snackBar.dismiss()
        }
        snackBar.show()
    }
}

fun Context.snackBar(message: String?, rootView: View) {
    val snackBar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}

fun Context.showMessageInDialog(message: String, okAction: () -> Unit, cancelAction: () -> Unit) {
    val dialog = AlertDialog.Builder(this)
        .setMessage(message)
        .setTitle(getString(R.string.app_name))
        .setCancelable(true)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            okAction.invoke()
            dialog.dismiss()
        }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            cancelAction.invoke()
            dialog.dismiss()
        }.create()

    dialog.show()

}

fun Context.viewLocation(lat: Any, lang: Any): Intent {
    val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lang")
    val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    intent.setPackage("com.google.android.apps.maps")
    return intent
}

fun Context.getTime(time: String): String {
    return try {
        DateFormat.format("hh:mm a", SimpleDateFormat("hh:mm:ss").parse(time)).toString()
    } catch (ex: Exception) {
        time
    }
}

fun Context.clearNotifications(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}

fun Uri.toMultiPart(context: Context, partName: String): MultipartBody.Part {
    val path = context.getRealPathFromUri(this)
    val file = File(path)
    val requestFile =
        RequestBody.create(MediaType.parse(context.contentResolver.getType(this)!!), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}

fun String.toMultiPart(): RequestBody =
    RequestBody.create(MediaType.parse("text/plain"), this)

fun TextInputEditText.setEmptyError() {
    this.error = Injector.getApplicationContext().getString(R.string.empty_field)
    this.requestFocus()
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    Handler().postDelayed({
        error = null
    }, 2000)
}

fun Context.getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    return sdf.format(Date())
}

fun Context.showLoading(
    message: String
): SpotsDialog {
    return SpotsDialog.Builder()
        .setMessage(message)
        .setContext(this)
        .build() as SpotsDialog
}

//=============================== Image Real Path =============================
@SuppressLint("ObsoleteSdkInt")
fun Context.getRealPathFromUri(uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(Injector.getApplicationContext(), uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return "${Environment.getExternalStorageDirectory()}/${split[1]}"
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )

            return getDataColumn(contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
        return getDataColumn(uri, null, null)
    } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
        return uri.path
    }// File
    // MediaStore (and general)

    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

private fun getDataColumn(
    uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor =
            Injector.getApplicationContext().contentResolver
                .query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            if (DEBUG)
                DatabaseUtils.dumpCursor(cursor)

            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

//=============================================================================================


