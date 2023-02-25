package com.android.covidvaccination

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.android.covidvaccination.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


val ageList = listOf(
    18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
    42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65
)
val stateList = listOf(
    "Andhra Pradesh",
    "Arunachal Pradesh",
    "Assam",
    "Bihar",
    "Chhattisgarh",
    "Goa",
    "Gujarat",
    "Haryana",
    "Himachal Pradesh",
    "Jammu and Kashmir",
    "Jharkhand",
    "Karnataka",
    "Kerala",
    "Madhya Pradesh",
    "Maharashtra",
    "Manipur",
    "Meghalaya",
    "Mizoram",
    "Nagaland",
    "Odisha",
    "Punjab",
    "Rajasthan",
    "Sikkim",
    "Tamil Nadu",
    "Telangana",
    "Tripura",
    "Uttarakhand",
    "Uttar Pradesh",
    "West Bengal",
    "Andaman and Nicobar Islands",
    "Chandigarh",
    "Dadra and Nagar Haveli",
    "Daman and Diu",
    "Delhi",
    "Lakshadweep",
    "Puducherry"
)
val monthList = listOf(
    "NONE",
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
)
val vaccineList = listOf(
    "NOT VACCINATED",
    "COVOVAX",
    "Corbevax",
    "ZyCoV-D",
    "GEMCOVAC-19",
    "Spikevax",
    "INCOVACC",
    "Sputnik Light",
    "Sputnik V",
    "Jcovden",
    "Vaxzevria",
    "Covishield",
    "Covaxin",
)
val dosesList = listOf(
    0, 1, 2, 3, 4
)

var userPhoneNumber = ""
var otpText = ""
var userName = ""
var userState = ""
var userAge = ""
var userVaccine = ""
var userDoses = ""
var userDoseDate = ""
var userNumber = ""
var adminSlots = ""


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.ANIMATION_CHANGED)
        setContent {
            CovidVaccinationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainViewModel()  //call the view-model to collect user data if it exists
                    Navigation()  //call Navigation so we navigate accordingly
                }
            }
        }
    }
}

