package ezlife.movil.oneparkingapp.data.api.model

import ezlife.movil.oneparkingapp.data.db.model.Car

data class User(val _id: String,
                val tipo: String,
                val nombre: String,
                val cedula: String,
                val celular: String,
                val email: String,
                val discapasitado: Boolean,
                val vehiculos: List<Car>,
                val saldo: Long,
                val validado: Boolean = true)