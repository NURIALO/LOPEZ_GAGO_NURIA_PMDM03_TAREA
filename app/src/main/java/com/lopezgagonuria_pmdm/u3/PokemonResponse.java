package com.lopezgagonuria_pmdm.u3;

import android.util.Log;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase que representa la respuesta obtenida de la API de Pokémon.
 * Contiene una lista de resultados, cada uno representando un Pokémon básico con nombre y URL.
 */
public class PokemonResponse {

    // Lista de resultados de la API.
    private List<Result> results;

    /**
     * Obtiene la lista de resultados de la respuesta.
     *
     * @return Lista de objetos Result.
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Establece la lista de resultados de la respuesta.
     *
     * @param results Lista de objetos Result.
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * Clase interna que representa cada resultado individual de la API de Pokémon.
     */
    public static class Result {

        // Nombre del Pokémon.
        private String name;

        // URL asociada al Pokémon.
        private String url;

        /**
         * Obtiene el nombre del Pokémon.
         *
         * @return Nombre del Pokémon.
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre del Pokémon.
         *
         * @param name Nombre del Pokémon.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtiene la URL asociada al Pokémon.
         *
         * @return URL del Pokémon.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Establece la URL asociada al Pokémon.
         *
         * @param url URL del Pokémon.
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Convierte el resultado en un objeto PokemonDetails.
         * Genera información básica, como el ID y la URL del sprite.
         *
         * @return Objeto PokemonDetails con datos iniciales.
         */
        public PokemonDetails toPokemonDetails() {
            // Extrae el ID del Pokémon desde la URL.
            String[] parts = url.split("/");
            String id = parts[parts.length - 1];

            // Genera la URL del sprite usando el ID.
            String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

            // Crea un objeto básico de PokemonDetails.
            PokemonDetails pokemon = new PokemonDetails(
                    id,
                    name,
                    0, // Peso predeterminado.
                    0, // Altura predeterminada.
                    spriteUrl, // URL del sprite generado.
                    url,
                    null, // Tipos no disponibles inicialmente.
                    false, // No capturado inicialmente.
                    false  // No clicado inicialmente.
            );

            // Realiza una solicitud secundaria para obtener los detalles del Pokémon (incluidos los tipos).
            fetchPokemonDetails(id, pokemon);
            return pokemon;
        }

        /**
         * Realiza una solicitud a la API para obtener los detalles completos de un Pokémon.
         *
         * @param pokemonId ID del Pokémon para realizar la solicitud.
         * @param pokemon Objeto PokemonDetails a actualizar con los datos obtenidos.
         */
        private void fetchPokemonDetails(String pokemonId, PokemonDetails pokemon) {
            PokemonApiService apiService = RetrofitClient.getRetrofitInstance().create(PokemonApiService.class);
            apiService.getPokemonDetails(pokemonId).enqueue(new Callback<PokemonDetails>() {
                @Override
                public void onResponse(Call<PokemonDetails> call, Response<PokemonDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PokemonDetails details = response.body();

                        // Actualiza los tipos del objeto PokemonDetails original.
                        pokemon.setTypes(details.getTypes());
                    } else {
                        Log.e("PokemonResponse", "Error al obtener detalles: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<PokemonDetails> call, Throwable t) {
                    Log.e("PokemonResponse", "Error al realizar la solicitud: " + t.getMessage());
                }
            });
        }
    }
}