package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.dogbreedsgallery.di.AppDependencies
import com.example.dogbreedsgallery.ui.navigation.AppNavGraph
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(applicationContext)
        setContent {
            DogBreedsGalleryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavGraph(navController = rememberNavController())
                }
            }
        }
    }
}