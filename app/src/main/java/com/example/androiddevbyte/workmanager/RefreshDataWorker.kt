package com.example.androiddevbyte.workmanager

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androiddevbyte.database.getDatabase
import com.example.androiddevbyte.repository.VideoRepository
import com.example.androiddevbyte.ui.DevByteViewModel
import retrofit2.HttpException
import java.net.HttpCookie

class RefreshDataWorker(val context: Context, params:WorkerParameters):CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {

        val database = getDatabase(context = applicationContext)
        val repository =VideoRepository(database = database)

         try {
             repository.refreshVideos()
         }  catch (e :HttpException) {
             Result.retry()
         }



        return  Result.success()
    }
}