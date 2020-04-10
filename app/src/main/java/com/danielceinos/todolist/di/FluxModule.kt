package com.danielceinos.todolist.di

import com.hoopcarpool.fluxy.Dispatcher
import com.hoopcarpool.fluxy.Logger
import com.hoopcarpool.timberlogger.TimberLogger
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object FluxModule {
    fun create() = Kodein.Module("fluxy", true) {
        bind<Logger>() with singleton { TimberLogger() }
        bind<Dispatcher>() with singleton { Dispatcher(instance()) }
    }
}
