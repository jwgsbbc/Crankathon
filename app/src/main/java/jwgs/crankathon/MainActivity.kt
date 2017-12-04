package jwgs.crankathon

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

/**
 * This can be easily broken by rotating while the dialogue is showing.
 * The dialogue will re-appear, but the state of view is lost (a solvable problem)
 * and (more importantly) the co-routine is lost.
 *
 * Perhaps the co-routine should live on a retained fragment, or a ViewModel??. But all that
 * starts to make the co-routine less useful.
 */
class MainActivity : AppCompatActivity(), TvLicenceDialogue.Receiver, PgDialogue.Receiver {

    private var onTvLicenceResult: ((Boolean) -> Unit)? = null
    private var onPgResult: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var loading: Boolean = false

    @Suppress("UNUSED_PARAMETER")
    fun onPlayClicked(view: View) {
        /**
         * My worry here is that if the Activity is destroyed
         * while the co-routine is running then the Activity will
         * be leaked.  I guess we need to cancel it on Destroy.
         */
        if(loading) return

        async(UI) {
            loading = true
            showLoading()
            if (checkTvLicence() && checkPG()) {
                playEpisode()
            }
            hideLoading()
            loading = false
        }
    }

    private suspend fun checkPG(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            showPgDialogue(this) {
                continuation.resume(it)
            }
        }
    }

    private suspend fun checkTvLicence(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            showTvLicenceDialogue(this) {
                continuation.resume(it)
            }
        }
    }

    private fun playEpisode() {
        Toast.makeText(this, "This is instead of an episode playing!", Toast.LENGTH_SHORT).show()
    }

    private fun showTvLicenceDialogue(activity: FragmentActivity, function: ((Boolean) -> Unit)?) {
        onTvLicenceResult = function
        val tvLicenceDialogue = TvLicenceDialogue()
        val fragmentManager = activity.fragmentManager
        tvLicenceDialogue.show(fragmentManager, "TV_LICENCE")
    }

    private fun showPgDialogue(activity: FragmentActivity, function: ((Boolean) -> Unit)?) {
        onPgResult = function
        val pgDialogue = PgDialogue()
        val fragmentManager = activity.fragmentManager
        pgDialogue.show(fragmentManager, "PG")
    }

    private fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun tvLicenceDialogueResult(result: Boolean) {
        onTvLicenceResult?.invoke(result)
    }

    override fun pgDialogueResult(result: Boolean) {
        onPgResult?.invoke(result)
    }

}

