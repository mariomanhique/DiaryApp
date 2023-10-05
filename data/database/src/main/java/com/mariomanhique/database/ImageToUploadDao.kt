package com.mariomanhique.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariomanhique.database.entity.ImageToUpload
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageToUploadDao {

    @Query("SELECT * FROM image_to_upload_table ORDER BY id ASC")
    fun getAllImages(): Flow<List<ImageToUpload>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addImageToUpload(imageToUpload: ImageToUpload)

    @Query("DELETE FROM image_to_upload_table WHERE id=:imageId")
     fun cleanupImage(imageId: Int)

}