package com.mariomanhique.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mariomanhique.database.Constants.IMAGE_TO_UPLOAD_TABLE

@Entity(tableName = IMAGE_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "remoteImagePath")
    val remoteImagePath: String,
    @ColumnInfo(name = "imageUri")
    val imageUri: String,
    @ColumnInfo(name = "sessionUri")
    val sessionUri: String
)
