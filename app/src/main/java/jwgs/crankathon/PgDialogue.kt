package jwgs.crankathon

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

class PgDialogue : DialogFragment() {

    interface Receiver {
        fun pgDialogueResult(result: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.dialog_check_pg)
                .setPositiveButton(R.string.yes, { _, _ ->
                    parent().pgDialogueResult(true)
                })
                .setNegativeButton(R.string.no, { _, _ ->
                    parent().pgDialogueResult(false)
                })
        return builder.create()
    }

    private fun parent(): Receiver {
        return activity as Receiver
    }

}