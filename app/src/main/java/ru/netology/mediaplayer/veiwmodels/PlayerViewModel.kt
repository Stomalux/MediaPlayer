package ru.netology.mediaplayer.veiwmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.mediaplayer.dto.Album
import ru.netology.mediaplayer.dto.DataModel
import ru.netology.mediaplayer.dto.Track
import ru.netology.mediaplayer.repository.MelodyRepository

import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    application: Application,
    private val repository: MelodyRepository,
) : AndroidViewModel(application) {

    private val _data = MutableLiveData<DataModel>()
    val data: LiveData<DataModel>
        get() = _data

    fun getAlbum() {
        repository.getAlbum(object : MelodyRepository.Callback<Album> {
            override fun onSuccess(result: Album) {
                _data.postValue(DataModel(album = result, error = null))
            }

            override fun onError(e: Exception) {
                _data.postValue(DataModel(album = null, error = e.message))
            }
        })
    }

    fun play(album: Album, track: Track) {
        album.tracks.map {
            if (it.id != track.id) {
                it.isPlayed = false
                it.initInPlayer = false
            }
        }
    }

    fun like(album: Album, track: Track) {
        album.tracks.find { it.id == track.id }?.isLiked = !track.isLiked
        saveTrackSettings(album.tracks.filter { it.isLiked })
    }

    fun getCurrentAlbum(): Album? = _data.value?.album

    fun getCurrentTrack(): Track? {
        val album = _data.value?.album
        val tracks = album?.tracks ?: return null
        if (tracks.size == 0) return null
        return tracks.findLast { track -> track.initInPlayer } ?: tracks.first()
    }

    fun getNextTrack(): Track? {
        val album = _data.value?.album
        val tracks = album?.tracks ?: return null
        if (tracks.size == 0) return null
        return tracks.firstOrNull {
            val track = getCurrentTrack()
            val index = tracks.indexOf(track)
            if (track?.initInPlayer == true)
                tracks.indexOf(it) > index
            else
                false
        } ?: album.tracks.first()
    }

    fun getPrevTrack(): Track? {
        val album = _data.value?.album
        val tracks = album?.tracks ?: return null
        if (tracks.size == 0) return null
        return tracks.lastOrNull {
            tracks.indexOf(it) < tracks.indexOf(getCurrentTrack())
        } ?: album.tracks.last()
    }

    fun removeTrack(track: Track) {
        val tracks = _data.value?.album?.tracks
        tracks?.remove(track)
    }

    fun getTrackSettings() = repository.getTrackSettings()

    fun saveTrackSettings(likedTracksList: List<Track>) =
        repository.saveTrackSettings(likedTracksList)
}