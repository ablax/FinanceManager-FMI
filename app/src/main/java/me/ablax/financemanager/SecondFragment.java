package me.ablax.financemanager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import me.ablax.financemanager.databinding.FragmentSecondBinding;
import me.ablax.financemanager.db.SQLiteDB;
import me.ablax.financemanager.dto.Transaction;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SQLiteDB db;

    @Override
    public View onCreateView(
            final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState
    ) {
        this.db = new SQLiteDB(this.getContext());

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.greetText.setText(binding.greetText.getText().toString().replace("%s", db.getUserName()));
        binding.addTransaction.setOnClickListener(v -> {
            final double amount = Double.parseDouble(binding.price.getText().toString());
            final String transactionName = binding.transName.getText().toString();

            db.addTransaction(new Transaction(transactionName, amount));
            Snackbar.make(view, "Successfully added transaction", Snackbar.LENGTH_LONG).setAction("Transaction", null).show();
            hideKeyboard(getActivity());
            refetchTransactions();
        });
        binding.clearAllBtn.setOnClickListener(v -> {
            Snackbar
                    .make(view, "Confirm delete all?", Snackbar.LENGTH_LONG)
                    .setAction("YES", event -> {
                        db.deleteAllTransactions();
                        refetchTransactions();
                        Snackbar.make(view, "All transactions successfully deleted!.", Snackbar.LENGTH_SHORT).show();
                    }).show();
        });
        refetchTransactions();
    }

    private void refetchTransactions() {
        final List<Transaction> transactions = db.getTransactions();
        final TableLayout tableLayout = binding.tableLayout;
        tableLayout.removeAllViews();
        double totalAmount = 0;
        for (final Transaction transaction : transactions) {
            final TableRow row = new TableRow(getContext());
            final Integer id = transaction.getId();
            row.setId(id);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            final TextView transId = new TextView(getContext());
            final TextView transName = new TextView(getContext());
            final TextView amount = new TextView(getContext());

            transId.setId(700000 + id);
            transName.setId(800000 + id);
            amount.setId(900000 + id);


            transId.setText(id + "");
            transName.setText(transaction.getName());
            final Double transAmount = transaction.getAmount();
            totalAmount += transAmount;
            amount.setText(transAmount.toString());

            transId.setPadding(2, 0, 5, 0);
            transId.setTextColor(Color.BLACK);
            transName.setPadding(2, 0, 15, 0);
            transName.setTextColor(Color.BLACK);
            amount.setPadding(2, 0, 15, 0);
            amount.setTextColor(Color.BLACK);

            final Button deleteTransaction = new Button(getContext());
            deleteTransaction.setText("Delete");
            deleteTransaction.setOnClickListener(v -> {
                this.db.deleteTransaction(id);
                refetchTransactions();
            });
            deleteTransaction.setPadding(2, 0, 15, 0);

            row.addView(transId);
            row.addView(transName);
            row.addView(amount);
            row.addView(deleteTransaction);


            tableLayout.addView(row);
        }

        final TableRow totalAms = new TableRow(getContext());
        final TextView totalTrans = new TextView(getContext());
        final TextView totalAmountView = new TextView(getContext());

        final int id = 600000;
        totalTrans.setId(id);
        final int id1 = 500000;
        totalAmountView.setId(id1);


        totalTrans.setText("Total spent amount");
        totalAmountView.setText(totalAmount + "");

        totalTrans.setPadding(2, 0, 15, 0);
        totalTrans.setTextColor(Color.BLACK);
        totalAmountView.setPadding(2, 0, 15, 0);
        totalAmountView.setTextColor(Color.BLACK);

        totalAms.addView(totalTrans);
        totalAms.addView(totalAmountView);


        tableLayout.addView(totalAms);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}