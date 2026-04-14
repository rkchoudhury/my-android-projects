package com.example.cleanarchitecture.framework.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cleanarchitecture.framework.RoomNoteDataSource
import com.example.cleanarchitecture.framework.UseCases
import com.example.cleanarchitecture.framework.di.ApplicationModule
import com.example.cleanarchitecture.framework.di.DaggerViewModelComponent
import com.example.core.data.Note
import com.example.core.repository.NoteRepository
import com.example.core.usecase.AddNote
import com.example.core.usecase.GetAllNotes
import com.example.core.usecase.GetNote
import com.example.core.usecase.RemoveNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // repository & useCases will be removed later and put into a Dependency Injection
//    val repository = NoteRepository(RoomNoteDataSource(application))
//    val useCases = UseCases(
//        AddNote(repository),
//        GetAllNotes(repository),
//        GetNote(repository),
//        RemoveNote(repository)
//    )

    // With Dependency Injection
    @Inject
    lateinit var useCases: UseCases

    init {
        // Rebuild the project -Dagger will generate DaggerViewModelComponent in the build directory
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build()
            .inject(this)
    }

    // LiveData
    val allNotes: MutableLiveData<List<Note>> = MutableLiveData()

    fun getAllNotes() {
        coroutineScope.launch {
            val notes: List<Note> = useCases.getAllNotes()
            allNotes.postValue(notes)
        }
    }
}