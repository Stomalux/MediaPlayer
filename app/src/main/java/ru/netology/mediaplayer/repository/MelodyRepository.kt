package ru.netology.mediaplayer.repository

import ru.netology.mediaplayer.dto.Album
import ru.netology.mediaplayer.dto.Track


interface MelodyRepository {
    fun getAlbum(callback: Callback<Album>)
    fun getTrackSettings(): List<Long>
    fun saveTrackSettings(trackList: List<Track>)

    interface Callback<T> {
        fun onSuccess(result: T) {}
        fun onError(e: Exception) {}
    }
}