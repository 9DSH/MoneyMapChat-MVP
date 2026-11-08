package com.swings.moneyMapChat.assistant.gpt.fetchdata


import com.swings.moneyMapChat.models.Expense
import com.swings.moneyMapChat.models.Recurrence
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

// Data class to hold expense information
data class ExpenseData(
    val recentExpenses: List<Expense>,
    val totalExpenses: Double,
    val categoryExpenses: Map<String, Double>,
    val recurrenceExpenses: Map<Recurrence, Double>
)

// Function to fetch expense data from Realm
fun getExpenseData(realm: Realm): ExpenseData {
    val recentExpenses = realm.query<Expense>().find().toList()
    val totalExpenses = recentExpenses.sumOf { it.amount }

    val categoryExpenses = mutableMapOf<String, Double>()
    recentExpenses.forEach { expense ->
        val categoryName = expense.category?.name ?: "Uncategorized"
        categoryExpenses[categoryName] = (categoryExpenses[categoryName] ?: 0.0) + expense.amount
    }

    val recurrenceExpenses = mutableMapOf<Recurrence, Double>()
    recentExpenses.forEach { expense ->
        recurrenceExpenses[expense.recurrence] = (recurrenceExpenses[expense.recurrence] ?: 0.0) + expense.amount
    }

    return ExpenseData(
        recentExpenses,
        totalExpenses,
        categoryExpenses,
        recurrenceExpenses
    )
}