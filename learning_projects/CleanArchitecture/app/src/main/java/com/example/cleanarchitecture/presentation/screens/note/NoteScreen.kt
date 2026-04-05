package com.example.cleanarchitecture.presentation.screens.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecture.presentation.common.FloatingButton
import com.example.cleanarchitecture.presentation.common.NaviBar
import com.example.cleanarchitecture.presentation.navigation.Route


@Composable
fun NoteScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Body(navController)
        FloatingButton(
            icon = Icons.Filled.Done,
            onClick = {
                navController.navigate(Route.Note.name)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            description = "Done button"
        )
    }

}

@Composable
fun Body(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val navController = rememberNavController()

    Column {
        NaviBar("New Note", navController)
        TextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteScreenPreview() {
    val navController = rememberNavController()
    NoteScreen(navController)
}