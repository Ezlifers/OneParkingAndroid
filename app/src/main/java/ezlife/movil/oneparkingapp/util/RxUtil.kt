package ezlife.movil.oneparkingapp.util

import android.databinding.ObservableBoolean
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.fragments.toast
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.intellij.lang.annotations.Flow
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.net.SocketTimeoutException


fun AppCompatActivity.validateForm(emptyMsg: Int, vararg fields: String): Observable<Array<out String>> =
        Observable.create<Array<out String>> {
            val empty = fields.contains("")
            if (empty) {
                toast(emptyMsg)
            } else {
                it.onNext(fields)
            }
            it.onComplete()
        }

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

fun <T> Flowable<T>.applyShedures(): Flowable<T> = compose {
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeWithError(onNext: (T) -> Unit, onHttpError: (resString: Int) -> Unit,
                                         onError: ((error: Throwable) -> Unit)? = null): Disposable =
        doOnError {
            when (it) {
                is SocketTimeoutException -> onHttpError(R.string.http_timeout)
                is HttpException -> {
                    when (it.code()) {
                        404 -> onError?.invoke(it)
                        401 -> onHttpError(R.string.http_401)
                        else -> onHttpError(R.string.http_server)
                    }
                }
                else -> onError?.invoke(it)
            }
        }
                .retry()
                .subscribe(onNext)
