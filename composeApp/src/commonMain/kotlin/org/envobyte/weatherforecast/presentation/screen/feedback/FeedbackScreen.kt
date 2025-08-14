package org.envobyte.weatherforecast.presentation.screen.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.envobyte.weatherforecast.presentation.screen.component.BasicTextInput
import org.envobyte.weatherforecast.presentation.screen.component.PrimaryButton
import org.envobyte.weatherforecast.presentation.screen.component.TopBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import weatherly.composeapp.generated.resources.Res
import weatherly.composeapp.generated.resources.ic_image
import weatherly.composeapp.generated.resources.ic_profile


@Composable
fun FeedbackScreen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(title = "Feedback", onClick = { navController.navigateUp() })
            FeedbackForm(onSubmit = { data ->

            })
        }
    }

}






// Data holder
data class FeedbackData(
    val name: String,
    val subject: String,
    val details: String,
)

@Composable
fun FeedbackForm(
    onSubmit: (FeedbackData) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        BasicTextInput(
            text = name,
            icon = painterResource(Res.drawable.ic_profile),
            placeholder = "Your name",
            onValueChange = { name = it }
        )

        BasicTextInput(
            text = subject,
            icon = painterResource(Res.drawable.ic_image),
            placeholder = "Subject",
            onValueChange = { subject = it }
        )


        BasicTextInput(
            text = details,
            height = 150.dp,
            singleLine = false,
            placeholder = "Tell us what's on your mind…",
            onValueChange = { details = it }
        )


        PrimaryButton(
            onClick = {
                onSubmit(
                    FeedbackData(
                        name = name.trim(),
                        subject = subject.trim(),
                        details = details.trim(),
                    )
                )
            },
            buttonText = "Submit"

        )
    }
}


@Preview
@Composable
fun FeedbackFormPreview() {
   FeedbackScreen(
       navController = rememberNavController()
   )
}