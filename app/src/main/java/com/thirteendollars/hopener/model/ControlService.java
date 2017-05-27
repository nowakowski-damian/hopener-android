package com.thirteendollars.hopener.model;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Damian Nowakowski on 03/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public interface ControlService {

    @Headers("Content-Type: application/json")
    @POST("api//RestController.php")
    Observable<Response> control(@Body Request request);

}
