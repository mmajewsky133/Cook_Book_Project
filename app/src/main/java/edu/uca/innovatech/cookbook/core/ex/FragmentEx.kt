package edu.uca.innovatech.cookbook.core.ex

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showMaterialDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    negativeBtn: String,
    positiveBtn: String,
    listenerNo: (String) -> Unit,
    listenerYes: (String) -> Unit
) {
    this.context?.let {
        MaterialAlertDialogBuilder(it)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setNegativeButton(negativeBtn) { _, _ ->
                listenerNo("")
            }
            .setPositiveButton(positiveBtn) { _, _ ->
                listenerYes("")
            }
            .show()
    }
}

fun Fragment.showToast(
    message: String,
    duration: Int
) {
    Toast.makeText(this.context, message, duration).show()
}



