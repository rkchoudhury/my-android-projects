package com.example.cleanarchitecture.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecture.presentation.screens.note.NoteScreen
import com.example.cleanarchitecture.presentation.screens.notelist.NoteListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.NoteList.name) {
        composable(Route.NoteList.name) { NoteListScreen() }
        composable(Route.Note.name) { NoteScreen() }
    }
}