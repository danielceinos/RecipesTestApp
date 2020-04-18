package com.danielceinos.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.danielceinos.notesdomain.flux.FetchRecipesAction
import com.danielceinos.notesdomain.flux.RecipesStore
import com.danielceinos.todolist.databinding.ActivityMainBinding
import com.hoopcarpool.fluxy.Dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Toolbar>(R.id.toolBar).setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    fun setToolbarTitle(title: String) {
        findViewById<Toolbar>(R.id.toolBar).title = title
    }

}
