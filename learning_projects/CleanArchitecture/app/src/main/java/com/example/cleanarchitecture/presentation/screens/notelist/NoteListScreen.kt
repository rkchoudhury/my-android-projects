package com.example.cleanarchitecture.presentation.screens.notelist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleanarchitecture.ui.theme.CleanArchitectureTheme

@Composable
fun NoteListScreen() {
    Text(
        text = "Hello!",
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteListScreenPreview() {
    CleanArchitectureTheme {
        NoteListScreen()
    }
}