package com.example.activity_two.lifecyclecomponent

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.activity_two.ui.theme.DaggerMultiModuleTheme
import com.example.api.interfaces.HeartRateViewModel
import com.example.api.interfaces.StepsViewModel
import com.example.core.lifecycle.BaseActivity
import com.example.core.scopes.FeatureScope
import com.example.core.scopes.InjectFeature

class ActivityTwo : BaseActivity() {

    @InjectFeature(FeatureScope.APP)
    lateinit var stepsVM: StepsViewModel

    @InjectFeature(FeatureScope.ACTIVITY)
    lateinit var heartRateVM: HeartRateViewModel

    override fun onFeaturesInjected() {
        super.onFeaturesInjected()
        Log.d("ActivityTwo", "Kratos - StepsVM - Id : ${stepsVM.instanceId}")
        Log.d("ActivityTwo", "Kratos - HeartRateVM - Id : ${heartRateVM.instanceId}")
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