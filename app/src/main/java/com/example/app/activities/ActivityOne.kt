package com.example.app.activities

import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api.interfaces.HeartRateViewModel
import com.example.api.interfaces.StepsViewModel
import com.example.core.scopes.InjectFeature
import com.example.ui.BaseActivity

class ActivityOne : BaseActivity() {

    @InjectFeature
    lateinit var stepsVM: StepsViewModel

    @InjectFeature
    lateinit var heartRateVM: HeartRateViewModel

    override fun onFeaturesInjected() {
        Log.d("ActivityOne", "stepsVM initialized: ${::stepsVM.isInitialized}")
        Log.d("ActivityOne", "heartRateVM initialized: ${::heartRateVM.isInitialized}")

        setupUI()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setContent {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(text = "Steps: ", Modifier.clickable {
                        println("Kratos - Steps - ${stepsVM.steps()}")
                    })
                    Spacer(Modifier.height(20.dp))
                    Text(text = "Heart Rate: ", Modifier.clickable {
                        println("Kratos - BPM - ${heartRateVM.bpm()}")
                    })
                }
            }
        }
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
    Greeting("Android")
}