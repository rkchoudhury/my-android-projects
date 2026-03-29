package com.example.cleanarchitecture.presentation.screens.note

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleanarchitecture.ui.theme.CleanArchitectureTheme


@Composable
fun NoteScreen() {
    Text(
        text = "Hello",
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteScreenPreview() {
    CleanArchitectureTheme {
        NoteScreen()
    }
}