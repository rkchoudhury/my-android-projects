package com.example.cleanarchitecture.presentation.screens.notelist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleanarchitecture.ui.theme.CleanArchitectureTheme

@Composable
fun NoteListScreen(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteListScreenPreview() {
    CleanArchitectureTheme {
        NoteListScreen("Android")
    }
}