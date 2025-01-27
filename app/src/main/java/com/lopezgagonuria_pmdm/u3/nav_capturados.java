package com.lopezgagonuria_pmdm.u3;

import static android.content.ContentValues.TAG;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lopezgagonuria_pmdm.u3.databinding.FragmentNavCapturadosBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que muestra una lista de Pokémon capturados y permite gestionarlos desde Firestore.
 */
public class nav_capturados extends Fragment {

    // View Binding para acceder a las vistas del diseño de forma segura.
    private FragmentNavCapturadosBinding binding;

    // Adaptador para el RecyclerView.
    private CapturadosAdapter adapter;

    // Lista de Pokémon capturados.
    private List<PokemonDetails> pokemonCapturadosList;

    // Instancias de Firebase Firestore para acceder a la base de datos.
    private FirebaseFirestore firestore;
    private CollectionReference capturadosRef;

    /**
     * Método que se ejecuta al crear el fragmento.
     *
     * @param savedInstanceState Estado guardado del fragmento.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Método que infla la vista del fragmento y configura la lógica necesaria.
     *
     * @param inflater Inflador para las vistas.
     * @param container Contenedor padre.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista raíz del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Configurar View Binding para inflar el diseño de forma segura.
        binding = FragmentNavCapturadosBinding.inflate(inflater, container, false);

        // Inicializar Firestore y la referencia a la colección "capturados".
        firestore = FirebaseFirestore.getInstance();
        capturadosRef = firestore.collection("capturados");

        // Inicializar la lista de Pokémon capturados.
        pokemonCapturadosList = new ArrayList<>();

        // Verificar si se pasó un Pokémon a través del Bundle.
        Bundle bundle = getArguments();
        if (bundle != null) {
            PokemonDetails selectedPokemon = (PokemonDetails) bundle.getSerializable("selectedPokemon");
            if (selectedPokemon != null) {
                Log.d(TAG, "Objeto seleccionado recibido: " + selectedPokemon.toString());
                savePokemonToFirestore(selectedPokemon); // Guardar el Pokémon en Firestore.
            } else {
                Log.e(TAG, "selectedPokemon es nulo");
            }
        }

        // Configurar el adaptador para el RecyclerView.
        adapter = new CapturadosAdapter(pokemonCapturadosList, new CapturadosAdapter.OnPokemonClickListener() {
            @Override
            public void onPokemonClick(PokemonDetails pokemon) {
                // Configurar visibilidad del contenedor de detalles.
                View fragmentContainer = requireActivity().findViewById(R.id.fragment_container);
                if (fragmentContainer != null && fragmentContainer.getVisibility() == View.INVISIBLE) {
                    fragmentContainer.setVisibility(View.VISIBLE);
                }

                // Abrir el fragmento de detalles del Pokémon.
                PokemonDetailsFragment fragment = PokemonDetailsFragment.newInstance(pokemon);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDeletePokemon(PokemonDetails pokemon) {
                deletePokemonFromFirestore(pokemon);
            }
        });

        // Configurar RecyclerView.
        binding.recyclerCapturados.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCapturados.setAdapter(adapter);

        // Configurar deslizamiento para eliminar elementos.
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                boolean canDelete = sharedPreferences.getBoolean("allow_delete", true);
                if (!canDelete) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    Toast.makeText(getContext(), R.string.habilitar_eliminar, Toast.LENGTH_SHORT).show();
                    return;
                }

                int position = viewHolder.getAdapterPosition();
                PokemonDetails pokemon = pokemonCapturadosList.get(position);
                deletePokemonFromFirestore(pokemon);
                Toast.makeText(getContext(), "Se ha eliminado el Pokemon correctamente!!", Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerCapturados);

        // Mostrar el ProgressBar mientras se cargan los datos.
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyView.setVisibility(View.GONE);

        // Cargar los datos de Pokémon capturados desde Firestore.
        loadCapturedPokemon();

        return binding.getRoot();
    }

    /**
     * Guarda un Pokémon en Firestore si sus datos son válidos.
     *
     * @param pokemon Objeto PokemonDetails a guardar.
     */
    private void savePokemonToFirestore(PokemonDetails pokemon) {
        if (pokemon.getSpriteUrl() == null || pokemon.getSpriteUrl().isEmpty()) {
            Log.e(TAG, "El campo spriteUrl está vacío o es nulo.");
            return;
        }
        if (pokemon.getTypes() == null || pokemon.getTypes().isEmpty()) {
            Log.e(TAG, "El campo types está vacío o es nulo.");
            return;
        }
        if (pokemon.getName() == null || pokemon.getName().isEmpty()) {
            Log.e(TAG, "El campo name está vacío o es nulo.");
            return;
        }

        for (PokemonDetails.Type type : pokemon.getTypes()) {
            if (type == null || type.getType() == null || type.getType().getName() == null) {
                Log.e(TAG, "Un tipo tiene datos incompletos o es nulo.");
                return;
            }
        }

        firestore.collection("capturados")
                .document(pokemon.getId())
                .set(pokemon)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "¡Pokémon guardado correctamente en Firestore!"))
                .addOnFailureListener(e -> Log.e(TAG, "Error al guardar el Pokémon en Firestore: " + e.getMessage()));
    }

    /**
     * Carga la lista de Pokémon capturados desde Firestore y actualiza el RecyclerView.
     */
    private void loadCapturedPokemon() {
        capturadosRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    binding.progressBar.setVisibility(View.GONE);
                    pokemonCapturadosList.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        PokemonDetails capturedPokemon = document.toObject(PokemonDetails.class);
                        if (capturedPokemon != null && capturedPokemon.getName() != null) {
                            pokemonCapturadosList.add(capturedPokemon);
                        }
                    }

                    if (pokemonCapturadosList.isEmpty()) {
                        binding.emptyView.setVisibility(View.VISIBLE);
                    } else {
                        binding.emptyView.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al leer datos de Firestore", e);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyView.setVisibility(View.VISIBLE);
                });
    }

    /**
     * Elimina un Pokémon de la lista local y actualiza el RecyclerView.
     *
     * @param pokemon Objeto PokemonDetails a eliminar de la lista.
     */
    public void removePokemonFromList(PokemonDetails pokemon) {
        Log.d(TAG, "Intentando eliminar: " + pokemon.getName());
        if (pokemonCapturadosList != null && adapter != null) {
            for (int i = 0; i < pokemonCapturadosList.size(); i++) {
                if (pokemonCapturadosList.get(i).getId().equals(pokemon.getId())) {
                    Log.d(TAG, "Pokémon encontrado en la lista: " + pokemon.getName());
                    pokemonCapturadosList.remove(i);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
            Log.d(TAG, "Pokémon no encontrado en la lista.");
        }
    }

    /**
     * Elimina un Pokémon de Firestore y actualiza la lista local.
     *
     * @param pokemon Objeto PokemonDetails a eliminar de Firestore.
     */
    private void deletePokemonFromFirestore(PokemonDetails pokemon) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean canDelete = sharedPreferences.getBoolean("allow_delete", true);
        if (!canDelete) {
            Log.d(TAG, "No está permitido eliminar Pokémon. Operación cancelada.");
            Toast.makeText(getContext(), "La opción eliminar está deshabilitada.", Toast.LENGTH_SHORT).show();
            return;
        }

        capturadosRef.document(pokemon.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    pokemonCapturadosList.removeIf(p -> p.getId().equals(pokemon.getId()));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "El Pokemon se ha eliminado con éxito!!.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error al eliminar el Pokémon de Firestore: " + e.getMessage()));
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