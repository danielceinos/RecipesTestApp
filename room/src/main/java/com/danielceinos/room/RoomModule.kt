package com.danielceinos.room

import androidx.room.Room
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object RoomModule {
    fun create() = Kodein.Module("room", true) {
        bind<RecipesDatabase>() with singleton {
            Room.databaseBuilder(instance(), RecipesDatabase::class.java, "databaseName").build()
        }

        bind<RecipesDao>() with singleton { instance<RecipesDatabase>().recipesDao() }

    }
}
