package com.lopezgagonuria_pmdm.u3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.lopezgagonuria_pmdm.u3.databinding.ItemPokemonBinding;
import java.util.List;

/**
 * Adaptador para mostrar una lista de Pokémon en un RecyclerView.
 * Proporciona funcionalidad para manejar eventos de clic, visualización de información
 * y actualización de estado en Firestore.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    // Lista de Pokémon a mostrar.
    private final List<PokemonDetails> pokemonList;

    // Listener para manejar eventos de clic en los elementos.
    private final OnPokemonClickListener listener;

    // Referencia a la colección "capturados" en Firestore.
    private final CollectionReference capturadosRef;

    /**
     * Interfaz para manejar clics en los elementos del RecyclerView.
     */
    public interface OnPokemonClickListener {
        /**
         * Método llamado al hacer clic en un Pokémon.
         *
         * @param pokemon Pokémon seleccionado.
         */
        void onPokemonClick(PokemonDetails pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param pokemonList Lista de Pokémon a mostrar.
     * @param listener Listener para manejar clics en los elementos.
     * @param capturadosRef Referencia a la colección "capturados" en Firestore.
     */
    public PokemonAdapter(List<PokemonDetails> pokemonList, OnPokemonClickListener listener, CollectionReference capturadosRef) {
        this.pokemonList = pokemonList;
        this.listener = listener;
        this.capturadosRef = capturadosRef;
    }

    /**
     * Método que infla el diseño de cada elemento del RecyclerView.
     *
     * @param parent ViewGroup padre.
     * @param viewType Tipo de vista (no usado aquí).
     * @return Una instancia de PokemonViewHolder.
     */
    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos View Binding para inflar el layout
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PokemonViewHolder(binding);
    }

    /**
     * Método que vincula los datos de un Pokémon a una vista del RecyclerView.
     *
     * @param holder ViewHolder que contiene la vista.
     * @param position Posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonDetails pokemon = pokemonList.get(position);

        // Configurar el nombre del Pokémon.
        holder.binding.pokemonName.setText(pokemon.getName());

        // Cargar el sprite del Pokémon usando Glide.
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getSpriteUrl())
                .into(holder.binding.pokemonSprite);

        // Configurar los iconos de tipo.
        List<PokemonDetails.Type> types = pokemon.getTypes();
        if (types != null && !types.isEmpty()) {
            // Configuración del primer tipo.
            String typeName1 = types.get(0).getType().getName();
            int typeImageResId1 = getTypeImageResId(typeName1);
            holder.binding.typeIcon1.setImageResource(typeImageResId1);
            holder.binding.typeIcon1.setVisibility(View.VISIBLE);

            if (types.size() > 1) {
                // Configuración del segundo tipo (si existe).
                String typeName2 = types.get(1).getType().getName();
                int typeImageResId2 = getTypeImageResId(typeName2);
                holder.binding.typeIcon2.setImageResource(typeImageResId2);
                holder.binding.typeIcon2.setVisibility(View.VISIBLE);
            } else {
                holder.binding.typeIcon2.setVisibility(View.GONE);
            }
        } else {
            holder.binding.typeIcon1.setVisibility(View.GONE);
            holder.binding.typeIcon2.setVisibility(View.GONE);
        }

        // Configurar el evento de hover para mostrar los tipos del Pokémon.
        holder.itemView.setOnHoverListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                StringBuilder message = new StringBuilder("Tipo(s): ");
                if (types != null && !types.isEmpty()) {
                    for (PokemonDetails.Type type : types) {
                        message.append(type.getType().getName()).append(" ");
                    }
                } else {
                    message.append("Desconocido");
                }
                Toast.makeText(holder.itemView.getContext(), message.toString().trim(), Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        // Cambiar la apariencia si el Pokémon está capturado.
        if (pokemon.isCaptured()) {
            holder.itemView.setEnabled(false);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.RED));
        } else {
            holder.itemView.setEnabled(true);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow_cardview));
        }

        // Configurar el clic en el elemento.
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && !pokemon.isCaptured()) {
                listener.onPokemonClick(pokemon);
                pokemon.setCaptured(true);
                notifyItemChanged(position);
                capturadosRef.document(pokemon.getId())
                        .set(pokemon)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "¡Pokémon capturado guardado con éxito!"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar el Pokémon", e));
            }
        });
    }

    /**
     * Devuelve el número de elementos en la lista.
     *
     * @return Número de Pokémon en la lista.
     */
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * ViewHolder para representar las vistas de cada Pokémon.
     */
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {

        // Binding para acceder a las vistas del diseño.
        final ItemPokemonBinding binding;

        /**
         * Constructor del ViewHolder.
         *
         * @param binding Binding del diseño.
         */
        public PokemonViewHolder(@NonNull ItemPokemonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Obtiene el recurso drawable correspondiente al nombre del tipo del Pokémon.
     *
     * @param typeName Nombre del tipo (por ejemplo, "fire", "water").
     * @return ID del recurso drawable correspondiente al tipo.
     */
    private int getTypeImageResId(String typeName) {
        switch (typeName.toLowerCase()) {
            case "bug":
                return R.drawable.bugs;
            case "flying":
                return R.drawable.flyings;
            case "fire":
                return R.drawable.fires;
            case "water":
                return R.drawable.waters;
            case "grass":
                return R.drawable.grasss;
            case "electric":
                return R.drawable.electrics;
            case "psychic":
                return R.drawable.psychics;
            case "ice":
                return R.drawable.ices;
            case "fairy":
                return R.drawable.fairys;
            case "normal":
                return R.drawable.normals;
            case "fighting":
                return R.drawable.fightings;
            case "poison":
                return R.drawable.poisons;
            case "ground":
                return R.drawable.grounds;
            case "rock":
                return R.drawable.rocks;
            case "ghost":
                return R.drawable.ghosts;
            case "steel":
                return R.drawable.steels;
            default:
                return R.drawable.free1s; // Imagen por defecto para tipos desconocidos.
        }
    }
}
