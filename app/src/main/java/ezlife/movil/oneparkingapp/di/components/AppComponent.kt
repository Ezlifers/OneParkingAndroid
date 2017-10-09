package ezlife.movil.oneparkingapp.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import ezlife.movil.oneparkingapp.App
import ezlife.movil.oneparkingapp.di.modules.ActivityBuilder
import ezlife.movil.oneparkingapp.di.modules.ViewModelBuilder
import ezlife.movil.oneparkingapp.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
        ActivityBuilder::class,
        AppModule::class,
        ViewModelBuilder::class))
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}