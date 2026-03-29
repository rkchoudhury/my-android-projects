package com.example.cleanarchitecture.presentation.screens.note

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleanarchitecture.ui.theme.CleanArchitectureTheme


@Composable
fun NoteScreen(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteScreenPreview() {
    CleanArchitectureTheme {
        NoteScreen("Android")
    }
}