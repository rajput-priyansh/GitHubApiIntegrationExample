package com.vibs.githubapidemo

import android.app.AlertDialog
import android.content.Context

class AppUtil {

    companion object {

        fun displaySimpleMessageDialog(context: Context, message: String) {

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setTitle("Message")
                .setPositiveButton("OKAY") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }

    }
}