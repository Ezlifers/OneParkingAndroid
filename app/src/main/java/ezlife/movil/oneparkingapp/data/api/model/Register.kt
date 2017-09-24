package ezlife.movil.oneparkingapp.data.api.model

data class RegisterReq(val tipo: String,
                       val nombre: String,
                       val cedula: String,
                       val celular: String,
                       val email: String,
                       val usuario: String,
                       val password: String,
                       val discapasitado: Boolean)

data class RegisterRes(val success: Boolean,
                       val token: String,
                       val id: String,
                       val exist: Boolean)