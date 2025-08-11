package org.envobyte.weatherforecast.presentation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.envobyte.weatherforecast.presentation.theme.PrimaryGradientButton
import org.envobyte.weatherforecast.presentation.theme.rubikFontFamily

@Composable

fun PrimaryButton(
    buttonText: String = "Get Started",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 28.dp)
            .background(PrimaryGradientButton, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Transparent
        ),
        onClick = onClick,
    ) {
        Text(
            buttonText, style = TextStyle(
                fontFamily = rubikFontFamily(),
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
            )
        )
    }
}
