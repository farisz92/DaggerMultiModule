package com.example.activityone

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api.interfaces.HeartRateViewModel
import com.example.api.interfaces.StepsViewModel
import com.example.core.scopes.InjectFeature
import com.example.activityone.ui.theme.DaggerMultiModuleTheme
import com.example.core.scopes.FeatureScope
import com.example.ui.BaseActivity

class ActivityOne : BaseActivity() {

    @InjectFeature(FeatureScope.APP)
    lateinit var stepsVM: StepsViewModel

    @InjectFeature(FeatureScope.ACTIVITY)
    lateinit var heartRateVM: HeartRateViewModel

    override fun onFeaturesInjected() {
        super.onFeaturesInjected()
        setupUI()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContent {
            DaggerMultiModuleTheme {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column {
                        StepsButton(stepsVM)
                        Spacer(Modifier.height(20.dp))
                        HeartRateButton(heartRateVM)
                    }
                }
            }
        }
    }
}

@Composable
fun StepsButton(stepsViewModel: StepsViewModel) {
    Button(onClick = {
        println("Kratos - Steps - ${stepsViewModel.steps()}")
    }) {
        Text("Step Count")
    }
}

@Composable
fun HeartRateButton(heartRateViewModel: HeartRateViewModel) {
    Button(onClick = {
        println("Kratos - BPM - ${heartRateViewModel.bpm()}")
    }) {
        Text("Heart Rate")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DaggerMultiModuleTheme {
        Greeting("Android")
    }
}