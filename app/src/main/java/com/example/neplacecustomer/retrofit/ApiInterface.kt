package com.example.neplacecustomer.retrofit


import com.example.neplacecustomer.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {


    @GET("api/get_otp/{get_otp}")
    suspend fun loginUser(@Path("get_otp") get_otp: String?): Response<LoginModel>

    @Multipart
    @POST("api/verify_otp")
    suspend fun verifyOtp(
        @Part("mobile") mobile: RequestBody?,
        @Part("otp") otp: RequestBody?,
        @Part("device_token") device_token: RequestBody?,
    ): Response<VerifyOtpModel>

    @Multipart
    @POST("api/register")
    suspend fun registerUser(
        @Part("first_name") first_name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone_number: RequestBody?,
        @Part("role_id") role_id: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("password_confirmation") password_confirmation: RequestBody?,
//        @Part("image") image: MultipartBody.Part,
        @Part file: MultipartBody.Part,

        @Part("device_token") device_token: RequestBody?,
    ): Response<VerifyOtpModel>

    @Multipart
    @POST("api/social-login")
    suspend fun socialLogin(
        @Part("email") email: RequestBody?,
        @Part("device_token") role_id: RequestBody?,
        @Part("device_type") city: RequestBody?,
        @Part("role_id") password: RequestBody?,
        @Part("google_id") google_id: RequestBody?,
    ): Response<SocialLoginModel>

    @POST("api/rides")
    suspend fun addRides(@Body request: MultipartBody): Response<AddRidesModel>

    @Multipart
    @POST("api/register_customer_card")
    suspend fun registerCustomerCard(
        @Part file: MultipartBody.Part,
        @Part file_back: MultipartBody.Part,
//        @Part("personal_id_card_front_image") front_image: RequestBody?,
//        @Part("personal_id_card_back_image") back_image: RequestBody?
    ): Response<VerifyOtpModel>

    @Multipart
    @POST("api/profile_edit")
    suspend fun profileEdit(
        @Part("first_name") first_name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
//        @Part("image") image: MultipartBody.Part ?,
        @Part file: MultipartBody.Part,
    ): Response<ProfileModel>

    @GET("api/user/profile")
    suspend fun getProfile(): Response<ProfileModel>


    @GET("api/choosen/vehicle/?{vehicle_id}")
    suspend fun chooseVehicle(
        @Query("vehicle_id") vehicle_id: String?,
    ): Response<ChooseVehicleModel>


    @GET("api/vehicletype")
    suspend fun getVehicleType(): Response<GetVehicleModel>

    @GET("api/plans")
    suspend fun getPlans(): Response<GetPlanModel>

    @GET("api/plans/{id}")
    suspend fun plansDetail(@Path("id") id: String?): Response<PlanDetailModel>
    @Multipart
    @POST("api/plan-subscription")
    suspend fun planSubscription(
        @Part("plan_id") plan_id: RequestBody?,
        @Part("card_id") card_id: RequestBody?,
    ): Response<PlanSubscripationModel>


    @GET("api/rides/{id}")
    suspend fun getRides(@Path("id") id: String?): Response<GetRideModel>

    @GET("api/userRides/history")
    suspend fun userRidesHistory(): Response<RideHistoryModel>

    @GET("api/userRides/upcoming")
    suspend fun userRidesUpComming(): Response<RideHistoryModel>

    @GET("api/airportcode")
    suspend fun airPortCode(): Response<AirPortCodeModel>

    @GET("api/airportapi/airline")
    suspend fun getAirline(): Response<AirlineModel>

    @GET("api/airportapi/flight/{key}")
    suspend fun getFlightNumber(@Path("key") key: String?): Response<FlightNumberModel>

    @GET("api/airportapi/eta/{key}")
    suspend fun getEtaNumber(@Path("key") key: String?): Response<EtaNumberModel>

    @GET("api/getnearbydrivers")
    suspend fun getNearByDriver(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("vehicle_type") vehicleType: String
    ): Response<GetNearByDriverModel>

    @PUT("api/rides/{id}")
    suspend fun cancelRides(@Path("id") id: String?): Response<GetRideModel>


    @GET("api/rides")
    suspend fun getRide(@Query("status") status: String): Response<RideHistoryModel>

    @GET("api/ridecancelcharges/{id}")
    suspend fun getRideCancelCharges(@Path("id") id: String?): Response<RideCancelChargesModel>


    @Multipart
    @POST("api/driver/rideStatusUpdate")
    suspend fun rideStatusUpdate(
        @Part("status") status: RequestBody?,
        @Part("ride_id") ride_id: RequestBody?,
        @Part("is_reject_driver") is_reject_driver: Boolean,
    ): Response<RideStatusUpdateModel>

    @POST("api/ratings")
    suspend fun sendRating(@Body request: MultipartBody): Response<SendRatingModel>


    @GET("api/user-notifications")
    suspend fun getNotification(): Response<UserNotificationModel>
    @DELETE("api/user-notifications/{id}")
    suspend fun deleteSingleNotification(@Path("id") id: String?): Response<UserNotificationModel>

    @DELETE("api/user-notifications")
    suspend fun deleteNotification(): Response<UserNotificationModel>

    @POST("tokens")
    fun createCardInterface(
        @Query("card[number]") cardNumber: String,
        @Query("card[exp_month]") expMonth: String,
        @Query("card[exp_year]") expYear: String,
        @Query("card[cvc]") cvc: String
    ): Call<Craete_CardToken>



    @POST("api/paymentstripe")
    suspend fun stripePayment(@Body request: MultipartBody): Response<StripePaymentModel>


    @POST("api/store-card")
    suspend fun addCardInterface(@Body hashMap: HashMap<String, Any>):Response<AddCardModel>



    @Multipart
    @POST("api/save_stripe_id")
    suspend fun addStripeId(

        @Part("stripe_id") stripeId: RequestBody?,


        ): Response<AddStripeModel>

//    @Multipart
//    @POST("api/save_stripe_id")
//    suspend fun addStripeId(
//
//        @Part("stripe_id") stripeId: RequestBody?,
//
//
//        ): Response<AddStripeModel>


    @GET("api/myCards")
    suspend fun getCardslist():Response<GetCards>

    @GET("api/themesetting/google_key")
    suspend fun getGoogleKey():Response<GetGoogleKeyModel>

    @POST("api/deleteCard/{card_id}")
    suspend fun deleteSinglecard(@Path("card_id") card_id: String?): Response<DeleteCardModel>


    @POST("api/rideTimeUpdate")
    suspend fun rideTimeUpdate(@Body request: MultipartBody): Response<AddRidesModel>

    @GET("directions/v5/mapbox/driving/{origin};{destination}")
    fun getDirections(
        @Query("alternatives") alternatives: Boolean = true,
        @Query("geometries") geometries: String = "geojson",
        @Query("language") language: String = "en",
        @Query("overview") overview: String = "full",
        @Query("steps") steps: Boolean = true,
        @Query("access_token") accessToken: String,
        @Path("origin") origin: String,
        @Path("destination") destination: String
    ): Call<DirectionsResponse>

    @Multipart
    @POST("api/delete-my-account")
    suspend fun deleteAccount(
        @Part("user_id") user_id:  RequestBody?,
    ): Response<DeleteUserAccountModel>


//    @GET("directions/v5/mapbox/driving/{origin};{destination}")
//    fun getDirections(
//        @Query("alternatives") alternatives: Boolean = true,
//        @Query("geometries") geometries: String = "geojson",
//        @Query("language") language: String = "en",
//        @Query("overview") overview: String = "full",
//        @Query("steps") steps: Boolean = true,
//        @Query("access_token") accessToken: String,
//        @Path("origin") origin: String,
//        @Path("destination") destination: String
//    ): Call<DirectionsResponse>





}


