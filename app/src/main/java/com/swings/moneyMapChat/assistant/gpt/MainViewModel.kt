package com.swings.moneyMapChat.assistant.gpt

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.swings.moneyMapChat.assistant.gpt.fetchdata.ExpenseData
import com.swings.moneyMapChat.assistant.gpt.fetchdata.getExpenseData
import com.swings.moneyMapChat.profile.CreditScoreViewModel
import com.swings.moneyMapChat.profile.UserProfile
import com.swings.moneyMapChat.utils.CreditScoreUtils.calculateCreditScore
import com.swings.moneyMapChat.utils.CreditScoreUtils.getTimeframeInMonths
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val gptQuery = mutableStateOf("")
    val word = mutableStateOf("")
    val chatHistory = mutableStateListOf<ChatMessage>()
    val creditScore = mutableStateOf(0)


    @OptIn(BetaOpenAI::class)
    fun getGPTResponse(realm: Realm) {
        viewModelScope.launch {
            val openAI = OpenAI(CHAT_GPT_API_KEY)

            try {
                val expenseData = getExpenseData(realm)
                val userProfile = getUserProfileData(realm)

                // Calculate credit score
                userProfile?.let {
                    creditScore.value = calculateCreditScore(
                        it.monthlyIncome,
                        expenseData.totalExpenses,
                        it.savingsGoal,
                        getTimeframeInMonths(it.savingsGoalTimeframe)
                    )
                }
                val promptWithHistory = buildPrompt(gptQuery.value,
                    chatHistory,
                    expenseData,
                    userProfile,
                    creditScore.value )

                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = promptWithHistory
                        )
                    )
                )

                val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
                val response = completion.choices.first().message?.content ?: ""

                chatHistory.add(
                    ChatMessage(
                        "User",
                        gptQuery.value
                    )
                )
                chatHistory.add(
                    ChatMessage(
                        "Assistant",
                        response
                    )
                )

                word.value = response

            } catch (e: Exception) {
                Log.d("MainViewModel", "getGPTResponse: ERROR: ${e.message ?: ""}")
            }
        }}

    private fun getUserProfileData(realm: Realm): UserProfile? {
        return realm.query<UserProfile>().first().find()
    }
    private fun buildPrompt(userInput: String,
                            history: List<ChatMessage>,
                            expenseData: ExpenseData,
                            userProfile: UserProfile?,
                            creditScore: Int ): String {
        val context = history.joinToString("\n") { "${it.sender}: ${it.content}" }
        val systemMessage = "You are a financial assistant of moneyMapChat, a financial specialist, " +
                "budgeting and tracking users expenses and have no other knowledge."

        val expenseInfo = """
            Recent Expenses: ${expenseData.recentExpenses.joinToString { "${it.amount} in ${it.category?.name} (${it.recurrence})" }}
            Total Expenses: ${expenseData.totalExpenses}
            Category Expenses: ${expenseData.categoryExpenses.entries.joinToString { "${it.key}: ${it.value}" }}
            Recurrence Expenses: ${expenseData.recurrenceExpenses.entries.joinToString { "${it.key}: ${it.value}" }}
        """.trimIndent()

        val userInfo = userProfile?.let {
            "User's name is ${it.username}," +
                    "they earn ${it.monthlyIncome} monthly and work as a ${it.occupation}."
        } ?: ""

        val savingsInfo = userProfile?.let {
            "User's savings goal is ${it.savingsGoal} to be achieved in ${it.savingsGoalTimeframe}."
        } ?: ""

        val creditInfo = "User's current credit score is $creditScore."

        return "$systemMessage\n$context\n$userInfo\n$savingsInfo\n$creditInfo\nExpense Information:\n$expenseInfo\nUser: $userInput"
    }




    companion object {
        const val CHAT_GPT_API_KEY = "sk-proj-1HnZCZPCwSDe4XtCnUhRT3BlbkFJyfuLWzkBbjJmX91anbrI" // Replace with your actual key
    }
}