package com.hetic.cheers.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Inflate the layout
 * @param layoutRes layout resource id
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

/**
 * Easy toast function for Activity
 */
fun Activity.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

/**
 * Easy toast function for Activity
 */
fun Activity.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

/**
 * Demo function to show how to write Extension function with receiver
 */
inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun loadFragment(fragmentManager: android.support.v4.app.FragmentManager, fragmentToShow: Fragment, containerId: Int){
    fragmentManager
            .beginTransaction()
            .add(containerId, fragmentToShow)
            .commit()
}

fun replaceFragment(fragmentManager: android.support.v4.app.FragmentManager, fragmentToShow : Fragment, containerId: Int){
    fragmentManager
            .beginTransaction()
            .replace(containerId, fragmentToShow)
            .addToBackStack(null)
            .commit()
}

fun displayMessage(text : String, context : Context){
    val toast = Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG)

    toast.setGravity(Gravity.TOP, 20, 20)
    toast.show()
}
