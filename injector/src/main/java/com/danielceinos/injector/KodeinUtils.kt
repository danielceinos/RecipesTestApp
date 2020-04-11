package com.danielceinos.injector

import com.hoopcarpool.fluxy.FluxyStore
import org.kodein.di.Kodein
import org.kodein.di.bindings.NoArgSimpleBindingKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Binds a store in a Kodein module, assuming that it's a singleton dependency.
 */
inline fun <reified T : FluxyStore<*>> Kodein.Builder.bindStore(noinline creator: NoArgSimpleBindingKodein<*>.() -> T) {
    bind<T>() with singleton(creator = creator)
    bind<FluxyStore<*>>().inSet() with singleton { instance<T>() }
}
