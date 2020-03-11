package com.example.scheduleapp.retro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroController {
    private static RetroController instance;
    private Retrofit retrofit;

    private RetroController(){
        buildService();
        return;
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public static RetroController getInstance() {
        if(instance == null)
            instance = new RetroController();

        return instance;
    }

    public void buildService(){
        String baseUrl = "localhost";

        synchronized (RetroController.class){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            GsonConverterFactory factory = GsonConverterFactory.create(gson);

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(factory)
                    .build();
        }

        return ;
    }
}
