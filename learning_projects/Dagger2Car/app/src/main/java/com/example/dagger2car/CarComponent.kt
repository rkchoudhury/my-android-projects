package com.example.dagger2car

import dagger.Component

@Component
interface CarComponent {
    fun getCar(): Car
}