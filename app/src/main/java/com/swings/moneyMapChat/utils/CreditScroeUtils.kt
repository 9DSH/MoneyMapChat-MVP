package com.swings.moneyMapChat.utils

object CreditScoreUtils {

    fun calculateCreditScore(
        monthlyIncome: Double,
        totalExpenses: Double,
        savingsGoal: Double,
        savingsTimeframeMonths: Int
    ): Int {
        val idealMonthlySavings = savingsGoal / savingsTimeframeMonths
        val actualMonthlySavings = monthlyIncome - totalExpenses

        var score = 0
        if (actualMonthlySavings >= idealMonthlySavings) {
            score += 50
        }
        if (totalExpenses <= 0.7 * monthlyIncome) {
            score += 30
        }
        if(savingsGoal > 0) {
            score += 20
        }

        return score.coerceIn(0, 100)
    }

    fun getTimeframeInMonths(timeframe: String): Int {
        val numberRegex = "\\d+".toRegex()
        val numberMatch = numberRegex.find(timeframe)
        val number = numberMatch?.value?.toIntOrNull() ?: 0

        return when {
            timeframe.contains("year", ignoreCase = true) -> number * 12
            timeframe.contains("month", ignoreCase = true) -> number
            timeframe.contains("Weeks", ignoreCase = true) -> number * 4
            else -> 0 // Handle cases where the format is not recognized
        }
    }
}