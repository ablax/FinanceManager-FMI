package me.ablax.financemanager;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import me.ablax.financemanager.databinding.ActivityMainBinding;
import me.ablax.financemanager.db.SQLiteDB;
import me.ablax.financemanager.db.UsersDb;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UsersDb usersDb = new UsersDb(this);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        if (usersDb.hasUser()) {
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
        }

    }


}