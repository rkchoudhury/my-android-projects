package com.example.cleanarchitecture.presentation.screens.note

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecture.framework.viewmodels.NoteViewModel
import com.example.cleanarchitecture.presentation.common.FloatingButton
import com.example.cleanarchitecture.presentation.common.NaviBar
import com.example.core.data.Note


@Composable
fun NoteScreen(navController: NavController) {
    val viewModel: NoteViewModel = viewModel()
    val context = LocalContext.current
    val currentNote = remember { Note("", "", 0L, 0L) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val saved by viewModel.saved.observeAsState()

    LaunchedEffect(saved) {
        saved?.let {
            if (it) {
                Toast.makeText(context, "Note created successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } else {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            NaviBar(title = "New Note", showBack = true, onClick = {
                navController.popBackStack()
            })
            Body(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it }
            )
        }
        FloatingButton(
            icon = Icons.Filled.Done,
            onClick = {
                if (title != "" || description != "") {
                    val time = System.currentTimeMillis()
                    currentNote.title = title
                    currentNote.content = description
                    currentNote.updateTime = time

                    if (currentNote.id == 0L) {
                        currentNote.creationTime = time
                    }

                    viewModel.saveNote(currentNote)
                } else {
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            description = "Done button"
        )
    }
}

@Composable
fun Body(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        TextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteScreenPreview() {
    val navController = rememberNavController()
    NoteScreen(navController)
}