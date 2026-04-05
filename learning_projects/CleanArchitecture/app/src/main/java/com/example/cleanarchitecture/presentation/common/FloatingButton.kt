package com.example.cleanarchitecture.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FloatingButton(
    icon: ImageVector,
    onClick: () -> Unit,
    description: String = "",
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        Icon(icon, description)
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewFloatingButton() {
    FloatingButton(Icons.Filled.Done, {}, "Done button")
}