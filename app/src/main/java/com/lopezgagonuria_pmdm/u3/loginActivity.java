package com.lopezgagonuria_pmdm.u3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

/**
 * Activity para manejar el inicio de sesión con Firebase Authentication utilizando FirebaseUI.
 */
public class loginActivity extends AppCompatActivity {

    /**
     * Método que se ejecuta al crear la actividad. Inicia el flujo de inicio de sesión.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicia el flujo de inicio de sesión al crearse la actividad
        startSingin();
    }

    /**
     * Método que inicia el flujo de inicio de sesión utilizando FirebaseUI.
     */
    private void startSingin() {
        // Configuración de los proveedores de inicio de sesión (solo por correo en este caso)
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        // Crea el intent para el inicio de sesión con FirebaseUI
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers) // Configura los proveedores disponibles
                .setLogo(R.drawable.r) // Establece un logo personalizado
                .setTheme(R.style.Base_Theme_U3) // Aplica un tema personalizado
                .setIsSmartLockEnabled(false) // Desactiva Smart Lock para contraseñas
                .build();

        // Lanza el flujo de inicio de sesión
        signInLauncher.launch(signInIntent);
    }

    /**
     * Lanzador para manejar el resultado del inicio de sesión con FirebaseUI.
     */
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                /**
                 * Método llamado cuando se recibe el resultado del inicio de sesión.
                 *
                 * @param result Resultado de la autenticación.
                 */
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    /**
     * Maneja el resultado del inicio de sesión con FirebaseUI.
     *
     * @param result Resultado de la autenticación.
     */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        // Obtiene la respuesta de autenticación
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK) {
            // Inicio de sesión exitoso
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Continúa hacia la actividad principal
            goToMainActivity();
        } else {
            // Falló el inicio de sesión (el usuario canceló o hubo un error)
            Toast.makeText(this, "Error Loguin", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que se ejecuta cuando la actividad pasa al estado de "inicio".
     * Verifica si el usuario ya está autenticado.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si el usuario ya está autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Si el usuario ya inició sesión, va a la actividad principal
            goToMainActivity();
        }
    }

    /**
     * Navega a la actividad principal (MainActivity).
     */
    private void goToMainActivity() {
        // Crea un intent para iniciar la actividad principal
        Intent intent = new Intent(this, MainActivity.class);
        // Añade un extra indicando que se debe mostrar el fragmento de Pokédex
        intent.putExtra("destination", "pokedex");
        // Inicia la actividad principal
        startActivity(intent);
        // Finaliza la actividad de inicio de sesión
        finish();
    }
}


