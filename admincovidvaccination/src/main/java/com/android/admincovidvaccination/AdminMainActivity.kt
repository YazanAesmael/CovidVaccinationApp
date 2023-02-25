package com.android.admincovidvaccination

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.admincovidvaccination.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

var userName = ""
var userState = ""
var userAge = ""
var userVaccine = ""
var userDoses = ""
var userDoseDate = ""
var userNumber = ""
var userIsBooked = "false"
var adminSlots = 0
val userDetails = mutableListOf<UserDetails>()
var userCountAdmin = 0

class AdminMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CovidVaccinationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AdminMainViewModel()
                    AdminNavigation()
                }
            }
        }
    }
}

@Composable
fun SplashScreen( navController: NavController) {
    AdminMainViewModel()
    getSlots()
    auth = FirebaseAuth.getInstance()
    var isVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        key1 = true,
        block = {
            isVisible = true
            delay(2000L)
            isVisible = false
            delay(100L)
            navController.navigate("home_screen")
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(0.90f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + expandHorizontally { 1 } + expandVertically { 1 },
                exit = fadeOut() + shrinkVertically { 0 }
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(360.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(180.dp)),
                        painter = painterResource(id = R.drawable.covid),
                        contentDescription = "Blood Donation Logo",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Box(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = "Vaccination App\nAdmin Panel",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun UsersLazyColumn(viewModel: AdminMainViewModel) {
    when (val result = viewModel.response.value) {
        is UserState.Loading -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "", tint = Color.White, modifier = Modifier.fillMaxWidth())
            }
        }
        is UserState.Empty-> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "", tint = Color.White, modifier = Modifier.fillMaxWidth())
            }
        }
        is UserState.Success -> {
            SetUserColumn(result.postDetails)
        }
        is UserState.Failure -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "", tint = Color.White, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SetUserColumn(adminUserDetails: MutableList<UserDetails>) {
    val userCount = adminUserDetails.size
    userCountAdmin = userCount
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Users Database",
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = OnBackgroundColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold

        )
        Spacer(
            modifier = Modifier
                .padding(start = 60.dp, end = 60.dp, top = 6.dp, bottom = 10.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(OnBackgroundColor)
        )
        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(BackgroundColor)
        ) {
            items(adminUserDetails) { user ->
                var isUserClicked by remember {
                    mutableStateOf(false)
                }
                Column(
                    modifier = Modifier
                        .padding(bottom = 4.dp, start = 10.dp, top = 4.dp, end = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = user.name,
                        color = TextColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(OnBackgroundColor, RoundedCornerShape(10.dp))
                            .border(1.dp, TextColor, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                            .clickable {
                                isUserClicked = !isUserClicked
                            },
                        textAlign = TextAlign.Center,

                        )
                    AnimatedVisibility(
                        visible = isUserClicked,
                        exit = fadeOut() + shrinkVertically { 0 },
                        enter = fadeIn() + expandVertically { 1 }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .background(Color.Transparent)
                        ) {
                            val booked = if (user.isBooked == "null") "not booked for vaccine" else "booked for vaccine"

                            UsersItem(text = "State", buttonText = user.state) {}
                            UsersItem(text = "Age", buttonText = user.age) {}
                            UsersItem(text = "Number", buttonText = user.userNumber) {}
                            UsersItem(text = "Vaccine", buttonText = user.vaccine) {}
                            UsersItem(text = "Dose", buttonText = user.dose) {}
                            UsersItem(text = "Latest Dose Date", buttonText = user.doseDate) {}
                            UsersItem(text = "Booked Appointment", buttonText = booked) {}
                            Spacer(
                                modifier = Modifier
                                    .padding(
                                        start = 10.dp,
                                        end = 10.dp,
                                        top = 20.dp,
                                        bottom = 10.dp
                                    )
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(OnBackgroundColor, RoundedCornerShape(10.dp))
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    AdminMainViewModel()
    val context = LocalContext.current
    var slotsNum by remember {
        mutableStateOf(adminSlots)
    }
    var isSlotClicked by remember {
        mutableStateOf(false)
    }
    var isAdminClicked by remember {
        mutableStateOf(false)
    }
    var isDeleteClicked by remember {
        mutableStateOf(false)
    }
    if (isSlotClicked) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = BackgroundColor,
            onDismissRequest = {
                isSlotClicked = !isSlotClicked
            },
            title = null,
            text = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                ) {
                    Text(
                        text = "Update Available Slots",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = OnBackgroundColor
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 6.dp, bottom = 40.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OnBackgroundColor)
                    )
                    Item("Number of available slots", slotsNum.toString()) {}
                    Row(modifier = Modifier
                        .padding(top = 0.dp, bottom = 0.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                slotsNum -= 1
                            },
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = OnBackgroundColor
                            ),
                            shape = RoundedCornerShape(10.dp)

                        ) {
                            Icon(painter = painterResource(id = R.drawable.minus), contentDescription = "", tint = BackgroundColor)
                        }
                        Button(
                            onClick = {
                                slotsNum += 1
                            },
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = OnBackgroundColor
                            ),
                            shape = RoundedCornerShape(10.dp)

                        ) {
                            Icon(painter = painterResource(id = R.drawable.plus), contentDescription = "", tint = BackgroundColor)
                        }
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp, end = 8.dp, start = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp)
                            .fillMaxWidth()
                            .focusable(true),
                        onClick = {
                            updateSlots(context, slotsNum)
                            isSlotClicked = !isSlotClicked
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = OnBackgroundColor,
                            contentColor = TextColor,

                            )
                    ) {
                        Text("Update", fontWeight = FontWeight.Normal)
                    }
                }
            },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #1
    if (isAdminClicked) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = BackgroundColor,
            onDismissRequest = {
                isAdminClicked = !isAdminClicked
            },
            title = null,
            text = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                ) {
                    Text(
                        text = "Contact Us",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 6.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OnBackgroundColor)
                    )
