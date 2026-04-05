package com.example.cleanarchitecture.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanarchitecture.R

@Composable
fun NaviBar(title: String, showBack: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(R.color.black)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "back_button",
                tint = colorResource(R.color.white),
                modifier = Modifier.clickable(
                    onClick = {
                        onClick()
                    }
                )
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white),
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewNaviBar() {
    NaviBar("Movie Preview")
}