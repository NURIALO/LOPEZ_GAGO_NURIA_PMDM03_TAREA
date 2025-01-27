package com.lopezgagonuria_pmdm.u3;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para consumir la API de Pokémon utilizando Retrofit.
 */
public interface PokemonApiService {

    /**
     * Obtener los detalles de un Pokémon dado su ID o nombre.
     *
     * @param idOrName ID o nombre del Pokémon.
     * @return Llamada a la API para obtener los detalles del Pokémon.
     */
    @GET("pokemon/{idOrName}")
    Call<PokemonDetails> getPokemonDetails(@Path("idOrName") String idOrName);

    /**
     * Obtener una lista de Pokémon con paginación.
     *
     * @param offset Índice inicial de la lista.
     * @param limit Número máximo de resultados a obtener.
     * @return Llamada a la API para obtener una lista de Pokémon.
     */
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    /**
     * Clase singleton para manejar la instancia de Retrofit.
     */
    public class RetrofitInstance {

        // URL base de la API de Pokémon.
        private static final String BASE_URL = "https://pokeapi.co/api/v2/";

        // Instancia única de Retrofit.
        private static Retrofit retrofit;

        /**
         * Obtener la instancia de Retrofit configurada con la URL base y Gson.
         *
         * @return Instancia de Retrofit configurada.
         */
        public static Retrofit getInstance() {
            if (retrofit == null) {
                synchronized (RetrofitInstance.class) {
                    if (retrofit == null) { // Verificación doble para inicialización segura.
                        retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                    }
                }
            }
            return retrofit;
        }
    }
}
