package com.danielceinos.todolist.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.danielceinos.common.AppLogger
import com.danielceinos.todolist.BuildConfig
import com.danielceinos.todolist.app
import com.hoopcarpool.fluxy.FluxyStore
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton
import timber.log.Timber

/**
 * Kodein module that provides app dependencies
 */
object AppModule {
    fun create() = Kodein.Module("app", true) {
        bind<Application>() with singleton { app }
        bind() from setBinding<FluxyStore<*>>()
        bind<ViewModelProvider.Factory>() with singleton {
            KodeinViewModelFactory(
                kodein.direct
            )
        }
        bind<Boolean>(tag = "debug") with singleton { BuildConfig.DEBUG }
        bind<String>(tag = "baseUrl") with singleton { "http://www.recipepuppy.com/" }
        bind<AppLogger>() with singleton {
            object : AppLogger {
                override fun v(tag: String, msg: String) = Timber.tag(tag).v(msg)
                override fun d(tag: String, msg: String) = Timber.tag(tag).d(msg)
                override fun i(tag: String, msg: String) = Timber.tag(tag).i(msg)
                override fun w(tag: String, msg: String) = Timber.tag(tag).w(msg)
                override fun e(tag: String, msg: String) = Timber.tag(tag).e(msg)
            }
        }
    }
}
