package com.example.androiddevbyte.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

class RequestWorker(context: Context,params:WorkerParameters):CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {


        return  Result.success()
    }
}