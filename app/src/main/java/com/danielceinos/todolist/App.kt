package com.danielceinos.todolist

import android.app.Application
import com.danielceinos.injector.DependenciesModule
import com.danielceinos.network.NetworkModule
import com.danielceinos.room.RoomModule
import com.danielceinos.todolist.di.AppModule
import com.danielceinos.todolist.di.FluxModule
import com.danielceinos.todolist.di.ViewModelsModule
import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.FluxyStore
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber
import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()

val app: App get() = appInstance

class App : Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true)

    private lateinit var dispatcher: Dispatcher

    override fun onCreate() {
        super.onCreate()

        appInstance = this
        Timber.plant(Timber.DebugTree())

        // Start dependency injection
        initializeInjection()
    }

    private fun initializeInjection() {

        kodein.clear()

        with(kodein) {
            addImport(AppModule.create())
            addImport(ViewModelsModule.create())
            addImport(FluxModule.create())
            addImport(NetworkModule.create())
            addImport(RoomModule.create())
            addImport(DependenciesModule.create())
        }

        dispatcher = kodein.direct.instance()
        dispatcher.stores = kodein.direct.instance<Set<FluxyStore<*>>>().toList()
    }
}
