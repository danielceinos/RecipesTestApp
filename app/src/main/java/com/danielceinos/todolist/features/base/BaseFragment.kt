package com.danielceinos.todolist.features.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import timber.log.Timber

open class BaseFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    /**
     * Observes a given [LiveData].
     */
    inline fun <T> LiveData<T>.observe(log: Boolean = true, crossinline cb: (T) -> Unit) = observe(this@BaseFragment,
        Observer {
            if (log) {
                Timber.d(
                    """

                    ┌────────────────────────────────────────────
                    |─> ${it.toString()}
                    └────────────────────────────────────────────
                    """.trimIndent()
                )
            }
            cb(it)
        })
}