package com.example.scheduleapp.Retro;

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
        String baseUrl = "http://10.0.2.2:8080";

        synchronized (RetroController.class){
            Gson gson = new GsonBuilder()
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
