package com.swings.moneyMapChat.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nikolovlazar.moneyMapChat.R
import com.swings.moneyMapChat.components.PickerTrigger
import com.swings.moneyMapChat.components.expensesList.ExpensesList
import com.swings.moneyMapChat.models.Recurrence
import com.swings.moneyMapChat.ui.theme.DividerColor
import com.swings.moneyMapChat.ui.theme.LabelSecondary
import com.swings.moneyMapChat.ui.theme.Typography
import com.swings.moneyMapChat.viewmodels.CategoriesViewModel
import com.swings.moneyMapChat.viewmodels.ExpensesViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Expenses(
  navController: NavController,
  vm: ExpensesViewModel = viewModel(),
  vmCat: CategoriesViewModel = viewModel()
) {
  val recurrences = listOf(
    Recurrence.Daily,
    Recurrence.Weekly,
    Recurrence.Monthly,
    Recurrence.Yearly
  )

  val state by vm.uiState.collectAsState()
  val catState by vmCat.uiState.collectAsState()

  val categories = catState.categories

  var recurrenceMenuOpened by remember {
    mutableStateOf(false)
  }

  Scaffold(
    topBar = {
      MediumTopAppBar(
        title = { Text("Expenses") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = Color(0xff003366),
          titleContentColor = Color.White
        )

      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { navController.navigate("add")}, // Navigate to "add" route
        containerColor = Color(0xff003366) // Customize as needed
      ) {
        Icon(
          imageVector = Icons.Filled.Add, // Use the add icon
          contentDescription = "Add Expense"
        )
      }
    },
    //floatingActionButtonPosition = FabPosition.Center,

    content = { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 16.dp)
          .padding(top = 16.dp)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            "Total Expense for:",
            style = Typography.bodyMedium,
          )
          PickerTrigger(
            state.recurrence.target ?: Recurrence.None.target,
            onClick = { recurrenceMenuOpened = !recurrenceMenuOpened },
            modifier = Modifier.padding(start = 16.dp)
          )
          Spacer(modifier = Modifier.width(16.dp))
          DropdownMenu(expanded = recurrenceMenuOpened,
            onDismissRequest = { recurrenceMenuOpened = false }
            ) {
            recurrences.forEach { recurrence ->
              DropdownMenuItem(text = { Text(recurrence.target) }, onClick = {
                vm.setRecurrence(recurrence)
                recurrenceMenuOpened = false
              })
            }
          }
        }
        

        Row(modifier = Modifier.padding(vertical = 32.dp)) {
          Text(
            "$",
            style = Typography.bodyMedium,
            color = LabelSecondary,
            modifier = Modifier.padding(end = 4.dp, top = 4.dp)
          )
          Text(
            DecimalFormat("0.#").format(state.sumTotal),
            style = Typography.titleLarge
          )
        }





        Divider(
          modifier = Modifier.padding(start = 16.dp),
          thickness = 1.dp,
          color = DividerColor
        )

        ClickableText(text = "Categories", onClick = { navController.navigate("settings/categories")})

        LazyRow{


                items(categories){cat->
                  Column(
                    modifier = Modifier
                      .padding(horizontal = 16.dp)
                      .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                  {

                    Text(modifier = Modifier.padding(8.dp), text = cat.name, fontSize = 12.sp)


                    ElevatedCard(modifier = Modifier
                      .padding(16.dp)
                      .size(50.dp),
                      onClick = { navController.navigate("add") }) {
                      Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = cat.name,
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = ColorFilter.tint(cat.color)
                      )

                    }
                  }



                }
              }




        ExpensesList(
          expenses = state.expenses,
          modifier = Modifier
            .weight(1f)
            .verticalScroll(
              rememberScrollState()
            )
        )
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  )
}

@Composable
fun ClickableText(text: String, onClick: () -> Unit) {
  Text(
    text = text,
    fontSize = 20.sp,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(16.dp)
  )
}
