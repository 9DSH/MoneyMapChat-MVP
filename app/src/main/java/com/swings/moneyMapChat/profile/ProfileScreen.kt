package com.swings.moneyMapChat.profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swings.moneyMapChat.assistant.gpt.MainViewModel
import com.swings.moneyMapChat.assistant.gpt.fetchdata.getExpenseData
import com.swings.moneyMapChat.db
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import com.swings.moneyMapChat.ui.theme.BackgroundElevated
import com.swings.moneyMapChat.viewmodels.ExpensesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(creditScoreViewModel: CreditScoreViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val userProfile by remember { mutableStateOf(loadUserProfile(db)) }
    val expenseData by remember { mutableStateOf(getExpenseData(db)) }

    // State variables to hold profile information
    var username by remember { mutableStateOf(loadUsername(db)) }
    var monthlyIncome by remember { mutableStateOf(loadMonthlyIncome(db).takeIf { it != 0.0 }?.toString() ?: "") }
    var occupation by remember { mutableStateOf(loadOccupation(db)) }
    // State variables for savings goal
    var savingsGoal by remember { mutableStateOf(loadSavingsGoal(db).takeIf { it != 0.0 }?.toString() ?: "") }
    var savingsGoalTimeframe by remember { mutableStateOf(loadSavingsGoalTimeframe(db)) }
    var expanded by remember { mutableStateOf(false) }

    // Calculate score whenever userProfile or expenseData changes
    LaunchedEffect(key1 = userProfile, key2 = expenseData) {
        creditScoreViewModel.calculateScore(userProfile, expenseData)
    }

    val score by creditScoreViewModel.creditScore.collectAsState()


    // Scaffold provides basic structure for the screen
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xff003366),
                    titleContentColor = Color.White
                ),
                actions = {// Display credit score with Badge
                    val offsetX = (-180).dp // Adjust left offset (negative value moves left)
                    val offsetY = (18).dp // Adjust down offset (positive value moves down)
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xff090c16),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .offset(x = 1.dp, y = (16).dp)
                            ) {
                                Text(
                                    text = "Score: $score",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .offset(x = offsetX, y = offsetY) // Add padding for better spacing
                    ) {
                        Row {
                            Text(
                                text = "Your Credit Score",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Credit Score",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    }
                }

            )
        }

    ) { innerPadding ->Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(scrollState), // Make the Column scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // Profile Picture section


            // Personal Information section (OutlinedTextFields)
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)


        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Face, // Icon for personal info
                        contentDescription = "PersonalInformation Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { newUsername ->
                        username = newUsername
                        coroutineScope.launch {
                            saveUsername(db, newUsername)
                        }
                    },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = monthlyIncome,
                    onValueChange = { newIncome ->
                        monthlyIncome = newIncome
                        val incomeValue = if (newIncome.isBlank()) 0.0 else newIncome.toDoubleOrNull() ?: 0.0
                        coroutineScope.launch {
                            saveMonthlyIncome(db, incomeValue)
                        }
                    },
                    label = { Text("Monthly Income") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = occupation,
                    onValueChange = { newOccupation ->
                        occupation = newOccupation
                        coroutineScope.launch {
                            saveOccupation(db, newOccupation)
                        }
                    },
                    label = { Text("Occupation") },
                    placeholder = { Text("Write more about your source of income") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Space between sections
        // Savings Goal Section (using Card)
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color(0xff090c16)),
            onClick = { expanded = !expanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Savings Goals Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Savings Goals",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (expanded) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = savingsGoal,
                        onValueChange = { newValue ->
                            savingsGoal = newValue
                            val goalValue = if (newValue.isBlank()) 0.0 else newValue.toDoubleOrNull() ?: 0.0
                            coroutineScope.launch {
                                saveSavingsGoal(db, goalValue)
                            }
                        },
                        label = { Text("Savings Goal Amount") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = savingsGoalTimeframe,
                        onValueChange = { newValue ->
                            savingsGoalTimeframe = newValue
                            coroutineScope.launch {
                                saveSavingsGoalTimeframe(db, newValue)
                            }
                        },
                        label = { Text("Goal Timeframe (e.g., 6 months)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }






        }
    }
}

// Helper functions to load and save profile data


private fun loadUsername(realm: Realm): String {
    val userProfile = realm.query<UserProfile>().first().find()
    return userProfile?.username ?: ""
}

private fun saveUsername(realm: Realm, username: String) {
    realm.writeBlocking {
        val userProfile = query<UserProfile>().first().find() ?: UserProfile()
        userProfile.username = username
        copyToRealm(userProfile)
    }
}


private fun loadMonthlyIncome(realm: Realm): Double {
    val userProfile = realm.query<UserProfile>().first().find()
    return userProfile?.monthlyIncome?: 0.0
}

private fun saveMonthlyIncome(realm: Realm, monthlyIncome: Double) {
    realm.writeBlocking {
        val userProfile = query<UserProfile>().first().find() ?: UserProfile()
        userProfile.monthlyIncome = monthlyIncome
        copyToRealm(userProfile)
    }
}

private fun loadOccupation(realm: Realm): String {
    val userProfile = realm.query<UserProfile>().first().find()
    return userProfile?.occupation ?: ""
}

private fun saveOccupation(realm: Realm, occupation: String) {
    realm.writeBlocking {
        val userProfile = query<UserProfile>().first().find() ?: UserProfile()
        userProfile.occupation = occupation
        copyToRealm(userProfile)
    }
}
private fun loadSavingsGoal(realm: Realm): Double {
    val userProfile = realm.query<UserProfile>().first().find()
    return userProfile?.savingsGoal ?: 0.0
}

private fun saveSavingsGoal(realm: Realm, savingsGoal: Double) {
    realm.writeBlocking {
        val userProfile = query<UserProfile>().first().find() ?: UserProfile()
        userProfile.savingsGoal = savingsGoal
        copyToRealm(userProfile)
    }
}

private fun loadSavingsGoalTimeframe(realm: Realm): String {
    val userProfile = realm.query<UserProfile>().first().find()
    return userProfile?.savingsGoalTimeframe ?: ""
}

private fun saveSavingsGoalTimeframe(realm: Realm, timeframe: String) {
    realm.writeBlocking {
        val userProfile = query<UserProfile>().first().find() ?: UserProfile()
        userProfile.savingsGoalTimeframe = timeframe
        copyToRealm(userProfile)
    }
}
private fun loadUserProfile(realm: Realm): UserProfile? {
    return realm.query<UserProfile>().first().find()
}