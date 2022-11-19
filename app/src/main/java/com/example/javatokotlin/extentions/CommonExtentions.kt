package com.example.javatokotlin.extentions

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import com.google.gson.GsonBuilder
import com.example.javatokotlin.models.ErrorResponse
import android.widget.Toast
import java.io.IOException

//object Util {
//    fun showErrorMessage(context: Context?, errorBody: ResponseBody) {
//        val gson = GsonBuilder().create()
//        val errorResponse: ErrorResponse
//        try {
//            errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
//            context?.toast(errorResponse.message!!)
//        } catch (e: IOException) {
//            Log.i("Exception ", e.toString())
//        }
//}
//
//    fun showMessage(context: Context?, msg: String) {
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//    }
//}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.showErrorMessage(errorBody: ResponseBody, duration: Int = Toast.LENGTH_SHORT) {
    val gson = GsonBuilder().create()
    try {
        val errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
        toast(errorResponse.message!!, duration)
    } catch (e: IOException) {
        Log.i("Exception ", e.toString())
    }
}