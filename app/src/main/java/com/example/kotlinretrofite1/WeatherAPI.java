package com.example.kotlinretrofite1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface WeatherAPI {
    @Headers({"x-rapidapi-host: community-open-weather-map.p.rapidapi.com",
            "x-rapidapi-key:4550463df6mshf7c6157d3e801b0p12d6d3jsn3ce2cd6e5ea5"})
    //@GET("helloworld.txt")
    //@GET() // AWO "where the request goes to "
    //@GET("weather?callback=test&id=2172797&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=London%252Cuk") // AWO "where the request goes to "
    @GET
    Call<ResponseBody> getWeatherInfo(@Url String url);
}
