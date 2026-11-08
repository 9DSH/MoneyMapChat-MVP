package com.swings.moneyMapChat.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.nikolovlazar.moneyMapChat.R
import com.swings.moneyMapChat.components.TableRow
import com.swings.moneyMapChat.components.UnstyledTextField
import com.swings.moneyMapChat.models.Recurrence
import com.swings.moneyMapChat.ui.theme.BackgroundElevated
import com.swings.moneyMapChat.ui.theme.DividerColor
import com.swings.moneyMapChat.ui.theme.Shapes
import com.swings.moneyMapChat.ui.theme.Typography
import com.swings.moneyMapChat.viewmodels.AddViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Add(navController: NavController,
        vm: AddViewModel = viewModel()) {
  val state by vm.uiState.collectAsState()

  val recurrences = listOf(
    Recurrence.None,
    Recurrence.Daily,
    Recurrence.Weekly,
    Recurrence.Monthly,
    Recurrence.Yearly
  )

  Scaffold(topBar = {
    MediumTopAppBar(
      title = { Text("Add") },
      colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = Color(0xff003366),
        titleContentColor = Color.White
      )
    )
  }, content = { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .clip(Shapes.large)
          .background(BackgroundElevated)
          .fillMaxWidth()
      ) {
        TableRow(label = "Amount", detailContent = {
          UnstyledTextField(
            value = state.amount,
            onValueChange = vm::setAmount,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0")},
            arrangement = Arrangement.End,
            maxLines = 1,
            textStyle = TextStyle(
              textAlign = TextAlign.Right,
            ),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Number,
            )
          )
        })
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        TableRow(label = "Recurrence", detailContent = {
          var recurrenceMenuOpened by remember {
            mutableStateOf(false)
          }
          TextButton(
            onClick = { recurrenceMenuOpened = true }, shape = Shapes.large
          ) {
            Text(state.recurrence?.name ?: Recurrence.None.name)
            DropdownMenu(expanded = recurrenceMenuOpened,
              onDismissRequest = { recurrenceMenuOpened = false }) {
              recurrences.forEach { recurrence ->
                DropdownMenuItem(text = { Text(recurrence.name) }, onClick = {
                  vm.setRecurrence(recurrence)
                  recurrenceMenuOpened = false
                })
              }
            }
          }
        })
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        var datePickerShowing by remember {
          mutableStateOf(false)
        }
        TableRow(label = "Date", detailContent = {
          TextButton(onClick = { datePickerShowing = true }) {
            Text(state.date.toString())
          }
          if (datePickerShowing) {
            DatePickerDialog(onDismissRequest = { datePickerShowing = false },
              onDateChange = { it ->
                vm.setDate(it)
                datePickerShowing = false
              },
              initialDate = state.date,
              title = { Text("Select date", style = Typography.titleLarge) })
          }
        })
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )
        TableRow(label = "Note", detailContent = {
          UnstyledTextField(
            value = state.note,
            placeholder = { Text("Leave some notes") },
            arrangement = Arrangement.End,
            onValueChange = vm::setNote,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
              textAlign = TextAlign.Right,
            ),
          )
        })
        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )

        /// Select Category


        TableRow(label = "Category", detailContent = {
          var categoriesMenuOpened by remember {
            mutableStateOf(false)
          }
          TextButton(
            onClick = { categoriesMenuOpened = true }, shape = Shapes.large
          ) {
            Text(
              state.category?.name ?: "Select a category",
              color = state.category?.color ?: Color.White
            )
            DropdownMenu(expanded = categoriesMenuOpened,
              onDismissRequest = { categoriesMenuOpened = false }) {




              state.categories?.forEach { category ->
                DropdownMenuItem(text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                      modifier = Modifier.size(10.dp),
                      shape = CircleShape,
                      color = category.color
                    ) {}
                    Text(
                      category.name, modifier = Modifier.padding(start = 8.dp)
                    )
                  }

                }, onClick = {
                  vm.setCategory(category)
                  categoriesMenuOpened = false
                    })

              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                  onClick = { navController.navigate("Settings/Categories") }
                ) {
                  Image(painter = painterResource(id = R.drawable.add), contentDescription = "Add Category")
                  Text(text = "Add Category", modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                }

              }



            }
          }
        })
      }
      Button(
        onClick = {vm.submitExpense()
                   navController.popBackStack()},

        modifier = Modifier.padding(16.dp),
        shape = Shapes.large,
        enabled = state.category != null
      ) {
        Text("Submit expense")
      }
    }
  })
}
