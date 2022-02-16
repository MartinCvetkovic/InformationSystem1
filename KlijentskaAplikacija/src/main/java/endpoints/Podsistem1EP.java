package endpoints;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
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
public interface Podsistem1EP {
    
    @GET("http://localhost:8080/CentralniServer/resources/podsistem1/mesto/")
    public Call<List<Mesto>> getMesta();
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem1/mesto/")
    public Call<Mesto> createMesto(@Body Mesto mesto);
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem1/filijala/")
    public Call<Filijala> createFilijala(@Body Filijala filijala);
    
    @GET("http://localhost:8080/CentralniServer/resources/podsistem1/filijala/")
    public Call<List<Filijala>> getFilijale();
    
    @POST("http://localhost:8080/CentralniServer/resources/podsistem1/komitent/")
    public Call<Komitent> createKomitent(@Body Komitent komitent);
    
    @GET("http://localhost:8080/CentralniServer/resources/podsistem1/komitent/")
    public Call<List<Komitent>> getKomitenti();
    
    @PUT("http://localhost:8080/CentralniServer/resources/podsistem1/komitent{idKom}/{idMes}/")
    public Call<Komitent> changeMestoForKomitent(@Path("idKom") int idKom, @Path("idMes") int idMes);
    
}