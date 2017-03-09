package com.akdeniza.gatt_explorer.lib.REST;

import com.akdeniza.gatt_explorer.lib.model.GitHubReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *  The GitHubInterface to receive the GATT file using the hash
 *  @author Akdeniz on 02/03/2017.
 */

public interface GitHubInterface {

    @GET("/akdeniza/gatt-explorer-database/master/data/{hash}.json")
    Call<GitHubReponse> getGATTJsonFromHash(
            @Path("hash") String hash
    );

}

