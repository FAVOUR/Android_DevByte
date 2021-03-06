package com.example.androiddevbyte

import android.app.Application
import androidx.work.*
import com.example.androiddevbyte.workmanager.RefreshDataWorker
import com.example.androiddevbyte.workmanager.RefreshDataWorker.Companion.CONSTANT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.lang.Exception
import java.util.concurrent.TimeUnit

class DevByteApplication :Application(){
    var applicationScope = CoroutineScope (Dispatchers.IO)

    val constraints = Constraints.Builder()
                                             .setRequiredNetworkType(NetworkType.UNMETERED)
                                             .build()

    override fun onCreate() {
       super.onCreate()
        delayedInit();
    }


    private fun delayedInit() {
        applicationScope.launch {
//            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to 'fetch' new network data daily.
     */
    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(15,TimeUnit.MINUTES)
             .setConstraints(constraints)
             .build()



        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(CONSTANT,ExistingPeriodicWorkPolicy.KEEP,repeatingRequest)
    }
}