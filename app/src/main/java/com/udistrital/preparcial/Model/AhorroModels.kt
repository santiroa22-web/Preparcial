package com.udistrital.preparcial.Model

import androidx.compose.runtime.mutableStateListOf

data class Aporte(
    val id: String,
    val integrante: String,
    val monto: Double,
    val fecha: String
)

data class PlanAhorro(
    val id: String,
    val nombre: String,
    val meta: Double,
    val fechaLimite: String,
    val aportes: MutableList<Aporte> = mutableStateListOf()
) {
    val totalAhorrado: Double
        get() = aportes.sumOf { it.monto }
}