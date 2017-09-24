package ezlife.movil.oneparkingapp.util

import android.databinding.ObservableBoolean
import android.support.v4.app.Fragment
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.fragments.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException

fun Fragment.validateForm(emptyMsg: Int, vararg fields: String): Observable<Array<out String>> =
        Observable.create<Array<out String>> {
            val empty = fields.contains("")
            if (empty) {
                toast(emptyMsg)
            } else {
                it.onNext(fields)
            }
            it.onComplete()
        }


class Loader {
    val loading: ObservableBoolean = ObservableBoolean(false)
}

fun <T> Observable<T>.loader(loader: Loader): Observable<T> =
        flatMap {
            loader.loading.set(true)
            Observable.just(it)
        }.compose { it.doFinally { loader.loading.set(false) } }

fun <T> Observable<T>.applyShedures(): Observable<T> = compose {
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeWithError(onNext: (T) -> Unit, onHttpError: (resString: Int) -> Unit,
                                         onError: (error: Throwable) -> Unit): Disposable =
        doOnError {
            when (it) {
                is SocketTimeoutException -> onHttpError(R.string.http_timeout)
                is HttpException -> {
                    when (it.code()) {
                        404 -> onError(it)
                        401 -> onHttpError(R.string.http_401)
                        else -> onHttpError(R.string.http_server)
                    }
                }
                else -> onError(it)
            }
        }
                .retry()
                .subscribe(onNext)
