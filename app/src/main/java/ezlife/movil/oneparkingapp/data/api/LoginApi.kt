package ezlife.movil.oneparkingapp.data.api

import ezlife.movil.oneparkingapp.data.api.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("usuarios/login")
    fun login(@Body req: LoginReq): Observable<LoginRes>

    @POST("usuarios/signin")
    fun register(@Body req: RegisterReq): Observable<RegisterRes>

    @POST("usuarios/pass")
    fun updatePassword(@Header("Authorization") auth: String, @Body req: UpdatePassReq): Observable<SimpleResponse>
}