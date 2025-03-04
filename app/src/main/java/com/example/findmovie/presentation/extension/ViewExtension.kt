package com.example.findmovie.presentation.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.findmovie.R
import com.google.android.material.snackbar.Snackbar

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.placeholder)
        .placeholder(R.drawable.placeholder)
        .placeholder(R.drawable.placeholder)
        .into(this)
}

fun ImageView.loadActorImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.actor)
        .placeholder(R.drawable.actor)
        .into(this)
}


fun View.hideKeyboard(){
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken,0)
}
fun Fragment.showSnackbar(message: String) {
    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
    }
}
