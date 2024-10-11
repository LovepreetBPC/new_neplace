package com.neplace.customer.login.repository

import com.neplace.customer.model.AddCardModel
import com.neplace.customer.model.AddRidesModel
import com.neplace.customer.model.AirPortCodeModel
import com.neplace.customer.model.AirlineModel
import com.neplace.customer.model.ChooseVehicleModel
import com.neplace.customer.model.DeleteCardModel
import com.neplace.customer.model.DeleteUserAccountModel
import com.neplace.customer.model.EtaNumberModel
import com.neplace.customer.model.FlightNumberModel
import com.neplace.customer.model.GetCards
import com.neplace.customer.model.GetGoogleKeyModel
import com.neplace.customer.model.GetNearByDriverModel
import com.neplace.customer.model.GetPlanModel
import com.neplace.customer.model.GetRideModel
import com.neplace.customer.model.GetVehicleModel
import com.neplace.customer.model.LoginModel
import com.neplace.customer.model.PlanDetailModel
import com.neplace.customer.model.PlanSubscripationModel
import com.neplace.customer.model.ProfileModel
import com.neplace.customer.model.RideCancelChargesModel
import com.neplace.customer.model.RideHistoryModel
import com.neplace.customer.model.RideStatusUpdateModel
import com.neplace.customer.model.SendRatingModel
import com.neplace.customer.model.SocialLoginModel
import com.neplace.customer.model.StripePaymentModel
import com.neplace.customer.model.UserNotificationModel
import com.neplace.customer.model.VerifyOtpModel
import com.nexter.application.retrofit.RetrofitUtils
import com.neplace.customer.retrofit.UserNetwork
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserRepository {


    suspend fun loginUser(email: String): Response<LoginModel> {
        return UserNetwork.retrofit.loginUser(email)
    }

    suspend fun verifyOtp(
        mobile: String,
        otp: String,
        device_token: String
    ): Response<VerifyOtpModel> {
        return UserNetwork.retrofit.verifyOtp(
            RetrofitUtils.stringToRequestBody(mobile),
            RetrofitUtils.stringToRequestBody(otp),
            RetrofitUtils.stringToRequestBody(device_token),

            )
    }

    suspend fun registerUser(
        first_name: String, last_name: String, email: String, phone_number:String,
        role_id: String, city: String, address: String,
        password: String, password_confirmation: String,
        image: MultipartBody.Part,
        device_token: String,
    ): Response<VerifyOtpModel> {
        return UserNetwork.retrofit.registerUser(
            RetrofitUtils.stringToRequestBody(first_name),
            RetrofitUtils.stringToRequestBody(last_name),
            RetrofitUtils.stringToRequestBody(email),
            RetrofitUtils.stringToRequestBody(phone_number),
            RetrofitUtils.stringToRequestBody(role_id),
            RetrofitUtils.stringToRequestBody(city),
            RetrofitUtils.stringToRequestBody(address),
            RetrofitUtils.stringToRequestBody(password),
            RetrofitUtils.stringToRequestBody(password_confirmation),
            image,
//            RetrofitUtils.stringToRequestBody(image),
            RetrofitUtils.stringToRequestBody(device_token)
        )
    }

    suspend fun socialLogin(
        email: String,
        device_token: String,
        device_type: String,
        role_id: String,
        google_id: String,
    ): Response<SocialLoginModel> {
        return UserNetwork.retrofit.socialLogin(
            RetrofitUtils.stringToRequestBody(email),
            RetrofitUtils.stringToRequestBody(device_token),
            RetrofitUtils.stringToRequestBody(device_type),
            RetrofitUtils.stringToRequestBody(role_id),
            RetrofitUtils.stringToRequestBody(google_id),
        )
    }


    suspend fun registerCustomerCard(
        front_image: MultipartBody.Part,
        back_image: MultipartBody.Part
    ): Response<VerifyOtpModel> {
        return UserNetwork.retrofit.registerCustomerCard(front_image, back_image)

    }

    suspend fun addRides(body: MultipartBody): Response<AddRidesModel> {
        return UserNetwork.retrofit.addRides(body)

    }


    suspend fun profileEdit(first_name: String, last_name: String, image: MultipartBody.Part, ): Response<ProfileModel> {
        return UserNetwork.retrofit.profileEdit(
            RetrofitUtils.stringToRequestBody(first_name),
            RetrofitUtils.stringToRequestBody(last_name),
            image
//            RetrofitUtils.stringToRequestBody(image),
        )
    }

    suspend fun getProfile(): Response<ProfileModel> {
        return UserNetwork.retrofit.getProfile()
    }

    suspend fun getVehicleType(): Response<GetVehicleModel> {
        return UserNetwork.retrofit.getVehicleType()
    }

    suspend fun chooseVehicle(vehicle_id: String): Response<ChooseVehicleModel> {
        return UserNetwork.retrofit.chooseVehicle(vehicle_id)
    }

    suspend fun getPlans(): Response<GetPlanModel> {
        return UserNetwork.retrofit.getPlans()
    }

    suspend fun plansDetail(id: String): Response<PlanDetailModel> {
        return UserNetwork.retrofit.plansDetail(id)
    }
    suspend fun planSubscription(plan_id: String,card_id: String): Response<PlanSubscripationModel> {
        return UserNetwork.retrofit.planSubscription(
            RetrofitUtils.stringToRequestBody(plan_id),
            RetrofitUtils.stringToRequestBody(card_id),
            )
    }

    suspend fun userRidesHistory(): Response<RideHistoryModel> {
        return UserNetwork.retrofit.userRidesHistory()
    }

    suspend fun userRidesUpComming(): Response<RideHistoryModel> {
        return UserNetwork.retrofit.userRidesUpComming()
    }

    suspend fun getRide(status: String): Response<RideHistoryModel> {
        return UserNetwork.retrofit.getRide(status)
    }

    suspend fun getRideCancelCharges(id: String): Response<RideCancelChargesModel> {
        return UserNetwork.retrofit.getRideCancelCharges(id)
    }

    suspend fun rideStatusUpdate(status: String, user_id: String, is_reject_driver : Boolean): Response<RideStatusUpdateModel> {
        return UserNetwork.retrofit.rideStatusUpdate(
            RetrofitUtils.stringToRequestBody(status),
            RetrofitUtils.stringToRequestBody(user_id),
            is_reject_driver ,
        )
    }

    suspend fun sendRating(body: MultipartBody): Response<SendRatingModel> {
        return UserNetwork.retrofit.sendRating(body)
    }

    suspend fun getRides(id: String): Response<GetRideModel> {
        return UserNetwork.retrofit.getRides(id)
    }

    suspend fun airPortCode(): Response<AirPortCodeModel> {
        return UserNetwork.retrofit.airPortCode()
    }

    suspend fun getAirline(): Response<AirlineModel> {
        return UserNetwork.retrofit.getAirline()
    }

    suspend fun getFlightNumber(key: String): Response<FlightNumberModel> {
        return UserNetwork.retrofit.getFlightNumber(key)
    }

    suspend fun getEtaNumber(key: String): Response<EtaNumberModel> {
        return UserNetwork.retrofit.getEtaNumber(key)
    }

    suspend fun getNearByDriver(
        lat: String,
        long: String,
        vehicle_type: String
    ): Response<GetNearByDriverModel> {
        return UserNetwork.retrofit.getNearByDriver(lat, long, vehicle_type)
    }

    suspend fun cancelRides(id: String): Response<GetRideModel> {
        return UserNetwork.retrofit.cancelRides(id)
    }

    suspend fun getNotification(): Response<UserNotificationModel> {
        return UserNetwork.retrofit.getNotification()
    }

    suspend fun deleteSingleNotification(id: String): Response<UserNotificationModel> {
        return UserNetwork.retrofit.deleteSingleNotification(id)
    }

    suspend fun deleteNotification(): Response<UserNotificationModel> {
        return UserNetwork.retrofit.deleteNotification()
    }

    suspend fun addCardRepo(hashMap: HashMap<String, Any>): Response<AddCardModel> {
        return UserNetwork.retrofit.addCardInterface(hashMap)
    }

    suspend fun stripePayment(hashMap: MultipartBody): Response<StripePaymentModel> {
        return UserNetwork.retrofit.stripePayment(hashMap)
    }

    suspend fun getCardsRepo():Response<GetCards>{
        return UserNetwork.retrofit.getCardslist()
    }

    suspend fun deleteSingleCard(card_id: String): Response<DeleteCardModel> {
        return UserNetwork.retrofit.deleteSinglecard(card_id)
    }

    suspend fun rideTimeUpdate(hashMap: MultipartBody): Response<AddRidesModel> {
        return UserNetwork.retrofit.rideTimeUpdate(hashMap)
    }
    suspend fun deleteAccount(
        user_id: String,
    ): Response<DeleteUserAccountModel> {
        return UserNetwork.retrofit.deleteAccount(
            RetrofitUtils.stringToRequestBody(user_id),
        )
    }

    suspend fun getGoogleKey():Response<GetGoogleKeyModel>{
        return UserNetwork.retrofit.getGoogleKey()
    }

}