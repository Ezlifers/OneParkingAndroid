package ezlife.movil.oneparkingapp.data.api.model


data class LoginReq(val role: String,
                    val user: String,
                    val password: String,
                    val timestamp: Long)

data class LoginRes(val success: Boolean,
                    val timeout: Boolean,
                    val token: String,
                    val user: User)