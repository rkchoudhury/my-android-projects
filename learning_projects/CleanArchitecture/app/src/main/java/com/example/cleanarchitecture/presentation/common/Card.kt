package com.example.cleanarchitecture.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview
@Composable
fun PreviewCard() {
    Card("Title Name", "Description")
}