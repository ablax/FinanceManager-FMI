package me.ablax.financemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import me.ablax.financemanager.databinding.FragmentFirstBinding;
import me.ablax.financemanager.db.SQLiteDB;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SQLiteDB db;

    @Override
    public View onCreateView(
            final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState
    ) {
        this.db = new SQLiteDB(this.getContext());

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signInBtn.setOnClickListener(v -> {
            this.db.createUser(binding.nameInputField.getText().toString());
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}