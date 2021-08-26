package com.vibs.githubapidemo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repository")
    fun getAllRepository(): LiveData<List<Repository>>


    @Insert
    fun insertAll(repositories: List<Repository>)

    @Query("DELETE FROM repository")
    fun deleteAll()
}