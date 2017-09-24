package ezlife.movil.oneparkingapp.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ezlife.movil.oneparkingapp.data.api.LoginApi
import ezlife.movil.oneparkingapp.data.db.AppDatabase
import ezlife.movil.oneparkingapp.data.db.dao.CarDao
import ezlife.movil.oneparkingapp.di.ActivityScope
import ezlife.movil.oneparkingapp.di.FragmentScope
import ezlife.movil.oneparkingapp.di.ViewModelKey
import ezlife.movil.oneparkingapp.ui.entry.login.LoginFragment
import ezlife.movil.oneparkingapp.ui.entry.login.LoginViewModel
import ezlife.movil.oneparkingapp.ui.entry.pass.PassFragment
import ezlife.movil.oneparkingapp.ui.entry.pass.PassViewModel
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterFragment
import ezlife.movil.oneparkingapp.ui.entry.register.RegisterViewModel
import ezlife.movil.oneparkingapp.util.AppViewModelFactory
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Provider

@Module
class EntryModule {

    @ActivityScope
    @Provides
    fun provideCarDao(database: AppDatabase): CarDao
            = database.carDao()

    @ActivityScope
    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi
            = retrofit.create(LoginApi::class.java)

    @ActivityScope
    @Provides
    @Named("entry")
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory
            = AppViewModelFactory(creators)
}

@Module
abstract class EntryFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    abstract fun bindLoginFragment(): LoginFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(RegisterModule::class))
    abstract fun bindRegisterFragment(): RegisterFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(PassModule::class))
    abstract fun bindPassFragment(): PassFragment
}

@Module
abstract class EntryViewModelBuilder{

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PassViewModel::class)
    abstract fun bindPassViewModel(viewModel: PassViewModel): ViewModel

}