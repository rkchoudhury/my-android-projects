package com.example.dagger2car

import android.util.Log
import javax.inject.Inject

class Car @Inject constructor(val wheel: Wheel, val engine: Engine) {

    fun drive() {
        Log.d("DaggerCar", "I am driving")
    }
}