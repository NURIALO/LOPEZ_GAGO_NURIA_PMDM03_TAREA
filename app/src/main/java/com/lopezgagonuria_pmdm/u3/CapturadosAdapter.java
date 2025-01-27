package com.lopezgagonuria_pmdm.u3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lopezgagonuria_pmdm.u3.databinding.ItemCapturadoBinding;
import java.util.List;

/**
 * Adapter para manejar y mostrar una lista de Pokémon capturados en un RecyclerView.
 */
public class CapturadosAdapter extends RecyclerView.Adapter<CapturadosAdapter.CapturadosViewHolder> {

    // Lista de Pokémon capturados que se mostrarán
    private List<PokemonDetails> pokemonList;
    // Listener para manejar eventos de clic y eliminación
    private final OnPokemonClickListener listener;

    /**
     * Constructor para inicializar la lista de Pokémon y el listener.
     *
     * @param pokemonList Lista de objetos PokemonDetails.
     * @param listener Listener para manejar eventos de clic y eliminar.
     */
    public CapturadosAdapter(List<PokemonDetails> pokemonList, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    /**
     * Interfaz para manejar los eventos de clic en los elementos del RecyclerView.
     */
    public interface OnPokemonClickListener {
        /**
         * Método para manejar el evento de clic en un Pokémon.
         *
         * @param pokemon Objeto PokemonDetails que fue clicado.
         */
        void onPokemonClick(PokemonDetails pokemon);

        /**
         * Método para manejar el evento de clic en el botón de eliminar.
         *
         * @param pokemon Objeto PokemonDetails que se desea eliminar.
         */
        void onDeletePokemon(PokemonDetails pokemon);
    }

    /**
     * Método que infla el diseño de los elementos del RecyclerView utilizando ViewBinding.
     *
     * @param parent El ViewGroup padre al que se añadirá la vista.
     * @param viewType Tipo de vista (no usado aquí, ya que todas las vistas son iguales).
     * @return Una instancia de CapturadosViewHolder con el diseño inflado.
     */
    @NonNull
    @Override
    public CapturadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada elemento utilizando el binding generado automáticamente
        ItemCapturadoBinding binding = ItemCapturadoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CapturadosViewHolder(binding);
    }

    /**
     * Método que vincula los datos de un Pokémon con la vista correspondiente en el RecyclerView.
     *
     * @param holder El ViewHolder que contiene la vista para el elemento.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull CapturadosViewHolder holder, int position) {
        // Obtiene el Pokémon correspondiente a la posición
        PokemonDetails pokemon = pokemonList.get(position);

        // Log para depuración: muestra el nombre y la URL del sprite del Pokémon
        Log.d("CapturadosAdapter", "Pokemon: " + pokemon.getName() + ", Sprite URL: " + pokemon.getSpriteUrl());

        // Vincula el nombre del Pokémon al TextView correspondiente
        holder.binding.pokemonName.setText(pokemon.getName());

        // Utiliza Glide para cargar la imagen del Pokémon desde su URL
        if (pokemon.getSpriteUrl() != null && !pokemon.getSpriteUrl().isEmpty()) {
            holder.binding.pokemonImage.setVisibility(View.VISIBLE);
            Glide.with(holder.binding.pokemonImage.getContext())
                    .load(pokemon.getSpriteUrl())
                    .into(holder.binding.pokemonImage);
        } else {
            // Si no hay imagen, oculta el ImageView
            holder.binding.pokemonImage.setVisibility(View.GONE);
        }

        // Configura el botón de eliminar para que llame al método correspondiente del listener
        holder.binding.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeletePokemon(pokemon);
            }
        });

        // Configura el clic en todo el elemento para que llame al método de clic del listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                PokemonDetails selectedPokemon = pokemonList.get(holder.getAdapterPosition());
                listener.onPokemonClick(selectedPokemon);
            }
        });
    }

    /**
     * Devuelve el número de elementos en la lista.
     *
     * @return Número total de Pokémon en la lista.
     */
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * Clase interna para manejar las vistas de cada elemento del RecyclerView utilizando ViewBinding.
     */
    public static class CapturadosViewHolder extends RecyclerView.ViewHolder {
        // Binding generado automáticamente para el diseño del elemento
        final ItemCapturadoBinding binding;

        /**
         * Constructor que inicializa el binding.
         *
         * @param binding El objeto de binding asociado con el diseño del elemento.
         */
        public CapturadosViewHolder(@NonNull ItemCapturadoBinding binding) {
            super(binding.getRoot()); // Llama al constructor de la superclase con la raíz del binding
            this.binding = binding;
        }
    }
}
