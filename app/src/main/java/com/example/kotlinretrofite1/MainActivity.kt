package com.example.kotlinretrofite1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
//import sun.util.logging.LoggingSupport.setLevel
import java.io.IOException


class MainActivity : AppCompatActivity() {

    //val BASE_URL = "http://publicobject.com/"
    val BASE_URL = "https://community-open-weather-map.p.rapidapi.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        runSelectListInitializer()

        runRetro()
    }

    fun runSelectListInitializer(){
        // Initializing a String Array
        val cities = arrayOf("Gdańsk", "Wrocław", "Kraków", "Rzeszów")

        // Initializing an ArrayAdapter
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            cities // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter;

        // Set an on item selected listener for spinner object
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                textView.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
    }


    // function to call server and update ui
    fun runRetro() {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        // AWO 0. zmiana - stworzymy okHttpClinet builder
        val okHttpClientBuilder = OkHttpClient.Builder()
        // add your other interceptors …
/*        okHttpClientBuilder.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", UUID.randomUUID().toString())
                .build()
            chain.proceed(newRequest)

        }*/
        // AWO : Zmiana wlasciwosci requestu przez intercept , przedziwna metoda
/*        okHttpClientBuilder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original: Request = chain.request()
                // Request customization: add request headers
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url("https://community-open-weather-map.p.rapidapi.com/weather?callback=test&id=2172797&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=London%252Cuk")
                    .get()
                    .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "4550463df6mshf7c6157d3e801b0p12d6d3jsn3ce2cd6e5ea5")
                val request: Request = requestBuilder.build()
                return chain.proceed(request)
            }
        })*/
        // add logging as last interceptor
        okHttpClientBuilder.addInterceptor(logging);  // <-- this is the important line!


        // AWO 1. definiowany Builder + stworzenie obj klasy retrofit przez .build
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)  // AWO : podstawowa specyfikacja baseUrl
            //.baseUrl("https://api.github.com/")  // AWO : podstawowa specyfikacja baseUrl
            //.addConverterFactory(GsonConverterFactory.create()) // GSON potrzebny do konwersji pomiedzy objektami Java , a JSON
            .addConverterFactory(ScalarsConverterFactory.create()) // jesli Plain String to inny konwerter , np Scalar
            .client(okHttpClientBuilder.build())
            .build()

        // AWO 2. teraz stworze request
        //instancja klienta (za pomoca interfejsu)
        var clientWeatherApi = retrofit.create(WeatherAPI::class.java)

        // AWO 3.  wywolanie metody na kliencie
        //var stringCall = clientApi.getUsers()
        val call = clientWeatherApi.getWeatherInfo(
            //"https://community-open-weather-map.p.rapidapi.com/weather?callback=test&id=2172797&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=London%252Cuk"
            //"https://community-open-weather-map.p.rapidapi.com/forecast/daily?q=san%20francisco%252Cus&lat=35&lon=139&cnt=10&units=metric%20or%20imperial"
            "https://community-open-weather-map.p.rapidapi.com/weather?callback=test&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=Wroclaw"

        )// AWO 4. Execute synchronicznie albo asychronicznie
        //przypadek asynchroniczny - enqueue

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val output = response.body()
                val output2 = response.body()!!.string()

                println("OK 1: $output2")

                //listView.setAdapter(GitHubRepoAdapter(this@MainActivity, repos))
            }

/*            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                println("COS POSZLO NIE TAK : $t")
            }*/

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("COS POSZLO NIE TAK : $t")
            }
        })

    }
}
