/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Mesto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 * @author Martin
 */
public interface Podsistem3EP {

    @GET("http://localhost:8080/CentralniServer/resources/podsistem3/sve/")
    public Call<List<Object>> getSve();

    @GET("http://localhost:8080/CentralniServer/resources/podsistem3/razlika/")
    public Call<List<Object>> getRazlika();

}
