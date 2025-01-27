package com.lopezgagonuria_pmdm.u3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lopezgagonuria_pmdm.u3.databinding.FragmentPokedexBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que muestra una Pokédex cargando datos desde una API y sincronizándolos con Firestore.
 */
public class PokedexFragment extends Fragment {

    // View Binding para acceder a las vistas del diseño.
    private FragmentPokedexBinding binding;

    // Adaptador para el RecyclerView.
    private PokemonAdapter adapter;

    // Lista de Pokémon mostrada en la Pokédex.
    private List<PokemonDetails> pokemonList;

    // Firestore y referencia a la colección "capturados".
    private FirebaseFirestore firestore;
    private CollectionReference capturadosRef;

    /**
     * Método que se ejecuta al crear la vista del fragmento.
     *
     * @param inflater Inflador para las vistas.
     * @param container Contenedor padre.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista raíz del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokedexBinding.inflate(inflater, container, false);

        // Inicializar Firestore y la referencia a la colección "capturados".
        firestore = FirebaseFirestore.getInstance();
        capturadosRef = firestore.collection("capturados");

        // Inicializar la lista de Pokémon.
        pokemonList = new ArrayList<>();

        // Configurar RecyclerView con el adaptador.
        adapter = new PokemonAdapter(pokemonList, pokemon -> {
            if (pokemon.getId() != null && !pokemon.getId().isEmpty()) {
                pokemon.setCaptured(true);
                int index = pokemonList.indexOf(pokemon);
                if (index != -1) {
                    pokemonList.set(index, pokemon);
                }
                capturadosRef.document(pokemon.getId())
                        .set(pokemon)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "¡Pokémon capturado guardado con éxito!"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar el Pokémon", e));
                adapter.notifyItemChanged(index);
            }
        }, capturadosRef);

        binding.recyclerPokemon.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPokemon.setAdapter(adapter);

        // Cargar datos de la API y sincronizar con Firestore.
        loadPokemonDataAndSync();

        return binding.getRoot();
    }

    /**
     * Carga los datos de Pokémon desde la API y los sincroniza con Firestore.
     */
    private void loadPokemonDataAndSync() {
        Log.d("PokedexFragment", "Cargando datos de la API y sincronizando...");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerPokemon.setVisibility(View.GONE);

        PokemonApiService apiService = RetrofitClient.getRetrofitInstance().create(PokemonApiService.class);
        apiService.getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("PokedexFragment", "Datos de la API recibidos correctamente.");
                    pokemonList.clear();
                    List<PokemonResponse.Result> results = response.body().getResults();
                    for (PokemonResponse.Result result : results) {
                        PokemonDetails pokemon = result.toPokemonDetails();
                        pokemonList.add(pokemon);
                    }
                    adapter.notifyDataSetChanged(); // Actualizar la lista con datos iniciales.
                    syncWithFirestore();
                } else {
                    Log.e("PokedexFragment", "Error al obtener datos de la API: " + response.message());
                    Toast.makeText(getContext(), "Error al obtener datos de la API", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("PokedexFragment", "Error al conectar con la API: " + t.getMessage());
                Toast.makeText(getContext(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Sincroniza la lista de Pokémon con los datos almacenados en Firestore.
     */
    private void syncWithFirestore() {
        Log.d("PokedexFragment", "Sincronizando datos con Firestore...");
        capturadosRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                PokemonDetails capturedPokemon = document.toObject(PokemonDetails.class);
                if (capturedPokemon != null && capturedPokemon.getId() != null) {
                    for (PokemonDetails pokemon : pokemonList) {
                        if (pokemon.getId().equals(capturedPokemon.getId())) {
                            pokemon.setCaptured(true);
                            pokemon.setTypes(capturedPokemon.getTypes());
                            pokemon.setWeight(capturedPokemon.getWeight());
                            pokemon.setHeight(capturedPokemon.getHeight());
                            pokemon.setName(capturedPokemon.getName());
                            pokemon.setFullyLoaded(true); // Marcar como completamente cargado.
                        }
                    }
                }
            }
            updateUIAfterSync();
        }).addOnFailureListener(e -> {
            Log.e("PokedexFragment", "Error al sincronizar con Firestore", e);
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error al sincronizar datos con Firestore", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Actualiza la interfaz de usuario después de sincronizar con Firestore.
     */
    private void updateUIAfterSync() {
        Log.d("PokedexFragment", "Actualizando UI después de la sincronización...");
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerPokemon.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    /**
     * Método que se ejecuta al destruir la vista del fragmento.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Liberar el binding para evitar fugas de memoria.
    }
}