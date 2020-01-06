package com.example.kotlinretrofite1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    val BASE_URL = "http://publicobject.com/"

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
        // AWO 1. definiowany Builder + stworzenie obj klasy retrofit przez .build
        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)  // AWO : podstawowa specyfikacja baseUrl
            //.baseUrl("https://api.github.com/")  // AWO : podstawowa specyfikacja baseUrl
            //.addConverterFactory(GsonConverterFactory.create()) // GSON potrzebny do konwersji pomiedzy objektami Java , a JSON
            .addConverterFactory(ScalarsConverterFactory.create()) // jesli Plain String to inny konwerter , np Scalar
            .build()

        // AWO 2. teraz stworze request
        //instancja klienta (za pomoca interfejsu)
        var clientApi = retrofit.create(Api::class.java)

        // AWO 3.  wywolanie metody na kliencie
        //var stringCall = clientApi.getUsers()
        val call = clientApi.getPlainText()

        // AWO 4. Execute synchronicznie albo asychronicznie
        //przypadek asynchroniczny - enqueue

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                val output = response.body()
                println("OK : $output")
                textView.text = output
                //listView.setAdapter(GitHubRepoAdapter(this@MainActivity, repos))
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                println("COS POSZLO NIE TAK : $t")
            }
        })

    }
}
