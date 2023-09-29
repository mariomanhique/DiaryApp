package com.example.diaryapp.data.repository.imageRepo

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diaryapp.data.database.ImageToDeleteDao
import com.example.diaryapp.data.database.ImageToUploadDao
import com.example.diaryapp.data.database.entity.ImageToDelete
import com.example.diaryapp.data.database.entity.ImageToUpload
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val imageToUploadDao: ImageToUploadDao,
    private val imageToDeleteDao: ImageToDeleteDao
) {

    fun getAllImagesToUpload():Flow<List<ImageToUpload>>{
        return imageToUploadDao.getAllImages()
    }

    fun getAllImagesToDelete():Flow<List<ImageToDelete>>{
        return imageToDeleteDao.getAllImages()
    }

    suspend fun addImageToUpload(imageToUpload: ImageToUpload){
        imageToUploadDao.addImageToUpload(imageToUpload = imageToUpload)

    }

    suspend fun cleanupImageUp(imageId: Int){
        imageToUploadDao.cleanupImage(imageId = imageId)
    }

    suspend fun addImageToDelete(imageToDelete: ImageToDelete){
        imageToDeleteDao.addImageToDelete(imageToDelete = imageToDelete)
    }

    suspend fun cleanupImageD(imageId: Int){
        imageToDeleteDao.cleanupImage(imageId = imageId)
    }

}