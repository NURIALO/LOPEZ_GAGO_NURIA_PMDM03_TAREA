package com.lopezgagonuria_pmdm.u3;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lopezgagonuria_pmdm.u3.databinding.ActivityMainBinding;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    // Variable para View Binding que conecta las vistas de activity_main.xml con esta clase
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método base para configurar la actividad.
        // Aplicar el idioma seleccionado Lo hago mejor desde ScreenSettingsFragment
       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String languageCode = prefs.getString("language", "en"); // "en" como valor predeterminado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());*/
        applyPreferences();
        FirebaseApp.initializeApp(this);
        // Inicializa los servicios de Firebase necesarios para la aplicación.
        // Configura View Binding para inflar el diseño de la actividad de forma segura.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Establece la vista raíz del diseño inflado como la vista principal de la actividad.
        setContentView(binding.getRoot());
        // Configura la Toolbar utilizando la referencia del archivo XML proporcionada por View Binding.
        setSupportActionBar(binding.toolbar);
        // Obtiene el usuario actualmente autenticado con Firebase Authentication.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Si no hay un usuario autenticado, redirige al loginActivity.
            goToLoginActivity();
            return; // Termina la ejecución del método para evitar errores posteriores.
        }
        // Acceso al BottomNavigationView desde View Binding.
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        // Obtiene el NavHostFragment usando View Binding (si está directamente en activity_main.xml).
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(binding.navHostFragment.getId());
        if (navHostFragment == null) {
            // Lanza una excepción si no encuentra el fragmento host.
            throw new IllegalStateException("NavHostFragment no encontrado. Verifica el diseño XML.");
        }
        // Obtiene el controlador de navegación (NavController) del NavHostFragment.
        NavController navController = navHostFragment.getNavController();
        //navController.navigate(R.id.action_nav_ajustes_to_preferenceScreen);
        // Configura AppBarConfiguration para manejar la navegación en los fragmentos principales.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_capturados, R.id.nav_pokedex, R.id.preferenceScreen
        ).build();
        // Vincula la Toolbar con el NavController y AppBarConfiguration.
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        // Vincula el BottomNavigationView con el NavController para sincronizar la navegación.
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    // Método para cerrar sesión en Firebase y redirigir al loginActivity.
    private void logout() {
        AuthUI.getInstance() // Obtiene una instancia del cliente de autenticación.
                .signOut(this) // Cierra sesión para el usuario actual.
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Redirige al usuario al loginActivity después de cerrar sesión.
                        goToLoginActivity();
                    }
                });
    }
    private void applyPreferences() {
        // Leer idioma de las preferencias
        String languageCode = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("language", "en"); // "en" por defecto
        updateLocale(languageCode);
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // Método para redirigir al loginActivity.
    private void goToLoginActivity() {
        Intent intent = new Intent(this, loginActivity.class); // Crea un Intent para iniciar loginActivity.
        startActivity(intent); // Inicia la actividad de login.
        finish(); // Finaliza la actividad actual para evitar que el usuario vuelva.
    }
    @Override
    protected void onDestroy() {
        super.onDestroy(); // Llama al método base para manejar la destrucción de la actividad.
        binding = null; // Establece el objeto de binding en null para evitar fugas de memoria.
    }
}