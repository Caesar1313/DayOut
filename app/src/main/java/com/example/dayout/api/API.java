package com.example.dayout.api;

import com.example.dayout.models.SearchPlaceModel;
import com.example.dayout.models.authModels.LoginModel;

import com.example.dayout.models.notification.NotificationModel;
import com.example.dayout.models.poll.PollsPaginationModel;
import com.example.dayout.models.profile.ProfileData;
import com.example.dayout.models.profile.ProfileModel;
import com.example.dayout.models.popualrPlace.PlaceModel;

import com.example.dayout.models.profile.organizer.OrganizersModel;
import com.example.dayout.models.trip.BookTripModel;
import com.example.dayout.models.trip.TripPaginationModel;
import com.example.dayout.models.trip.place.PlaceDetailsModel;
import com.example.dayout.models.trip.roadMap.RoadMapModel;
import com.example.dayout.models.trip.TripDetailsModel;
import com.example.dayout.models.trip.tripType.TripTypeModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {


    /**
     * Get Request
     */

    @GET("api/place/popular/{id}")
    Call<PlaceModel> getPopularPlaces(@Path("id") int id);

    @GET("api/user/profile/customer/{id}")
    Call<ProfileModel> getPassengerProfile(@Path("id") int id);

    @GET("api/trip/types")
    Call<TripTypeModel> getTripType();

    @GET("api/trip")
    Call<TripPaginationModel> getTripPost(@Query("page") int page);

    @GET("api/trip/{id}/details")
    Call<TripDetailsModel> getTripDetails(@Path("id") int id);

    @GET("api/notifications")
    Call<NotificationModel> getNotifications();

    @GET("api/user/logout")
    Call<ResponseBody> logOut();

    @GET("api/trip/road-map/{id}")
    Call<RoadMapModel> getRoadMap(@Path("id") int tripId);

    @GET("api/place/details/{id}")
    Call<PlaceDetailsModel> getPlaceDetails(@Path("id") int id);

    @GET("api/polls")
    Call<PollsPaginationModel> getPolls(@Query("page") int page);

    @GET("api/favorites/places")
    Call<PlaceModel> getFavoritePlace();

    @GET("api/organizer/index")
    Call<OrganizersModel> getAllOrganizers(@Query("page") int page);

    @GET("api/followers")
    Call<OrganizersModel> getAllFollowedOrganizers(@Query("page") int page);


    /**
     * Post Request
     */

    @POST("api/user/login")
    Call<LoginModel> login(@Body JsonObject loginReqBody);


    @POST("api/user/register")
    Call<ProfileData> registerPassenger(@Body JsonObject profile);

    @POST("api/place/favorite")
    Call<ResponseBody> addToFavorite(@Body JsonObject favoritePlace);

    @POST("api/search/trip")
    Call<TripPaginationModel> searchForTrip(@Body JsonObject searchObject);

    @POST("api/trip/rate")
    Call<ResponseBody> rateTrip(@Body JsonObject rate);

    @POST("api/bookings/book")
    Call<ResponseBody> bookTrip(@Body BookTripModel model);

    @POST("api/user/report")
    Call<ResponseBody> reportUser(@Body JsonObject object);

    @POST("api/search/place")
    Call<SearchPlaceModel> searchForPlace(@Body JsonObject searchPlaceObj, @Query("page") int page);

    @POST("api/trip/upcoming/customer")
    Call<TripPaginationModel> getUpcomingTrips(@Body JsonObject filterModel, @Query("page") int page);

    @POST("api/trip/active/customer")
    Call<TripPaginationModel> getActiveTrips(@Body JsonObject filterModel, @Query("page") int page);

    @POST("api/trip/history/customer")
    Call<TripPaginationModel> getHistoryTrips(@Body JsonObject filterModel, @Query("page") int page);

    @POST("api/user/password/request")
    Call<ResponseBody> checkPhoneNumberExist(@Body JsonObject phoneNumber);

    @POST("api/user/password/reset")
    Call<ResponseBody> resetPassword(@Body JsonObject resetPassword);

    /**
     * Put Request
     */

    @Multipart
    @POST("api/user/profile/customer/edit")
    Call<ResponseBody> editProfile(@Part("_method") RequestBody methodName,
                                   @Part("first_name") RequestBody firstName,
                                   @Part("last_name") RequestBody lastName,
                                   @Part("email") RequestBody email,
                                   @Part MultipartBody.Part photo);

    @PUT("api/user/mobile-token")
    Call<ResponseBody> sendFirebaseToken(@Body JsonObject firebaseObj);

    @PUT("api/polls/vote/{pollId}/{choiceId}")
    Call<ResponseBody> voteOnPoll(@Path("pollId")int pollId,@Path("choiceId") int choiceId);

    @PUT("api/bookings/{trip_id}/user/cancel")
    Call<ResponseBody> cancelBooking(@Path("trip_id") int tripId);

    @PUT("api/followers/follow/{organizer_user_id}")
    Call<ResponseBody> followOrganizer(@Path("organizer_user_id") int organizerId);


    /**
     * Delete Request
     */
}