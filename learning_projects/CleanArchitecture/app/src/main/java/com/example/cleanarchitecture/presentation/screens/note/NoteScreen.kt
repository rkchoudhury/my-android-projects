package com.example.cleanarchitecture.presentation.screens.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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


@Composable
fun NoteScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            NaviBar(title = "New Note", showBack = true, onClick = {
                navController.popBackStack()
            })
            Body()
        }
        FloatingButton(
            icon = Icons.Filled.Done,
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            description = "Done button"
        )
    }

}

@Composable
fun Body() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)) {
        TextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
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