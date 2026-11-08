package com.swings.moneyMapChat.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.swings.moneyMapChat.profile.UserProfile
import com.swings.moneyMapChat.assistant.gpt.fetchdata.ExpenseData
import com.swings.moneyMapChat.utils.CreditScoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CreditScoreViewModel @Inject constructor() : ViewModel() {
    private val _creditScore = MutableStateFlow(0)
    val creditScore: StateFlow<Int> = _creditScore.asStateFlow()

    fun calculateScore(userProfile: UserProfile?, expenseData: ExpenseData) {
        userProfile?.let {
            _creditScore.value = CreditScoreUtils.calculateCreditScore(
                it.monthlyIncome,
                expenseData.totalExpenses,
                it.savingsGoal,
                CreditScoreUtils.getTimeframeInMonths(it.savingsGoalTimeframe)
            )
        }
    }
}