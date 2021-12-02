package me.ablax.financemanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import me.ablax.financemanager.databinding.ActivityMainBinding;
import me.ablax.financemanager.db.UsersDb;

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