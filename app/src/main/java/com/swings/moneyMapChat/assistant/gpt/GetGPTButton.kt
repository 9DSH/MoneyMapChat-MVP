package com.swings.moneyMapChat.assistant.gpt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.realm.kotlin.Realm

@Composable
fun GetGPTButton(viewModel: MainViewModel, text: MutableState<String>, realm: Realm ) {
    Icon(
        modifier = Modifier
            .size(40.dp)
            .clickable {viewModel.getGPTResponse(realm)
                text.value = ""
            },
        imageVector = Icons.Rounded.Send,
        contentDescription = "Send prompt",
        tint = Color.White
    )
}