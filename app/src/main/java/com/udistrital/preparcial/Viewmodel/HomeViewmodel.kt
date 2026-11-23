package com.udistrital.preparcial.Viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.udistrital.preparcial.Model.Aporte
import com.udistrital.preparcial.Model.PlanAhorro

class HomeViewmodel : ViewModel() {

    val planes = mutableStateListOf(
        PlanAhorro(
            id = "1",
            nombre = "Ahorro para viaje familiar",
            meta = 5000000.0,
            fechaLimite = "15/12/2026",
            aportes = mutableStateListOf(
                Aporte("a1", "Thiago", 1500000.0, "01/11/2026"),
                Aporte("a2", "Majo", 1000000.0, "15/11/2026")
            )
        )
    )

    fun agregarPlan(plan: PlanAhorro) {
        planes.add(plan)
    }

    fun agregarAporte(planId: String, aporte: Aporte) {
        planes.find { it.id == planId }?.aportes?.add(aporte)
    }
}