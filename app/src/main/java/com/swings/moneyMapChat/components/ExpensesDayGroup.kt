package com.swings.moneyMapChat.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swings.moneyMapChat.models.DayExpenses
import com.swings.moneyMapChat.ui.theme.LabelSecondary
import com.swings.moneyMapChat.ui.theme.Typography
import com.swings.moneyMapChat.utils.formatDay
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun ExpensesDayGroup(
  date: LocalDate,
  dayExpenses: DayExpenses,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      date.formatDay(),
      style = Typography.headlineMedium,
      color = LabelSecondary
    )
    Divider(modifier = Modifier.padding(top = 10.dp, bottom = 4.dp))
    dayExpenses.expenses.forEach { expense ->
      ExpenseRow(
        expense = expense,
        modifier = Modifier.padding(top = 12.dp)
      )
    }
    Divider(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Total:", style = Typography.bodyMedium, color = LabelSecondary)
      Text(
        DecimalFormat("USD 0.#").format(dayExpenses.total),
        style = Typography.headlineMedium,
        color = LabelSecondary
      )
    }
  }
}