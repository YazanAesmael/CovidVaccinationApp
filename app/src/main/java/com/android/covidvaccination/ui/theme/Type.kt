package com.android.covidvaccination.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.covidvaccination.R

val quicksandNormal = FontFamily(Font(R.font.quicksand_normal))
val quicksandBold = FontFamily(Font(R.font.quicksand_bold))
val quicksandLight = FontFamily(Font(R.font.quicksand_light))
val quicksandMedium = FontFamily(Font(R.font.quicksand_medium))
val quicksandSemiBold = FontFamily(Font(R.font.quicksand_semi_bold))

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = quicksandNormal,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)