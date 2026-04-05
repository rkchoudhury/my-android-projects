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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecture.presentation.common.Card
import com.example.cleanarchitecture.presentation.common.FloatingButton
import com.example.cleanarchitecture.presentation.common.NaviBar
import com.example.cleanarchitecture.presentation.navigation.Route

@Composable
fun NoteListScreen(navController: NavController) {
    val list = listOf(
        "A", "B", "C", "D", "E", "F", "A", "B", "C", "D", "E", "F", "D", "E", "F"
    )

    Column {
        NaviBar("Notes", navController)
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                items(items = list, itemContent = { item ->
                    Card(title = item, content = item.repeat(10))
                })
            }

            FloatingButton(
                Icons.Filled.Add,
                onClick = {
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