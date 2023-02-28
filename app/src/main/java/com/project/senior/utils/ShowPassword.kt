package com.project.senior.utils

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.project.senior.R

fun showPassword(passEditText: EditText, showPassImg: ImageView) {
    if(showPassImg.tag == R.drawable.baseline_visibility_off_24){
        passEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        showPassImg.tag = R.drawable.baseline_visibility_24
        showPassImg.setImageResource(R.drawable.baseline_visibility_24)
    } else{
        passEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        showPassImg.tag = R.drawable.baseline_visibility_off_24
        showPassImg.setImageResource(R.drawable.baseline_visibility_off_24)
    }
}