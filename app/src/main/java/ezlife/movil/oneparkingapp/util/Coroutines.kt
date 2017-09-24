package ezlife.movil.oneparkingapp.util

fun asyncUI(job: suspend () -> Unit) {
    /*launch(UI) {
        job()
    }*/
}

suspend fun <T> await(action: () -> T): T {
    return action()
}//= run(CommonPool) { action() }