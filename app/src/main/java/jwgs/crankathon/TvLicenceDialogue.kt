package jwgs.crankathon

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

class TvLicenceDialogue : DialogFragment() {

    interface Receiver {
        fun tvLicenceDialogueResult(result: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.dialog_check_tv_licence)
                .setPositiveButton(R.string.yes, { _, _ ->
                    parent().tvLicenceDialogueResult(true)
                })
                .setNegativeButton(R.string.no, { _, _ ->
                    parent().tvLicenceDialogueResult(false)
                })
        return builder.create()
    }

    private fun parent(): Receiver {
        return activity as Receiver
    }

}