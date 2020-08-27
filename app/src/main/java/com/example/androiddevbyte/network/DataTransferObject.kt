package com.example.androiddevbyte.network

import androidx.lifecycle.Transformations.map
import com.example.androiddevbyte.database.DatabaseVideo
import com.example.androiddevbyte.domain.DevByteVideo
import com.squareup.moshi.JsonClass


 @JsonClass(generateAdapter = true)
 data class NetworkVideoContainer (val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkVideo(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String,
    val closedCaptions: String?)


 fun NetworkVideoContainer.addDomainModel():List<DevByteVideo>{

     return videos.map {
            DevByteVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
     }
 }

 fun NetworkVideoContainer.asDatabaseModel():List<DatabaseVideo>{
     return videos.map {
         DatabaseVideo(
             title = it.title,
             description = it.description,
             url = it.url,
             updated = it.updated,
             thumbnail = it.thumbnail)
     }
 }
