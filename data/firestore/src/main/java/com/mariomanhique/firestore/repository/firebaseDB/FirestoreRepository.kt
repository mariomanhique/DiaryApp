package com.mariomanhique.firestore.repository.firebaseDB

import com.mariomanhique.util.model.Diary
import com.mariomanhique.util.model.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface FirestoreRepository {

     fun getAllDiaries(): Flow<Diaries>
     fun getDiaries():  Flow<Diaries>
     fun insertDiary(diary: Diary): RequestState<Flow<Diary?>>
     suspend fun updateDiary(diary: Diary): RequestState<String>
     fun getSelectedDiary(diaryId: String): Flow<RequestState<Diary?>>
     fun deleteDiary(diaryId: String): RequestState<String>?
     suspend fun deleteAllDiary(): RequestState<Boolean>?

      fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries>
}