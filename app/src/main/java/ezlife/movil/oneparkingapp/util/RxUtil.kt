package ezlife.movil.oneparkingapp.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import android.support.v4.app.Fragment
import ezlife.movil.oneparkingapp.R
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.toast
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

fun <T> Flowable<T>.applyShedures(): Flowable<T> = compose {
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyShedures(): Single<T> = compose {
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

//TODO:  Especificar si se mantiene conectado o si se corta la conexión para un reintento
//TODO: Como esta actualmente solo se desuscribe y ya
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
                //.retry()
                .subscribe(onNext, {})


class LifeDisposable(owner: LifecycleOwner) : LifecycleObserver {

    val dis: CompositeDisposable = CompositeDisposable()

    init {
        owner.lifecycle.addObserver(this)
    }

    infix fun add(disposable: Disposable) {
        dis.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clearSubscribes() {
        dis.clear()
    }
}