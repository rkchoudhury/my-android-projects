package com.example.cleanarchitecture.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun Card(title: String, content: String, updateTime: Long, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("MMM dd, HH:mm:ss")
    val resultDate = sdf.format(Date(updateTime))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp, 20.dp, 0.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = content, fontSize = 16.sp)
            Text(
                text = "Last updated: $resultDate",
                fontSize = 12.sp,
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
            )
        }

    }
}

@Preview
@Composable
fun PreviewCard() {
    Card("Title Name", "Description", 0L, {})
}