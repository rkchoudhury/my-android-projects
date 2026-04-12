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

class NoteListViewModel(application: Application) : AndroidViewModel(application) {
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
    val allNotes: MutableLiveData<List<Note>> = MutableLiveData()

    fun getAllNotes() {
        coroutineScope.launch {
            val notes: List<Note> = useCases.getAllNotes()
            allNotes.postValue(notes)
        }
    }
}