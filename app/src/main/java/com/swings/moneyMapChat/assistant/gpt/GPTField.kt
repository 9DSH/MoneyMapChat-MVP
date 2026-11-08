package com.swings.moneyMapChat.assistant.gpt

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun GPTFieldPreview() {
    GPTField()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPTField(
    gptQuery: MutableState<String>? = null
) {
    val text = remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.wrapContentSize(),
        value = text.value,
        onValueChange = {
            text.value = it
            gptQuery?.value = it
        }
    )
}
