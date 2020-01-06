package com.example.kotlinretrofite1;

//import io.reactivex.Observable

import retrofit2.Call;
import retrofit2.http.GET;
//import retrofit2.http.Query

/**
 * Created by Eyehunt Team on 12/06/18.
 */

public interface Api {

    //@GET("users?q=rokano")
    //Call<UsersList> getUsers();
    //AWO : ten interfejs jest do ogarniecia
    // ale w takim formacie get nie wymaga parametru i wywoluje ponizsza metode
    //@GET("users/{user}/repos")
    //Call getUsers();
    //Call<String> getStringResponse(@Url String url);
    //brak parametrow
    //Call<String> getUsers();
    //AWO w moim przykladzie , chce pozyskac wystawionego Stringa
    //ale trzeba zwrapowac odpowiedz do obj Call <Typ>

    //AWO kiedy wyciagamy plain text :
    @GET("helloworld.txt")
    Call<String> getPlainText();



}
