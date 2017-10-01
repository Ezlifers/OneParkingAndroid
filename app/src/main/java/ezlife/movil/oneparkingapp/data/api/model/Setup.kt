package ezlife.movil.oneparkingapp.data.api.model

import ezlife.movil.oneparkingapp.data.db.model.Config

data class Setup(val success: Boolean, val version: Int = 0, val config: Config?, val zones: List<ZoneBase>?)

data class Time(val d: Boolean, val ti: Int, val tf: Int)
data class TimeRange(val horarios: List<Time>)
data class Point(val coordinates: List<Double>)
data class ZoneBase(val _id: String
                    , val codigo: Int
                    , val nombre: String
                    , val direccion: String
                    , val localizacion: Point
                    , val tiempos: List<TimeRange>)