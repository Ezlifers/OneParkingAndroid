package ezlife.movil.oneparkingapp

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ezlife.movil.oneparkingapp.db.AppDataBase
import ezlife.movil.oneparkingapp.db.DB
import ezlife.movil.oneparkingapp.di.components.AppComponent
import ezlife.movil.oneparkingapp.di.components.DaggerAppComponent
import ezlife.movil.oneparkingapp.providers.RetrofitHelper
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = injector

}
