/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Isplata;
import entities.Prenos;
import entities.Racun;
import entities.Transakcija;
import entities.Uplata;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 *
 * @author Martin
 */
public interface Podsistem2EP {
    
    @GET("http://localhost:8080/CentralniServer/resources/podsistem2/racun/{idKom}/")
    public Call<List<Racun>> getRacunForKomitent(@Path("idKom") int idKom);
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem2/prenos/{IdRac}/")
    public Call<Prenos> createPrenos(@Body Transakcija transakcija, @Path("IdRac") int idRac);
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem2/uplata/{IdFil}/")
    public Call<Uplata> createUplata(@Body Transakcija transakcija, @Path("IdFil") int idFil);
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem2/isplata/{IdFil}/")
    public Call<Isplata> createIsplata(@Body Transakcija transakcija, @Path("IdFil") int idFil);
    
    @GET("http://localhost:8080/CentralniServer/resources/podsistem2/transakcija/{idRac}/")
    public Call<List<Transakcija>> getTransakcijaForRacun(@Path("idRac") int idRac);
    
    @PUT("http://localhost:8080/CentralniServer/resources/podsistem2/racun{idRac}/")
    public Call<Racun> closeRacun (@Path("idRac") int idRac);
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem2/racun/")
    public Call<Racun> createRacun(@Body Racun racun);
    
}
