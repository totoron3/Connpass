package com.s24.connpassclient

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

class ContentFragment: Fragment() {

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}