package com.example.cleanarchitecture.presentation.screens.notelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleanarchitecture.ui.theme.CleanArchitectureTheme


@Composable
fun NoteListScreen() {
    val list = listOf(
        "A", "B", "C", "D"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 25.dp, 0.dp, 0.dp)
        ) {
            items(items = list, itemContent = { item ->
                Card(title = item, content = item.repeat(10))
            })
        }

        FloatingButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

}

@Composable
fun Card(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp, 20.dp, 0.dp)
            .background(Color.LightGray)
            .padding(10.dp)
    ) {
        Text(text = title)
        Text(text = content)
    }
}

@Composable
fun FloatingButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteListScreenPreview() {
    CleanArchitectureTheme {
        NoteListScreen()
    }
}