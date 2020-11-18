package com.sahil.cocoontest.models.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "TopStoriesTable", indices = [Index(value = ["title","image"], unique = true)])
class NewsTable(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "publish_date") var publishDate: String,
    @ColumnInfo(name = "url") var mainUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}