@Composable
fun SplashScreen( navController: NavController) {
    MainViewModel()
    auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var isVisible by remember {
        mutableStateOf(false)
    }
    if (currentUser?.uid != null) {
        LaunchedEffect(key1 = true) {
            delay(100L)
            isVisible = true
            delay(2000L)
            if (userName != "") {
                navController.navigate("profile_screen") {
                    popUpTo(0)
                }
            }else {
                if (userName != "") {
                    navController.navigate("profile_screen") {
                        popUpTo(0)
                    }
                }else {
                    navController.navigate("login_screen") {
                        popUpTo(0)
                    }
                }
            }
        }
    }else {
        LaunchedEffect(key1 = true) {
            delay(100L)
            isVisible = true
            delay(2000L)
            navController.navigate("login_screen") {
                popUpTo(0)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
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
                exit = fadeOut() + shrinkVertically { 0 } + shrinkHorizontally { 0 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "Blood Donation Logo",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Box(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = "Vaccination App",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().uid

    var isSlotClicked by remember {
        mutableStateOf(false)
    }

    var isCerClicked by remember {
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
                        text = "Available Slots",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = OnBackgroundColor

                    )
                    Spacer(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 6.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OnBackgroundColor)
                    )
                    Item("Location", userState) {}
                    Item("Number of available slots", adminSlots) {}
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
                                  // make appointment here
                                  //show toast
                                  bookSlot("true", context)
                            isSlotClicked = !isSlotClicked
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = OnBackgroundColor,
                            contentColor = TextColor,

                            )
                    ) {
                        Text("Book Slot", fontWeight = FontWeight.Normal)
                    }
                }
            },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #1
    if (isCerClicked) {
        AlertDialog(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = BackgroundColor,
            onDismissRequest = {
                isCerClicked = !isCerClicked
            },
            title = null,
            text = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                ) {
                    Text(
                        text = "Vaccination certificate",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 4.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OnBackgroundColor)
                    )
                    Item("Full Name", userName) {}
                    Item("Age", userAge) {}
                    Item("State/City", userState) {}
                    Item("Vaccine Name", userVaccine) {}
                    Item("Latest Dose Date", userDoseDate) {}
                }
            },
            buttons = {
                val view = LocalView.current
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
                            val bitmap = getScreenShotFromView(view)
                            if (bitmap != null) {
                                saveMediaToStorage(bitmap, context)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = OnBackgroundColor,
                            contentColor = TextColor,

                            )
                    ) {
                        Text("Download", fontWeight = FontWeight.Normal)
                    }
                }
            },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #2
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
                    Item(text = "Website", buttonText = "www.progressiveorigin.com") {
                        uriHandler.openUri("https://www.progressiveorigin.com/")
                    }
                    Item(text = "Email", buttonText = "supporteam@progorgn.com") {}
                }
            },
            buttons = {  },
            modifier = Modifier
                .padding(10.dp)
        )
    } // Alert Dialog #3
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
    } // Alert Dialog #4

    val vaccinated = if (userVaccine == "NOT VACCINATED") "not vaccinated" else "vaccinated"
    val vaccineTextColor = if (userVaccine == "NOT VACCINATED") RedTextColor else GreenTextColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
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
                        painter = painterResource(id = R.drawable.user),
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
                    text = userName,
                    fontSize = 32.sp,
                    color = OnBackgroundColor,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = vaccinated,
                    fontSize = 22.sp,
                    color = vaccineTextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (userVaccine != "NOT VACCINATED") {
                        Text(
                            text = userVaccine,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .border(
                                    Dp.Hairline,
                                    OnBackgroundColor.copy(0.4f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(6.dp)
                                .padding(bottom = 2.dp),
                            fontWeight = FontWeight.Thin,
                            textAlign = TextAlign.Center

                        )
                    }
                    Text(
                        text = "$userDoses dose",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .border(
                                Dp.Hairline,
                                OnBackgroundColor.copy(0.4f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(6.dp)
                            .padding(bottom = 2.dp),
                        fontWeight = FontWeight.Thin,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = userState,
                        modifier = Modifier
                            .padding(0.dp)
                            .border(
                                Dp.Hairline,
                                OnBackgroundColor.copy(0.4f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(6.dp)
                            .padding(bottom = 2.dp),
                        fontWeight = FontWeight.Thin,
                        textAlign = TextAlign.Center

                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp, top = 32.dp, bottom = 0.dp)
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
            Column {
                LightButtonCompose("Check available slots") {
                    isSlotClicked = !isSlotClicked
                }
                LightButtonCompose("Vaccination certificates") {
                    isCerClicked = !isCerClicked
                }
                LightButtonCompose("Contact Support") {
                    isAdminClicked = !isAdminClicked
                }
                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp, end = 10.dp, start = 10.dp, top = 10.dp)
                        .fillMaxWidth()
                        .clickable {
                            isDeleteClicked = !isDeleteClicked
                        },
                    contentAlignment = Alignment.Center
                ) {
                    LightText(text = "Delete Account")
                }
            }
        } // Bottom Box

    } // Profile Screen
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var isOtpSent by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    var buttonText by remember {
        mutableStateOf("send OTP")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Covid\nVaccination\nApp",
                fontSize = 36.sp,
                color = OnBackgroundColor,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .weight(2f)
                .background(
                    OnBackgroundColor,
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                ),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Login",
                    fontSize = 32.sp,
                    color = TextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    NumberTextFieldCompose("Phone Number", true)
                    AnimatedVisibility(
                        visible = isOtpSent,
                        exit = fadeOut() + shrinkVertically { 0 },
                        enter = fadeIn() + expandVertically { 1 }
                    ) {
                        NumberTextFieldCompose("OTP", false)
                    }
                    ButtonCompose(buttonText) {
                        if (isOtpSent) {
                            Log.d(MY_LOG, otpText)
//                            navController.navigate("info_screen")
                            if (storedVerificationId.isNotBlank()) {
                                verifyPhoneNumberWithCode(context, storedVerificationId, otpText, navController)
                            }else {
                                d(MY_LOG, "blank")
                            }
                        }else {
                            onLoginClicked(context, userPhoneNumber, navController)
                            buttonText = "next"
                            isOtpSent = !isOtpSent
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun InformationScreen(navController: NavController) {
    val context = LocalContext.current

    var isOtpSent by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Covid\nVaccination\nApp",
                fontSize = 36.sp,
                color = OnBackgroundColor,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .weight(2f)
                .background(
                    OnBackgroundColor,
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                ),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Profile",
                    fontSize = 32.sp,
                    color = TextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {
                        AnimatedVisibility(
                            visible = !isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 }
                        ) {
                            TextFieldCompose("Full Name", false, "name")
                        }
                        AnimatedVisibility(
                            visible = !isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 }
                        ) {
                            ListViewCompose(
                                "State/City",
                                stateList,
                                ageList,
                                true,
                                "state"
                            )
                        }
                        AnimatedVisibility(
                            visible = !isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 }
                        ) {
                            ListViewCompose(
                                "Age",
                                stateList,
                                ageList,
                                false,
                                "age"
                            )
                        }
                    } // First Info Column
                    Column {
                        AnimatedVisibility(
                            visible = isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 },
                            enter = fadeIn() + expandVertically { 1 }
                        ) {
                            ListViewCompose(
                                "Vaccine Name",
                                vaccineList,
                                ageList,
                                true,
                                "vaccine"
                            )
                        }
                        AnimatedVisibility(
                            visible = isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 },
                            enter = fadeIn() + expandVertically { 1 }
                        ) {
                            ListViewCompose(
                                "How Many Doses",
                                stateList,
                                dosesList,
                                false,
                                "doses"
                            )
                        }
                        AnimatedVisibility(
                            visible = isOtpSent,
                            exit = fadeOut() + shrinkVertically { 0 },
                            enter = fadeIn() + expandVertically { 1 }
                        ) {
                            ListViewCompose(
                                "Latest vaccine shot",
                                monthList,
                                ageList,
                                true,
                                "date"
                            )
                        }
                    } // Second Info Column
                    ButtonCompose("Next") {
                        if (isOtpSent) {
                            if (userName.isNotBlank() || userVaccine.isNotBlank() || userAge.isNotBlank() || userState.isNotBlank() || userDoseDate.isNotBlank() || userDoses.isNotBlank()) {
                                uploadData(context)
                                navController.navigate("profile_screen")
                                Log.d(MY_LOG, "$userName, $userAge, $userAge, $userVaccine, $userDoses, $userDoseDate")
                            }else {
                                Toast.makeText(context, "please enter correct data!", Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            isOtpSent = !isOtpSent
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Item(text: String, buttonText: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = OnBackgroundColor
    )
    ButtonCompose(text = buttonText) {
        onClick()
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun NumberTextFieldCompose(label: String, isPhone: Boolean) {
    var phoneNumber by remember {
        mutableStateOf("")
    }
    TextField(
        value = phoneNumber,
        onValueChange = {
            if (it.length <= 40) {
                phoneNumber = it
                if (isPhone) {
                    userPhoneNumber = phoneNumber
                }else {
                 otpText = phoneNumber
                }
            }
        },
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = White,
            backgroundColor = FieldsColor,
            cursorColor = Color.Black,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            unfocusedLabelColor = OnBackgroundColor,
            focusedLabelColor = White,
            disabledLabelColor = OnBackgroundColor,
        ),
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
        ),
        maxLines = 1,

    )
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
            contentColor = White
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

@Composable
fun TextFieldCompose(label: String, isPassword: Boolean, type: String) {
    var text by remember {
        mutableStateOf("")
    }
    when (type) {
        "name" -> {
            userName = text
        }
    }
    TextField(
        value = text,
        onValueChange = {
            if (it.length <= 32) {
                text = it
            }
        },
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = White,
            backgroundColor = FieldsColor,
            cursorColor = OnBackgroundColor,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            unfocusedLabelColor = OnBackgroundColor,
            focusedLabelColor = White,
            disabledLabelColor = OnBackgroundColor,
        ),
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        maxLines = 1,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,

        )
}

@Composable
fun ListViewCompose(label: String, stringList: List<String>, numbersList: List<Int>, isString: Boolean, type: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val list = if (isString) stringList else numbersList
    var selectedText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (isExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        isExpanded = !isExpanded
    }

    when (type) {
        "state" -> {
            userState = selectedText
        }
        "age" -> {
            userAge = selectedText
        }
        "vaccine" -> {
            userVaccine = selectedText
        }
        "doses" -> {
            userDoses = selectedText
        }
        "date" -> {
            userDoseDate = selectedText
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(
                textColor = White,
                backgroundColor = FieldsColor,
                cursorColor = OnBackgroundColor,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                unfocusedLabelColor = OnBackgroundColor,
                focusedLabelColor = Transparent,
                disabledLabelColor = Transparent,
            ),
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .clickable {
                    isExpanded = !isExpanded
                },
            label = { Text(label, fontSize = 14.sp) },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { isExpanded = !isExpanded })
            },
            enabled = true,
            shape = RoundedCornerShape(20.dp),
            interactionSource = interactionSource
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .background(OnBackgroundColor, RoundedCornerShape(20.dp))
        ) {
            list.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = label.toString()
                        isExpanded = false
                    }
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor, RoundedCornerShape(10.dp))) {
                        Text(
                            text = label.toString(),
                            fontSize = 16.sp,
                            color = OnBackgroundColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LightText(text: String) {
    Text(
        text = text,
        color = RedTextColor,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .background(OnBackgroundColor, RoundedCornerShape(20.dp))
            .padding(12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CovidVaccinationTheme {

    }
}
