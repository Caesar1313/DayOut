package com.example.dayout.viewModels;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.dayout.api.ApiClient;
import com.example.dayout.models.poll.PollsPaginationModel;
import com.example.dayout.models.trip.BookTripModel;
import com.example.dayout.models.trip.TripPaginationModel;
import com.example.dayout.models.trip.roadMap.RoadMapModel;
import com.example.dayout.models.trip.TripDetailsModel;
import com.example.dayout.models.trip.tripType.TripTypeModel;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.config.AppConstants.getErrorMessage;

public class TripViewModel {

    private final ApiClient apiClient = new ApiClient();
    private static TripViewModel instance;

    public static final String TRIP_PHOTOS_URL = BASE_URL + "api/trip/photo/";

    public MutableLiveData<Pair<TripPaginationModel, String>> upcomingTripsMutableLiveData;
    public MutableLiveData<Pair<TripPaginationModel, String>> activeTripsMutableLiveData;
    public MutableLiveData<Pair<TripPaginationModel, String>> historyTripsMutableLiveData;
    public MutableLiveData<Pair<TripPaginationModel, String>> tripPostMutableLiveData;
    public MutableLiveData<Pair<TripTypeModel, String>> tripTypeTripMutableLiveData;
    public MutableLiveData<Pair<ResponseBody, String>> rateTripMutableLiveData;
    public MutableLiveData<Pair<TripDetailsModel, String>> tripDetailsMutableLiveData;
    public MutableLiveData<Pair<RoadMapModel, String>> roadMapMutableLiveData;
    public MutableLiveData<Pair<PollsPaginationModel, String>> pollMutableLiveData;
    public MutableLiveData<Pair<Boolean, String>> successfulMutableLiveData;
    public MutableLiveData<Pair<ResponseBody, String>> bookTripMutableLiveData;
    public MutableLiveData<Pair<Boolean, String>> cancelBookingMutableLiveData;

    public static TripViewModel getINSTANCE() {
        if (instance == null) {
            instance = new TripViewModel();
        }
        return instance;
    }

    public void getUpcomingTrips(JsonObject filterModel, int page) {
        upcomingTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getUpcomingTrips(filterModel, page).enqueue(new Callback<TripPaginationModel>() {
            @Override
            public void onResponse(Call<TripPaginationModel> call, Response<TripPaginationModel> response) {
                if (response.isSuccessful()) {
                    upcomingTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        upcomingTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripPaginationModel> call, Throwable t) {
                upcomingTripsMutableLiveData.setValue(null);
            }
        });
    }

    public void getActiveTrips(JsonObject filterModel, int page) {
        activeTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getActiveTrips(filterModel, page).enqueue(new Callback<TripPaginationModel>() {
            @Override
            public void onResponse(Call<TripPaginationModel> call, Response<TripPaginationModel> response) {
                if (response.isSuccessful()) {
                    activeTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        activeTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripPaginationModel> call, Throwable t) {
                activeTripsMutableLiveData.setValue(null);
            }
        });
    }

    public void getHistoryTrips(JsonObject filterModel, int page) {
        historyTripsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getHistoryTrips(filterModel, page).enqueue(new Callback<TripPaginationModel>() {
            @Override
            public void onResponse(Call<TripPaginationModel> call, Response<TripPaginationModel> response) {
                if (response.isSuccessful()) {
                    historyTripsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        historyTripsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripPaginationModel> call, Throwable t) {
                historyTripsMutableLiveData.setValue(null);
            }
        });
    }

    public void getTripPost(int page) {
        tripPostMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripPost(page).enqueue(new Callback<TripPaginationModel>() {
            @Override
            public void onResponse(Call<TripPaginationModel> call, Response<TripPaginationModel> response) {
                if (response.isSuccessful()) {
                    tripPostMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripPostMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripPaginationModel> call, Throwable t) {
                tripPostMutableLiveData.setValue(null);
            }
        });
    }

    public void getTripType() {
        tripTypeTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripType().enqueue(new Callback<TripTypeModel>() {
            @Override
            public void onResponse(Call<TripTypeModel> call, Response<TripTypeModel> response) {
                if (response.isSuccessful()) {
                    tripTypeTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripTypeTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripTypeModel> call, Throwable t) {
                tripTypeTripMutableLiveData.setValue(null);
            }
        });
    }

    public void getTripDetails(int id) {
        tripDetailsMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getTripDetails(id).enqueue(new Callback<TripDetailsModel>() {
            @Override
            public void onResponse(Call<TripDetailsModel> call, Response<TripDetailsModel> response) {
                if (response.isSuccessful()) {
                    tripDetailsMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripDetailsMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripDetailsModel> call, Throwable t) {
                tripDetailsMutableLiveData.setValue(null);
            }
        });
    }

    public void getRoadMap(int tripId) {
        roadMapMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getRoadMap(tripId).enqueue(new Callback<RoadMapModel>() {
            @Override
            public void onResponse(Call<RoadMapModel> call, Response<RoadMapModel> response) {
                if (response.isSuccessful()) {
                    roadMapMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        roadMapMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RoadMapModel> call, Throwable t) {
                roadMapMutableLiveData.setValue(null);
            }
        });
    }

    public void getPolls(int page) {
        pollMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().getPolls(page).enqueue(new Callback<PollsPaginationModel>() {
            @Override
            public void onResponse(Call<PollsPaginationModel> call, Response<PollsPaginationModel> response) {
                if (response.isSuccessful()) {
                    pollMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        pollMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PollsPaginationModel> call, Throwable t) {
                pollMutableLiveData.setValue(null);
            }
        });
    }

    public void searchForTrip(JsonObject searchTrip) {
        tripPostMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().searchForTrip(searchTrip).enqueue(new Callback<TripPaginationModel>() {
            @Override
            public void onResponse(Call<TripPaginationModel> call, Response<TripPaginationModel> response) {
                if (response.isSuccessful()) {
                    tripPostMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        tripPostMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripPaginationModel> call, Throwable t) {
                tripPostMutableLiveData.setValue(null);
            }
        });
    }

    public void rateTrip(JsonObject rateObject) {
        rateTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().rateTrip(rateObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    rateTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        rateTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                rateTripMutableLiveData.setValue(null);
            }
        });
    }

    public void voteOnPoll(int pollId, int choiceId) {
        successfulMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().voteOnPoll(pollId, choiceId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    successfulMutableLiveData.setValue(new Pair<>(true, null));
                } else {
                    try {
                        successfulMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                successfulMutableLiveData.setValue(null);
            }
        });

    }

    public void bookTrip(BookTripModel model) {
        bookTripMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().bookTrip(model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    bookTripMutableLiveData.setValue(new Pair<>(response.body(), null));
                } else {
                    try {
                        bookTripMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                bookTripMutableLiveData.setValue(null);
            }
        });
    }

    public void cancelBooking(int tripId){
        cancelBookingMutableLiveData = new MutableLiveData<>();
        apiClient.getAPI().cancelBooking(tripId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    cancelBookingMutableLiveData.setValue(new Pair<>(true, null));
                } else {
                    try {
                        cancelBookingMutableLiveData.setValue(new Pair<>(null, getErrorMessage(response.errorBody().string())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                cancelBookingMutableLiveData.setValue(null);
            }
        });
    }
}
