package com.example.cleanarchitecture.presentation.navigation

sealed class Route(val name: String) {
    data object NoteList: Route("NoteList")
    data object Note: Route("Note")
}