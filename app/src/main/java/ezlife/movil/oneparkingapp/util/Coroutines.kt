package ezlife.movil.oneparkingapp.util

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run

fun asyncUI(job: suspend () -> Unit) {
    launch(UI) {
        job()
    }
}

suspend fun <T> await(action: () -> T): T = run(CommonPool) { action() }