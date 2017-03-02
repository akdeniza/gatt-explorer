package com.akdeniza.gatt_explorer.lib.REST;

import com.akdeniza.gatt_explorer.lib.model.GitHubRepose;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Akdeniz on 02/03/2017.
 */

public interface GitHubInterface {

    @GET("/akdeniza/gatt-explorer-database/master/data/{hash}.json")
    Call<GitHubRepose> getGATTJsonFromHash(
            @Path("hash") String hash
    );

}
