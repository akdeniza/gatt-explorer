package com.akdeniza.gatt_explorer.lib.REST;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *  The GitHubClient to request data of the gatt explorer database
 *  @author Akdeniz on 01/03/2017.
 */

public class GitHubClient {

    public static final String BASE_URL = "https://raw.githubusercontent.com";
    private static Retrofit retrofit = null;


    public static Retrofit getGitHubClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

}
