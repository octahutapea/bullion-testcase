package com.bullion.bulliontestcase.data.remote.retrofit

import android.net.Uri
import com.bullion.bulliontestcase.data.remote.request.EditRequestBody
import com.bullion.bulliontestcase.data.remote.request.LoginRequestBody
import com.bullion.bulliontestcase.data.remote.response.EditResponse
import com.bullion.bulliontestcase.data.remote.response.LoginResponse
import com.bullion.bulliontestcase.data.remote.response.RegisterResponse
import com.bullion.bulliontestcase.data.remote.response.UsersResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.Date

interface ApiService {
    @POST("api/v1/auth/login")
    fun postLogin(
        @Body loginRequestBody: LoginRequestBody
    ): Call<LoginResponse>

    @Multipart
    @POST("api/v1/auth/register")
    fun postRegister(
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("date_of_birth") dateOfBirth: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part("password") password: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<RegisterResponse>

    @GET("api/v1/admin/")
    fun getUsers(
        @Header("Authorization") auth: String
    ): Call<UsersResponse>

    @PUT("api/v1/admin/{id}/update")
    fun postEdit(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Body editRequestBody: EditRequestBody
    ): Call<EditResponse>
}