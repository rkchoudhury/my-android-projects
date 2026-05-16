package com.example.dagger2car

import android.util.Log

class Car(val wheel: Wheel, val engine: Engine) {

    fun drive() {
        Log.d("DRAGGER", "Car is driving")
    }
}