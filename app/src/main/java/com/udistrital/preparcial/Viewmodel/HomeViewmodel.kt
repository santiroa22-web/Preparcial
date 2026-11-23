package com.udistrital.preparcial.Viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.Aporte
import com.udistrital.preparcial.Model.PlanAhorro

class HomeViewmodel : ViewModel() {
    val planes = mutableStateListOf<PlanAhorro>()

    fun agregarPlan(plan: PlanAhorro) {
        planes.add(plan)
    }

    fun agregarAporte(planId: String, aporte: Aporte) {
        planes.find { it.id == planId }?.aportes?.add(aporte)
    }
}