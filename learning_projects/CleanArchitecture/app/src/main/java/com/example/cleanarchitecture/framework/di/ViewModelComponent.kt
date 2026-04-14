package com.example.cleanarchitecture.framework.di

import com.example.cleanarchitecture.framework.viewmodels.NoteListViewModel
import com.example.cleanarchitecture.framework.viewmodels.NoteViewModel
import dagger.Component

@Component(modules = [ApplicationModule::class, RepositoryModule::class, UseCasesModule::class])
interface ViewModelComponent {

    fun inject(noteViewModel: NoteViewModel)

    fun inject(listViewModel: NoteListViewModel)
}