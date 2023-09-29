package com.example.diaryapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diaryapp.data.database.entity.ImageToDelete
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageToDeleteDao {

    @Query("SELECT * FROM image_to_delete_table ORDER BY id ASC")
     fun getAllImages(): Flow<List<ImageToDelete>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun addImageToDelete(imageToDelete: ImageToDelete)

    @Query("DELETE FROM image_to_delete_table WHERE id=:imageId")
     fun cleanupImage(imageId: Int)

}