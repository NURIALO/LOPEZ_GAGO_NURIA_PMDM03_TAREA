package com.lopezgagonuria_pmdm.u3;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.firebase.ui.auth.AuthUI;

import java.util.Locale;

/**
 * Fragmento de configuración que permite al usuario personalizar diversas opciones
 * como idioma, permisos de eliminación de Pokémon, y cerrar sesión.
 */
public class ScreenSettingsFragment extends PreferenceFragmentCompat {

    /**
     * Método llamado para inicializar las preferencias desde un recurso XML.
     *
     * @param savedInstanceState Estado guardado del fragmento.
     * @param rootKey Clave raíz de las preferencias.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Log.d("ScreenSettingsFragment", "Fragment instanciado correctamente");
        setPreferencesFromResource(R.xml.preference_screen, rootKey);

        // Configurar la preferencia "about".
        configureAboutPreference();

        // Configurar la preferencia "logout".
        configureLogoutPreference();

        // Configurar las preferencias relacionadas con la eliminación de Pokémon.
        configureDeletePreferences();

        // Configurar la preferencia de idioma.
        configureLanguagePreference();
    }

    /**
     * Configura la preferencia "about" para mostrar información sobre el desarrollador.
     */
    private void configureAboutPreference() {
        Preference aboutPreference = findPreference("about");
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(preference -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.acerca_de)
                        .setMessage(R.string.datos_desarrollador)
                        .setPositiveButton(R.string.aceptar, null)
                        .show();
                return true;
            });
        }
    }

    /**
     * Configura la preferencia "logout" para cerrar sesión y redirigir al login.
     */
    private void configureLogoutPreference() {
        Preference logoutPreference = findPreference("logout");
        if (logoutPreference != null) {
            logoutPreference.setOnPreferenceClickListener(preference -> {
                AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(task -> {
                    Intent intent = new Intent(getActivity(), loginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.d("ScreenSettingsFragment", "Usuario desconectado y redirigido al login");
                });
                return true;
            });
        }
    }

    /**
     * Configura las preferencias relacionadas con la eliminación de Pokémon, incluyendo
     * la habilitación o deshabilitación de la opción de eliminar.
     */
    private void configureDeletePreferences() {
        SwitchPreferenceCompat allowDeletePreference = findPreference("allow_delete");
        Preference deletePokemonOption = findPreference("delete_pokemon_option");

        if (allowDeletePreference != null && deletePokemonOption != null) {
            // Inicialmente, habilitar o deshabilitar la preferencia en función del estado actual.
            deletePokemonOption.setEnabled(allowDeletePreference.isChecked());

            allowDeletePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isAllowed = (Boolean) newValue;
                deletePokemonOption.setEnabled(isAllowed);

                // Mostrar un Toast indicando el estado de la preferencia.
                Context context = getContext(); // Obtener el contexto seguro.
                if (context != null) { // Asegurarse de que el contexto no es nulo.
                    if (isAllowed) {
                        Toast.makeText(context, "Eliminar Pokémon está habilitado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Eliminar Pokémon está deshabilitado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("configureDeletePreferences", "Contexto es nulo, no se puede mostrar el Toast.");
                }

                return true;
            });
        }
    }

    /**
     * Configura la preferencia de idioma, permitiendo al usuario cambiar el idioma de la aplicación.
     */
    private void configureLanguagePreference() {
        ListPreference languagePreference = findPreference("language");
        if (languagePreference != null) {
            // Cargar el idioma actual de las preferencias.
            String currentLanguage = getPreferenceManager().getSharedPreferences().getString("language", "en");
            languagePreference.setValue(currentLanguage); // Establecer el idioma actual.

            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLanguage = newValue.toString();
                updateLocale(selectedLanguage);

                // Mostrar un Toast informando sobre el cambio de idioma.
                if (selectedLanguage.equals("es")) {
                    Toast.makeText(requireContext(), "Idioma cambiado a Español", Toast.LENGTH_SHORT).show();
                } else if (selectedLanguage.equals("en")) {
                    Toast.makeText(requireContext(), "Language changed to English", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Idioma cambiado", Toast.LENGTH_SHORT).show();
                }

                return true;
            });
        }
    }

    /**
     * Actualiza el idioma de la aplicación y recarga la actividad para reflejar el cambio.
     *
     * @param languageCode Código del idioma seleccionado (por ejemplo, "es" para Español, "en" para Inglés).
     */
    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Recargar la actividad para reflejar el cambio.
        requireActivity().recreate();
    }
}