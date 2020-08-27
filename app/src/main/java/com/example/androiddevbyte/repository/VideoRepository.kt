package com.example.androiddevbyte.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.androiddevbyte.database.VideosDatabase
import com.example.androiddevbyte.database.asDomainModel
import com.example.androiddevbyte.domain.DevByteVideo
import com.example.androiddevbyte.network.DevByteNetwork
import com.example.androiddevbyte.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRepository (val database: VideosDatabase) {


    //Retrieve the database information amd transform it to the app ready model

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()){
            it.asDomainModel()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {

//            Timber.d("refresh videos is called");
            val playlist = DevByteNetwork.devbytes.getPlaylist().await()
            database.videoDao.insertAll(playlist.asDatabaseModel())


        }
    }
}