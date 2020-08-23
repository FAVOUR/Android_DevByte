package com.example.androiddevbyte.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */


/**
 * DatabaseVideo represents a video entity in the database.
 */
@Entity
data class DatabaseVideo constructor(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String)


///**
// * Map DatabaseVideos to domain entities
// */
//
// fun List<DatabaseVideo>.asDominModel():List<DevByteVideo>{
//
//}