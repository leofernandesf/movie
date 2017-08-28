package com.example.leo.gridexemple.webservice;

import com.example.leo.gridexemple.model.Example;
import com.example.leo.gridexemple.model.Trailers;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by leo on 22/05/17.
 */

public interface MovieService {

    @GET("popular")
    Call<ResponseBody> getPopularMovie(@QueryMap Map<String, String> options);

    @GET("top_rated")
    Call<ResponseBody> getHighestMovie(@QueryMap Map<String, String> options);

    @GET("{id}/trailers")
    Call<Trailers> getTrailersMovie(@Path("id") Integer id, @QueryMap Map<String, String> options);

    @GET("{id}/reviews")
    Call<Example> getReviewMovie(@Path("id") Integer id, @QueryMap Map<String, String> options);


}
