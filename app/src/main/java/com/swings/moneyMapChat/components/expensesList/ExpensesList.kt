package com.swings.moneyMapChat.components.expensesList

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swings.moneyMapChat.components.ExpensesDayGroup
import com.swings.moneyMapChat.mock.mockExpenses
import com.swings.moneyMapChat.models.Expense
import com.swings.moneyMapChat.models.groupedByDay
import com.swings.moneyMapChat.ui.theme.GoodbyeMoneyTheme

@Composable
fun ExpensesList(expenses: List<Expense>, modifier: Modifier = Modifier) {
  val groupedExpenses = expenses.groupedByDay()

  Column(modifier = modifier) {
    if (groupedExpenses.isEmpty()) {
      Text("No data for selected date range.", modifier = Modifier.padding(top = 32.dp))
    } else {
      groupedExpenses.keys.forEach { date ->
        if (groupedExpenses[date] != null) {
          ExpensesDayGroup(
            date = date,
            dayExpenses = groupedExpenses[date]!!,
            modifier = Modifier.padding(top = 24.dp)
          )
        }
      }
    }
  }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun Preview() {
  GoodbyeMoneyTheme {
    ExpensesList(mockExpenses)
  }
}