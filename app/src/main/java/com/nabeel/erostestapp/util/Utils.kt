package com.nabeel.erostestapp.util

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.nabeel.erostestapp.R

class Utils {


    companion object {
        fun showSnackBar(view: View, message: String) {
            val snack: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val v: View = snack.view
            val tv = v.findViewById<View>(R.id.snackbar_text) as TextView
            tv.setTextColor(Color.WHITE)
            snack.show()
        }
    }
}