package com.example.moveup_re

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moveup_re.screen.Greetings
import com.example.moveup_re.screen.WorkOutScreen
import com.example.moveup_re.ui.theme.MoveUP_reTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoveUP_reTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier){
    var nowPage by rememberSaveable { mutableStateOf("workOut") }

    Surface(modifier) {
        when (nowPage) {
            "home" -> HomeScreen(onContinueClicked = { nowPage = "false" })
            "workOut" -> WorkOutScreen(
                onImageCaptureClick = {},
                onVideoCaptureClick = {}
            )
            "stepTracker" -> StepTrackerScreen()
            "dailyReport" -> DailyReportScreen()
            "monthlyReport" -> MonthlyReportScreen()
            else -> Greetings()
        }
    }
}

@Composable
fun HomeScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement  = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ){
            Text("Continue")
        }
    }
}


@Composable
fun StepTrackerScreen(){

}

@Composable
fun DailyReportScreen(){

}

@Composable
fun MonthlyReportScreen(){

}