//                    Item(text = "Website", buttonText = "www.progressiveorigin.com") {
//                        uriHandler.openUri("https://www.progressiveorigin.com/")
//                    }
//                    Item(text = "Email", buttonText = "supporteam@progorgn.com") {}
                }
            },
            buttons = {  },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #2
    if (isDeleteClicked) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = BackgroundColor,
            onDismissRequest = {
                isDeleteClicked = !isDeleteClicked
            },
            title = null,
            text = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                ) {
                    Text(
                        text = "All your data will be deleted...",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = OnBackgroundColor
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp, end = 8.dp, start = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .padding(start = 30.dp, end = 30.dp)
                            .fillMaxWidth()
                            .focusable(true),
                        onClick = {
                            FirebaseAuth.getInstance().signOut().let {
                                navController.navigate("login_screen") {
                                    popUpTo(0)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = OnBackgroundColor,
                            contentColor = TextColor,

                            )
                    ) {
                        Text("Delete", fontWeight = FontWeight.Normal, color = RedTextColor)
                    }
                }
            },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .weight(1.5f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 10.dp)
                        .size(160.dp, 160.dp)
                        .clip(RoundedCornerShape(90.dp))
                        .background(OnBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.covid),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(90.dp)),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                } // User Profile Picture
                Text(
                    text = "Covid Vaccination",
                    fontSize = 32.sp,
                    color = OnBackgroundColor,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Admin Panel",
                    fontSize = 22.sp,
                    color = GreenTextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
                if (userCountAdmin > 0) {
                    Text(
                        text = "$userCountAdmin users",
                        fontSize = 16.sp,
                        color = OnBackgroundColor,
                        modifier = Modifier
                            .background(BackgroundColor, RoundedCornerShape(90.dp))
                            .border(Dp.Hairline, OnBackgroundColor, RoundedCornerShape(90.dp))
                            .padding(top = 2.dp, bottom = 2.dp, start = 10.dp, end = 10.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 0.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(OnBackgroundColor)
                )
            }
        } // Top Box
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                LightButtonCompose("Update Available Slots") {
                    isSlotClicked = !isSlotClicked
                }
                LightButtonCompose("Users Information") {
                    navController.navigate("user_info_screen")
                }
            }
        } // Bottom Box
    } // Profile Screen
}

@Composable
fun Item(text: String, buttonText: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = OnBackgroundColor
    )
//    TextFieldCompose("2", false, "")
    ButtonCompose(text = buttonText) {
        onClick()
    }
    Spacer(modifier = Modifier.height(0.dp))
}

@Composable
fun UsersItem(text: String, buttonText: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = OnBackgroundColor
    )
    UserButtonCompose(text = buttonText) {
        onClick()
    }
    Spacer(modifier = Modifier.height(0.dp))
}

@Composable
fun UserButtonCompose(text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ButtonsColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = OnBackgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            )
    }
}

@Composable
fun ButtonCompose(text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ButtonsColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = OnBackgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,

            )
    }
}

@Composable
fun LightButtonCompose(text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = OnBackgroundColor,
            contentColor = TextColor
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = TextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
        )
    }
}
