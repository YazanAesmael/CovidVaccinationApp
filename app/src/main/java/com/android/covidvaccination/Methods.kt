package com.android.covidvaccination

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

lateinit var auth: FirebaseAuth
internal val database = Firebase
    .database("YOUR FIREBASE DATABASE URL")
var storedVerificationId: String = ""
const val MY_LOG = "logCompose"
val userDetails = mutableListOf<UserData>()

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun signInWithPhoneAuthCredential(context: Context, credential: PhoneAuthCredential, navController: NavController) {
    context.getActivity()?.let {
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(it) { task ->
                if (task.isSuccessful) {
                    getNumber()
                    Toast.makeText(context, "verification success!", Toast.LENGTH_SHORT).show()
                    navController.navigate("info_screen") {
                        popUpTo(0)
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context, "something went wrong..., ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}

fun verifyPhoneNumberWithCode(context: Context, verificationId: String, code: String, navController: NavController) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(context,credential, navController)
}

fun onLoginClicked (context: Context, phoneNumber: String, navController: NavController) {
    auth = FirebaseAuth.getInstance()
    auth.setLanguageCode("en")
    val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(context,credential, navController)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(context, "verification failed.. check number or OTP and try again!", Toast.LENGTH_SHORT).show()

        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            Toast.makeText(context, "code sent!", Toast.LENGTH_SHORT).show()

        }
    }
    val options = context.getActivity()?.let {
        PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(it)
            .setCallbacks(callback)
            .build()
    }
    if (options != null) {
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

fun getScreenShotFromView(v: View): Bitmap? {
    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        v.draw(canvas)
    } catch (e: Exception) {
        Log.e("GFG", "Failed to capture screenshot because:" + e.message)
    }
    return screenshot
}

fun saveMediaToStorage(bitmap: Bitmap, context: Context) {
    val filename = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        Toast.makeText(context , "Vaccination certificate saved to gallery" , Toast.LENGTH_SHORT).show()
    }
}

fun uploadData(context: Context) {
    val uid = FirebaseAuth.getInstance().uid.toString()
    val myRef = database.getReference("Users/$uid")
    val user = UserData(userName, userState, userAge, userVaccine, userDoses, userDoseDate, userNumber)
    myRef.setValue(user)
        .addOnSuccessListener {
            Toast.makeText(context, "account created successfully! $userNumber", Toast.LENGTH_SHORT).show()
        }
}

fun bookSlot(isBooked: String, context: Context) {
    val uid = FirebaseAuth.getInstance().uid
    database.getReference("Users/$uid/isBooked").setValue(isBooked).addOnSuccessListener {
        Toast.makeText(context, "you'll get a call soon regarding your booking.", Toast.LENGTH_LONG).show()
    }
        .addOnFailureListener {
            database.getReference("Users/$uid/isBooked").setValue("false")
            Toast.makeText(context, "check you connection and try again.", Toast.LENGTH_SHORT).show()

        }
}

fun getNumber() {
    userNumber = auth.currentUser?.phoneNumber.toString()
}
