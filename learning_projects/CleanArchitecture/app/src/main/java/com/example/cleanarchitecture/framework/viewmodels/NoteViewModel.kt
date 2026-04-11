package com.example.cleanarchitecture.framework.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cleanarchitecture.framework.RoomNoteDataSource
import com.example.cleanarchitecture.framework.UseCases
import com.example.core.data.Note
import com.example.core.repository.NoteRepository
import com.example.core.usecase.AddNote
import com.example.core.usecase.GetAllNotes
import com.example.core.usecase.GetNote
import com.example.core.usecase.RemoveNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // repository & useCases will be removed later and put into a Dependency Injection
    val repository = NoteRepository(RoomNoteDataSource(application))
    val useCases = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository)
    )

    // LiveData
    val saved: MutableLiveData<Boolean> = MutableLiveData()
    val selectedNote: MutableLiveData<Note?> = MutableLiveData()

    fun saveNote(note: Note) {
        coroutineScope.launch {
            useCases.addNote(note)
            saved.postValue(true)
        }
    }

    fun getNote(noteId: Long) {
        coroutineScope.launch {
            val note = useCases.getNote(noteId)
            selectedNote.postValue(note)
        }
    }
}