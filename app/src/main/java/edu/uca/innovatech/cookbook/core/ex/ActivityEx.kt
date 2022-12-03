package edu.uca.innovatech.cookbook.core.ex

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Activity.showMaterialDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    negativeBtn: String,
    positiveBtn: String,
    listenerNo: () -> Unit,
    listenerYes: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(isCancelable)
        .setNegativeButton(negativeBtn) { _, _ ->
            listenerNo()
        }
        .setPositiveButton(positiveBtn) { _, _ ->
            listenerYes()
        }
        .show()
}

fun Activity.showToast(
    message: String,
    duration: Int
) {
    Toast.makeText(this, message, duration).show()
}