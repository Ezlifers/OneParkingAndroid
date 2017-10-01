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
import ezlife.movil.oneparkingapp.data.api.CashApi
import ezlife.movil.oneparkingapp.data.api.SetupApi
import ezlife.movil.oneparkingapp.data.api.ZoneStateApi
import ezlife.movil.oneparkingapp.data.db.AppDatabase
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.data.db.dao.ConfigDao
import ezlife.movil.oneparkingapp.data.db.dao.ScheduleDao
import ezlife.movil.oneparkingapp.data.db.dao.ZoneDao
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import ezlife.movil.oneparkingapp.util.Loader
import ezlife.movil.oneparkingapp.util.Preference
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
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCarDao(database: AppDatabase): CarDao
            = database.carDao()

    @Singleton
    @Provides
    fun proviceConfigDao(database: AppDatabase): ConfigDao
            = database.configDao()

    @Singleton
    @Provides
    fun proviceZoneDao(database: AppDatabase): ZoneDao
            = database.zoneDao()

    @Singleton
    @Provides
    fun proviceScheduleDao(database: AppDatabase): ScheduleDao
            = database.scheduleDao()

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
    fun provideCashApi(retrofit: Retrofit): CashApi
            = retrofit.create(CashApi::class.java)

    @Singleton
    @Provides
    fun provideSetupApi(retrofit: Retrofit): SetupApi
            = retrofit.create(SetupApi::class.java)

    @Singleton
    @Provides
    fun provideZoneStateApi(retrofit: Retrofit): ZoneStateApi
            = retrofit.create(ZoneStateApi::class.java)

    @Singleton
    @Provides
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)

}