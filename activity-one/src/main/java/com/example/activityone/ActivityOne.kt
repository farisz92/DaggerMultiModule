package com.example.activityone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.activity_two.lifecyclecomponent.ActivityTwo
import com.example.activityone.ui.theme.DaggerMultiModuleTheme
import com.example.features.heartrate.api.interfaces.HeartRateViewModel
import com.example.api.interfaces.StepsViewModel
import com.example.core.di.FeatureScope
import com.example.core.featureprovision.LazyFeatureDelegate
import com.example.core.featureprovision.lazyFeature
import com.example.core.lifecycle.BaseActivity
import com.example.core.scopes.InjectFeature
import kotlin.getValue

class ActivityOne : BaseActivity() {

    @field:InjectFeature(FeatureScope.APP)
    private lateinit var _stepsVM: LazyFeatureDelegate<StepsViewModel>

    val stepsVM: StepsViewModel
        get() = _stepsVM.get()

    private val heartRateVM by lazyFeature(HeartRateViewModel::class, FeatureScope.ACTIVITY) {
        getFeatureManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // This calls onFeaturesInjected()
        Log.d("ActivityOne", "Leviathan - onCreate - Before onFeaturesInjected")

        // This should not trigger VM creation
        Log.d("ActivityOne", "Leviathan - Activity created, VM should not be created yet")
    }

    override fun onFeaturesInjected() {
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("ActivityOne", "Leviathan - After onFeaturesInjected - Before first access")
            Log.d("ActivityOne", "Leviathan - About to access heartRateVM for the first time")
            val vm = heartRateVM
            Log.d("ActivityOne", "Leviathan - After first access to heartRateVM")

            // Verify the VM was created
            assert(vm != null)

            // Access again to verify caching
            val sameVm = heartRateVM
            assert(vm === sameVm) // Should be the same instance
            setupUI()
            Log.d("Kratos", "StepsVM - Id : ${stepsVM.instanceId}")
            Log.d("Kratos", "HeartRateVM - Id : ${heartRateVM.instanceId}")

        }, 1000) // 1 second delay to ensure injection is complete
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
                        Spacer(Modifier.height(40.dp))
                        ActivityTwoButton(this@ActivityOne)
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
fun ActivityTwoButton(context: Context) {
    Button(onClick = {
        val activityTwoIntent = Intent(context, ActivityTwo::class.java)
        context.startActivity(activityTwoIntent)
    }) {
        Text("Go To -> Activity Two")
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