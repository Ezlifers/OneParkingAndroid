package ezlife.movil.oneparkingapp.data.api.model

data class UserIncident(val nombre: String, val celular: String)
data class ZoneIncident(val id: String, val codigo: Int, val nombre: String)
data class Incident(val foto: String, val observaciones: String, val zona: ZoneIncident, val usuario: UserIncident)

data class IncidentRes(val success: Boolean, val failImage: Boolean)