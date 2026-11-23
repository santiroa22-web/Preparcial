package com.udistrital.preparcial.Viewmodel

import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.PlanAhorro

class AhorroViewModel : ViewModel() {
    fun calcularProgreso(plan: PlanAhorro): Float {
        if (plan.meta <= 0) return 0f
        return (plan.totalAhorrado / plan.meta).toFloat().coerceIn(0f, 1f)
    }
}