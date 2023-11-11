package com.mariomanhique.firestore.repository.authWithCredentials.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await():T{
    return suspendCancellableCoroutine {coroutine->
        addOnCompleteListener {task->
            if(task.exception!=null){
                coroutine.resumeWithException(task.exception!!)
            }else{
                coroutine.resume(task.result,null)
            }

        }

    }
}
