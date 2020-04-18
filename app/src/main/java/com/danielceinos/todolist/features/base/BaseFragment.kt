package com.danielceinos.todolist.features.base

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.danielceinos.todolist.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import timber.log.Timber

open class BaseFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    fun setToolbarTitle(title: String) {
        activity?.findViewById<Toolbar>(R.id.toolBar)?.title = title
    }

    /**
     * Observes a given [LiveData].
     */
    inline fun <T> LiveData<T>.observe(log: Boolean = true, crossinline cb: (T) -> Unit) {
        removeObservers(this@BaseFragment)
        observe(this@BaseFragment,
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
}