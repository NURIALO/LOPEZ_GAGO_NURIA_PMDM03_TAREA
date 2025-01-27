package com.lopezgagonuria_pmdm.u3;

import java.util.List;

/**
 * Clase que representa un Pokémon con información detallada como ID, nombre, tipos, sprite, entre otros.
 */
public class Pokemon {

    // ID único del Pokémon
    private String id;

    // Nombre del Pokémon
    private String name;

    // URL relacionada con el Pokémon (por ejemplo, API o detalles)
    private String url;

    // URL del sprite o imagen del Pokémon
    private String spriteUrl;

    // Lista de tipos del Pokémon (por ejemplo, fuego, agua)
    private List<String> types;

    // Cadena que almacena los tipos en un formato legible
    private static String formattedTypes;

    // Indica si el Pokémon ha sido capturado
    private boolean captured;

    // Peso del Pokémon
    private double weight;

    // Altura del Pokémon
    private double height;

    // Indica si el Pokémon ha sido clicado (para interacciones en la interfaz)
    private boolean isClicked;

    /**
     * Constructor vacío necesario para Firebase.
     */
    public Pokemon() {
        // Constructor vacío.
    }

    /**
     * Constructor completo para inicializar todos los campos.
     *
     * @param id ID del Pokémon.
     * @param name Nombre del Pokémon.
     * @param url URL del Pokémon.
     * @param spriteUrl URL del sprite del Pokémon.
     * @param types Lista de tipos del Pokémon.
     * @param captured Indica si el Pokémon ha sido capturado.
     */
    public Pokemon(String id, String name, String url, String spriteUrl, List<String> types, boolean captured) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.spriteUrl = spriteUrl;
        this.types = types;
        this.captured = captured;
    }

    /**
     * Constructor simplificado para inicializar con información básica.
     *
     * @param name Nombre del Pokémon.
     * @param url URL del Pokémon.
     * @param captured Indica si el Pokémon ha sido capturado.
     */
    public Pokemon(String name, String url, boolean captured) {
        this.name = name;
        this.url = url;
        this.captured = captured;
    }

    /**
     * Obtiene el ID del Pokémon.
     *
     * @return ID del Pokémon.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del Pokémon.
     * Acepta tanto String como Long y lo convierte a String si es necesario.
     *
     * @param id ID del Pokémon (String o Long).
     */
    public void setId(Object id) {
        if (id instanceof Long) {
            this.id = String.valueOf(id);
        } else if (id instanceof String) {
            this.id = (String) id;
        }
    }

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
     * Obtiene la URL del Pokémon.
     *
     * @return URL del Pokémon.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL del Pokémon.
     *
     * @param url URL del Pokémon.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Obtiene la URL del sprite del Pokémon.
     *
     * @return URL del sprite.
     */
    public String getSpriteUrl() {
        return spriteUrl;
    }

    /**
     * Establece la URL del sprite del Pokémon.
     *
     * @param spriteUrl URL del sprite del Pokémon.
     */
    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    /**
     * Obtiene la lista de tipos del Pokémon.
     *
     * @return Lista de tipos del Pokémon.
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Establece los tipos del Pokémon y actualiza el campo "formattedTypes".
     *
     * @param types Lista de tipos del Pokémon.
     */
    public void setTypes(List<String> types) {
        this.types = types;
        setFormattedTypes();
    }

    /**
     * Verifica si el Pokémon ha sido capturado.
     *
     * @return true si está capturado, false en caso contrario.
     */
    public boolean isCaptured() {
        return captured;
    }

    /**
     * Establece si el Pokémon está capturado.
     *
     * @param captured true si está capturado, false en caso contrario.
     */
    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    /**
     * Obtiene el peso del Pokémon.
     *
     * @return Peso del Pokémon.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Establece el peso del Pokémon.
     *
     * @param weight Peso del Pokémon.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Obtiene la altura del Pokémon.
     *
     * @return Altura del Pokémon.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Establece la altura del Pokémon.
     *
     * @param height Altura del Pokémon.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Obtiene los tipos del Pokémon en formato legible.
     *
     * @return Tipos formateados del Pokémon.
     */
    public String getFormattedTypes() {
        return formattedTypes;
    }

    /**
     * Actualiza el campo "formattedTypes" basándose en los tipos.
     */
    public void setFormattedTypes() {
        if (types != null && !types.isEmpty()) {
            this.formattedTypes = String.join(", ", types);
        } else {
            this.formattedTypes = "Desconocido";
        }
    }

    /**
     * Verifica si el Pokémon ha sido clicado.
     *
     * @return true si ha sido clicado, false en caso contrario.
     */
    public boolean isClicked() {
        return isClicked;
    }

    /**
     * Establece si el Pokémon ha sido clicado.
     *
     * @param clicked true si ha sido clicado, false en caso contrario.
     */
    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
