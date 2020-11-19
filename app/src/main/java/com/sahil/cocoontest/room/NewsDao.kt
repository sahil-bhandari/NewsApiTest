package com.sahil.cocoontest.room

import androidx.room.*
import com.sahil.cocoontest.BuildConfig
import com.sahil.cocoontest.models.localdb.NewsTable

@Dao
interface NewsDao {

    @Query("SELECT * FROM "+BuildConfig.TABLE_NAME)
    fun getAllData(): List<NewsTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(vararg topStoriesTable: NewsTable)

    @Delete
    fun deleteData(vararg topStoriesTable: NewsTable)
}