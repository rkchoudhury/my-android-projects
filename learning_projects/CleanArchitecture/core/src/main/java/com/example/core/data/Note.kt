package com.example.core.data

/**
 * Simple POJO Objects (Plain Old Java Object)
 *
 * These classes only contains the data and don't contain any functionality
 */
data class Note(
    var title: String,
    var content: String,
    var creationTime: Long,
    var updateTime: Long,
    var id: Long = 0,
)