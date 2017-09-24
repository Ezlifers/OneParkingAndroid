package ezlife.movil.oneparkingapp.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ezlife.movil.oneparkingapp.di.ActivityScope
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

open class AppViewModelFactory(val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: throw Throwable("unknown model class $modelClass")
        return creator.get() as T
    }
}

