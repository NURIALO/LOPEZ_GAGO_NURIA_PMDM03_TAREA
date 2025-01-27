package com.lopezgagonuria_pmdm.u3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lopezgagonuria_pmdm.u3.databinding.PokemonDetailFragmentBinding;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que muestra los detalles de un Pokémon seleccionado, permite la edición de los datos
 * y la eliminación del Pokémon de la base de datos Firestore.
 */
public class PokemonDetailsFragment extends Fragment {

    // View Binding para acceder a las vistas del diseño.
    private PokemonDetailFragmentBinding binding;

    /**
     * Crea una nueva instancia del fragmento con los detalles de un Pokémon.
     *
     * @param pokemonDetails Objeto PokemonDetails con la información del Pokémon.
     * @return Una nueva instancia de PokemonDetailsFragment.
     */
    public static PokemonDetailsFragment newInstance(PokemonDetails pokemonDetails) {
        PokemonDetailsFragment fragment = new PokemonDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("pokemonDetails", pokemonDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Infla la vista del fragmento y configura los eventos de los botones y la visualización de datos.
     *
     * @param inflater Inflador para las vistas.
     * @param container Contenedor padre.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista raíz inflada.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Configurar View Binding para inflar el diseño de forma segura.
        binding = PokemonDetailFragmentBinding.inflate(inflater, container, false);

        // Obtener el Pokémon desde los argumentos.
        PokemonDetails pokemon = (PokemonDetails) getArguments().getSerializable("pokemonDetails");

        // Mostrar los detalles del Pokémon usando View Binding.
        if (pokemon != null) {
            updateDetails(pokemon);
            binding.nameTextDetail.setText(pokemon.getName());
            binding.idPokemon.setText("ID: " + pokemon.getId());
            binding.typeTextDetail.setText(getString(R.string.tipo) + pokemon.getFormattedTypes());
            binding.weightTextDetail.setText(getString(R.string.peso) + pokemon.getWeight() + " kg");
            binding.heightTextDetail.setText(getString(R.string.altura) + pokemon.getHeight() + " m");

            // Cargar la imagen del Pokémon usando Glide.
            Glide.with(requireContext())
                    .load(pokemon.getSpriteUrl())
                    .into(binding.imageCharacterDetail);

            // Si los datos no están completamente cargados, buscar los detalles desde la API.
            if (!pokemon.isFullyLoaded()) {
                fetchPokemonDetails(pokemon);
            }
        }

        // Configurar el botón "Volver".
        binding.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Configurar el botón "Eliminar".
        binding.buttonDelete.setOnClickListener(v -> {
            // Verificar si la eliminación está permitida.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            boolean canDelete = sharedPreferences.getBoolean("allow_delete", true);
            if (canDelete) {
                // Eliminar el Pokémon si está permitido.
                deletePokemon(pokemon);
            } else {
                // Mostrar mensaje si no está permitido eliminar.
                Toast.makeText(getContext(), R.string.habilitar_eliminar, Toast.LENGTH_SHORT).show();
            }
        });

        // Devolver la vista raíz inflada por View Binding.
        return binding.getRoot();
    }

    /**
     * Elimina el Pokémon de Firestore y actualiza la lista en el fragmento padre.
     *
     * @param pokemon Objeto PokemonDetails a eliminar.
     */
    private void deletePokemon(PokemonDetails pokemon) {
        if (pokemon != null) {
            FirebaseFirestore.getInstance().collection("capturados")
                    .document(pokemon.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("PokemonDetailsFragment", "Pokémon eliminado de Firestore: " + pokemon.getName());
                        Fragment parentFragment = requireActivity().getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment);
                        if (parentFragment != null && parentFragment.getChildFragmentManager() != null) {
                            List<Fragment> fragments = parentFragment.getChildFragmentManager().getFragments();
                            for (Fragment fragment : fragments) {
                                if (fragment instanceof nav_capturados) {
                                    Log.d("PokemonDetailsFragment", "Fragmento nav_capturados encontrado.");
                                    ((nav_capturados) fragment).removePokemonFromList(pokemon);
                                    break;
                                }
                            }
                        } else {
                            Log.e("PokemonDetailsFragment", "Fragmento nav_capturados no encontrado.");
                        }
                        requireActivity().getSupportFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PokemonDetailsFragment", "Error al eliminar Pokémon: " + e.getMessage());
                    });
        }
    }

    /**
     * Obtiene los detalles del Pokémon desde la API y los actualiza en Firestore y en la interfaz de usuario.
     *
     * @param pokemon Objeto PokemonDetails del cual obtener los detalles.
     */
    private void fetchPokemonDetails(PokemonDetails pokemon) {
        if (pokemon == null || pokemon.getId() == null) {
            Log.e("PokemonDetailsFragment", "El Pokémon o su ID es nulo.");
            return;
        }

        // Crear una instancia del servicio de la API.
        PokemonApiService apiService = RetrofitClient.getRetrofitInstance().create(PokemonApiService.class);

        // Llamada a la API para obtener detalles del Pokémon.
        apiService.getPokemonDetails(pokemon.getId()).enqueue(new Callback<PokemonDetails>() {
            @Override
            public void onResponse(Call<PokemonDetails> call, Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar el Pokémon con los detalles obtenidos.
                    PokemonDetails details = response.body();
                    pokemon.setWeight(details.getWeight());
                    pokemon.setHeight(details.getHeight());
                    pokemon.setTypes(details.getTypes());
                    pokemon.setId(details.getId());
                    pokemon.setFullyLoaded(true);

                    // Actualizar la UI con los nuevos datos.
                    updateDetails(pokemon);

                    // Guardar los detalles actualizados en Firebase.
                    FirebaseFirestore.getInstance()
                            .collection("capturados")
                            .document(pokemon.getId())
                            .set(pokemon)
                            .addOnSuccessListener(aVoid -> Log.d("PokemonDetailsFragment", "Detalles actualizados en Firestore: " + pokemon.getName()))
                            .addOnFailureListener(e -> Log.e("PokemonDetailsFragment", "Error al actualizar los detalles en Firestore: " + e.getMessage()));

                    Log.d("PokemonDetailsFragment", "Detalles cargados correctamente para: " + pokemon.getName());
                } else {
                    Log.e("PokemonDetailsFragment", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PokemonDetails> call, Throwable t) {
                Log.e("PokemonDetailsFragment", "Error en la solicitud de detalles: " + t.getMessage());
            }
        });
    }

    /**
     * Actualiza los detalles del Pokémon en la interfaz de usuario.
     *
     * @param pokemon Objeto PokemonDetails con los datos a mostrar.
     */
    public void updateDetails(PokemonDetails pokemon) {
        if (pokemon == null || binding == null) {
            return;
        }

        // Actualizar nombre.
        binding.nameTextDetail.setText(pokemon.getName() != null ? pokemon.getName() : getString(R.string.dato_no_disponible));

        // Actualizar tipo(s).
        StringBuilder types = new StringBuilder();
        if (pokemon.getTypes() != null && !pokemon.getTypes().isEmpty()) {
            for (PokemonDetails.Type type : pokemon.getTypes()) {
                types.append(type.getType().getName()).append(" ");
            }
            binding.typeTextDetail.setText(getString(R.string.tipo) + types.toString().trim());
        } else {
            binding.typeTextDetail.setText(getString(R.string.tipo) + getString(R.string.dato_no_disponible));
        }

        // Actualizar peso.
        if (pokemon.getWeight() > 0) {
            binding.weightTextDetail.setText(getString(R.string.peso) + pokemon.getWeight() + " kg");
        } else {
            binding.weightTextDetail.setText(getString(R.string.peso) + getString(R.string.dato_no_disponible));
        }

        // Actualizar altura.
        if (pokemon.getHeight() > 0) {
            binding.heightTextDetail.setText(getString(R.string.altura) + pokemon.getHeight() + " m");
        } else {
            binding.heightTextDetail.setText(getString(R.string.altura) + getString(R.string.dato_no_disponible));
        }

        // Actualizar imagen.
        if (pokemon.getSpriteUrl() != null && !pokemon.getSpriteUrl().isEmpty()) {
            Glide.with(requireContext())
                    .load(pokemon.getSpriteUrl())
                    .into(binding.imageCharacterDetail);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Liberar el objeto de View Binding para evitar fugas de memoria.
    }
}