package com.swings.moneyMapChat.assistant.gpt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swings.moneyMapChat.ui.theme.BackgroundElevated
import io.realm.kotlin.Realm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGPT(
    viewModel: MainViewModel = hiltViewModel(), realm: Realm
) {
    val text = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        GPTResponseList(viewModel.chatHistory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                modifier = Modifier.weight(1f),
                value = text.value,
                onValueChange = {
                    text.value = it
                    viewModel.gptQuery.value = it
                },
                placeholder = { Text(text = "Type a command") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White, // Text color inside the field
                    containerColor = BackgroundElevated, // Background color of the field
                    focusedIndicatorColor = Color.Transparent, // Hide the focused indicator
                    unfocusedIndicatorColor = Color.Transparent // Hide the unfocused indicator
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            GetGPTButton(viewModel = viewModel, text = text, realm = realm  )
        }
    }
}

@Composable
fun GPTResponseList(chatHistory: List<ChatMessage>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 10.dp)
    ) {
        items(chatHistory) { message ->
            ChatMessageItem(message) // Use ChatMessageItem to display each message
           // Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val backgroundColor = if (message.sender == "User") Color(0xff003366) else Color.Black
    val textColor = if (message.sender == "User") Color.White else Color.LightGray

    Column(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .padding(16.dp),
            text = message.content,
            fontSize = 17.sp,
            color = textColor
        )
    }
}

