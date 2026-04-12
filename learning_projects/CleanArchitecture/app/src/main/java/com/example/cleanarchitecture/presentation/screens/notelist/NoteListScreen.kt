package com.example.cleanarchitecture.presentation.screens.notelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecture.framework.viewmodels.NoteListViewModel
import com.example.cleanarchitecture.presentation.common.Card
import com.example.cleanarchitecture.presentation.common.FloatingButton
import com.example.cleanarchitecture.presentation.common.NaviBar
import com.example.cleanarchitecture.presentation.navigation.Route

@Composable
fun NoteListScreen(navController: NavController) {
    val viewModel: NoteListViewModel = viewModel()
    val notes by viewModel.allNotes.observeAsState()

    LifecycleResumeEffect(Unit) {
        // This function is called every time the lifecycle enters ON_RESUME
        viewModel.getAllNotes()

        onPauseOrDispose {
            // Optional: Clean up when leaving ON_RESUME
        }
    }

    Column {
        NaviBar("Notes") { navController.popBackStack() }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                notes?.let { it ->
                    if (it.isNotEmpty()) {
                        items(
                            items = it.sortedByDescending { it.updateTime },
                            itemContent = { item ->
                                Card(title = item.title, content = item.content, item.updateTime, {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "id",
                                        item.id
                                    )
                                    navController.navigate(Route.Note.name)
                                })
                            })
                    }
                }
            }

            FloatingButton(
                Icons.Filled.Add,
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("id", 0L)
                    navController.navigate(Route.Note.name)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                description = "Add Button"
            )
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteListScreenPreview() {
    val navController = rememberNavController()
    NoteListScreen(navController)
}