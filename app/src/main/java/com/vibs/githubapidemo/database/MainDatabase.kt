package com.vibs.githubapidemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Repository::class], version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract fun repositoryDto(): RepositoryDao
    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabaseClient(context: Context) : MainDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, MainDatabase::class.java, "MAIN_DATABASE")
                    .fallbackToDestructiveMigration()//If it cannot find the set of Migrations that will bring the database to the current version, it will throw an IllegalStateException.
                    .build()

                return INSTANCE!!

            }
        }

    }
}