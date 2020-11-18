package com.sahil.cocoontest.room

import androidx.room.*
import com.sahil.cocoontest.models.localdb.NewsTable

@Dao
interface NewsDao {

    @Query("SELECT * FROM TopStoriesTable")
    fun getAllData(): List<NewsTable>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(vararg topStoriesTable: NewsTable)

    @Delete
    fun deleteData(vararg topStoriesTable: NewsTable)
}