package com.lopezgagonuria_pmdm.u3;

//Primero, necesitamos un cliente de Retrofit para realizar las peticiones.
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para inicializar y proporcionar una instancia singleton de Retrofit,
 * utilizada para realizar solicitudes HTTP a la API de Pokémon.
 */
public class RetrofitClient {

    // Instancia singleton de Retrofit.
    private static Retrofit retrofit;

    // URL base de la API de Pokémon.
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    /**
     * Proporciona una instancia singleton de Retrofit para realizar solicitudes HTTP.
     *
     * @return Instancia de Retrofit inicializada con la URL base y un convertidor JSON.
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Configura Retrofit con la URL base y un convertidor JSON.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // URL base de la API.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertidor de JSON a objetos.
                    .build();
        }
        return retrofit;
    }
}

