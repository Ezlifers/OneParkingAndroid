package ezlife.movil.oneparkingapp.di.modules

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import ezlife.movil.oneparkingapp.R
import ezlife.movil.oneparkingapp.data.db.AppDatabase
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import ezlife.movil.oneparkingapp.util.Loader
import ezlife.movil.oneparkingapp.util.Preference
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app

    @Singleton
    @Provides
    fun providePreference(context: Context): SharedPreferences
            = context.getSharedPreferences(Preference.NAME, Activity.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase
            = Room.databaseBuilder(context, AppDatabase::class.java, "oneparkingapp")
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(context: Context): Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.url_base))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideLoader(): Loader = Loader()

    @Singleton
    @Provides
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)

}