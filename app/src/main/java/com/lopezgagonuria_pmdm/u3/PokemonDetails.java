package com.lopezgagonuria_pmdm.u3;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que representa los detalles de un Pokémon, incluyendo su información básica,
 * tipos, estado de captura y otros atributos relacionados.
 */
public class PokemonDetails implements Serializable {

    // ID único del Pokémon.
    private String id;

    // Nombre del Pokémon.
    private String name;

    // Peso del Pokémon.
    private double weight;

    // Altura del Pokémon.
    private double height;

    // URL del sprite del Pokémon.
    private String spriteUrl;

    // URL relacionada con la API del Pokémon.
    private String url;

    // Lista de tipos del Pokémon.
    private List<Type> types;

    // Indica si el Pokémon ha sido capturado.
    private boolean captured;

    // Indica si el Pokémon ha sido clicado en la interfaz.
    private boolean clicked;

    // Tipos formateados como cadena.
    private String formattedTypes;

    // Indica si el Pokémon tiene todos sus datos completamente cargados.
    private boolean isFullyLoaded;

    /**
     * Constructor vacío requerido por Firebase.
     */
    public PokemonDetails() {
        // Constructor vacío.
    }

    /**
     * Constructor completo para inicializar los detalles del Pokémon.
     *
     * @param id ID único del Pokémon.
     * @param name Nombre del Pokémon.
     * @param weight Peso del Pokémon.
     * @param height Altura del Pokémon.
     * @param spriteUrl URL del sprite del Pokémon.
     * @param url URL relacionada con la API del Pokémon.
     * @param types Lista de tipos del Pokémon.
     * @param captured Estado de captura del Pokémon.
     * @param clicked Estado de clicado en la interfaz.
     */
    public PokemonDetails(String id, String name, double weight, double height, String spriteUrl, String url,
                          List<Type> types, boolean captured, boolean clicked) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.spriteUrl = spriteUrl;
        this.url = url;
        this.types = types;
        this.captured = captured;
        this.clicked = clicked;
        this.isFullyLoaded = false;
    }

    /**
     * Verifica si el Pokémon tiene todos sus datos cargados completamente.
     *
     * @return true si está completamente cargado, false en caso contrario.
     */
    public boolean isFullyLoaded() {
        return isFullyLoaded;
    }

    /**
     * Establece si el Pokémon está completamente cargado.
     *
     * @param fullyLoaded Estado de carga completa.
     */
    public void setFullyLoaded(boolean fullyLoaded) {
        isFullyLoaded = fullyLoaded;
    }

    /**
     * Obtiene los tipos formateados como cadena.
     *
     * @return Tipos formateados.
     */
    public String getFormattedTypes() {
        if (formattedTypes == null) {
            setFormattedTypes(); // Genera `formattedTypes` si está vacío.
        }
        return formattedTypes;
    }

    /**
     * Genera la cadena formateada de los tipos del Pokémon.
     */
    private void setFormattedTypes() {
        if (types != null && !types.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Type type : types) {
                if (type != null && type.getType() != null && type.getType().getName() != null) {
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(type.getType().getName());
                }
            }
            formattedTypes = builder.toString();
        } else {
            formattedTypes = "Desconocido";
        }
    }

    /**
     * Clase interna que representa los tipos del Pokémon.
     */
    public static class Type {

        // Número de slot del tipo.
        private int slot;

        // Objeto que contiene el nombre y la URL del tipo.
        private NestedType type;

        /**
         * Constructor vacío requerido por Firebase.
         */
        public Type() {
            // Constructor vacío.
        }

        /**
         * Constructor para inicializar el tipo.
         *
         * @param slot Número de slot del tipo.
         * @param type Objeto que contiene el nombre y URL del tipo.
         */
        public Type(int slot, NestedType type) {
            this.slot = slot;
            this.type = type;
        }

        /**
         * Obtiene el número de slot del tipo.
         *
         * @return Número de slot.
         */
        public int getSlot() {
            return slot;
        }

        /**
         * Establece el número de slot del tipo.
         *
         * @param slot Número de slot.
         */
        public void setSlot(int slot) {
            this.slot = slot;
        }

        /**
         * Obtiene el objeto que contiene el nombre y la URL del tipo.
         *
         * @return Objeto NestedType.
         */
        public NestedType getType() {
            return type;
        }

        /**
         * Establece el objeto NestedType.
         *
         * @param type Objeto NestedType.
         */
        public void setType(NestedType type) {
            this.type = type;
        }
    }

    /**
     * Clase interna para manejar el nombre y URL dentro del tipo.
     */
    public static class NestedType {

        // Nombre del tipo.
        private String name;

        // URL del tipo.
        private String url;

        /**
         * Constructor vacío requerido por Firebase.
         */
        public NestedType() {
            // Constructor vacío.
        }

        /**
         * Constructor para inicializar el NestedType.
         *
         * @param name Nombre del tipo.
         * @param url URL del tipo.
         */
        public NestedType(String name, String url) {
            this.name = name;
            this.url = url;
        }

        /**
         * Obtiene el nombre del tipo.
         *
         * @return Nombre del tipo.
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre del tipo.
         *
         * @param name Nombre del tipo.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtiene la URL del tipo.
         *
         * @return URL del tipo.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Establece la URL del tipo.
         *
         * @param url URL del tipo.
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }

    // Getters y setters de los atributos principales.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Obtiene una lista de nombres de tipos del Pokémon.
     *
     * @return Lista de nombres de tipos.
     */
    public List<String> getTypeNames() {
        if (types != null) {
            return types.stream()
                    .map(type -> type.getType() != null ? type.getType().getName() : null)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Genera la URL del sprite del Pokémon basándose en su ID.
     */
    public void generateSpriteUrl() {
        if (this.id != null && !this.id.isEmpty()) {
            this.spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + this.id + ".png";
        } else {
            this.spriteUrl = ""; // Asignar una cadena vacía si no hay ID.
        }
    }
}