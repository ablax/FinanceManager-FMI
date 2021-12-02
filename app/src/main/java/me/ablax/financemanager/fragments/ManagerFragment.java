package me.ablax.financemanager.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import me.ablax.financemanager.R;
import me.ablax.financemanager.databinding.FragmentSecondBinding;
import me.ablax.financemanager.db.SQLiteDB;
import me.ablax.financemanager.db.UsersDb;
import me.ablax.financemanager.dto.Transaction;

public class ManagerFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SQLiteDB transactionsDb;
    private UsersDb usersDb;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final Context context = this.getContext();
        this.transactionsDb = new SQLiteDB(context);
        this.usersDb = new UsersDb(context);

        this.binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.binding.greetText.setText(binding.greetText.getText().toString().replace("%s", this.usersDb.getUserName()));
        this.binding.addTransaction.setOnClickListener(v -> {
            final double amount = Double.parseDouble(binding.price.getText().toString());
            final String transactionName = binding.transName.getText().toString();

            transactionsDb.addTransaction(new Transaction(transactionName, amount));
            Snackbar.make(view, "Successfully added transaction", Snackbar.LENGTH_LONG).setAction("Transaction", null).show();
            binding.price.setText("");
            binding.transName.setText("");
            hideKeyboard(this.requireActivity());
            refetchTransactions();
        });
        this.binding.clearAllBtn.setOnClickListener(v -> {
            Snackbar
                    .make(view, "Confirm delete all?", Snackbar.LENGTH_LONG)
                    .setAction("YES", event -> {
                        transactionsDb.deleteAllTransactions();
                        refetchTransactions();
                        Snackbar.make(view, "All transactions successfully deleted!.", Snackbar.LENGTH_SHORT).show();
                    }).show();
        });
        refetchTransactions();
    }

    private void refetchTransactions() {
        final List<Transaction> transactions = transactionsDb.getTransactions();
        final TableLayout tableLayout = binding.tableLayout;
        tableLayout.removeAllViews();
        double totalAmount = 0;
        for (final Transaction transaction : transactions) {
            final TableRow row = new TableRow(getContext());
            final Integer id = transaction.getId();
            row.setId(id);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            final Double transAmount = transaction.getAmount();
            totalAmount += transAmount;

            Arrays.asList(
                    getTextField(700000 + id, id::toString),
                    getTextField(800000 + id, transaction::getName),
                    getTextField(900000 + id, transAmount::toString),
                    getDeleteButton(id)
            ).forEach(row::addView);

            tableLayout.addView(row);
        }

        final TableRow totalAms = new TableRow(getContext());
        final TextView totalTrans = new TextView(getContext());
        final TextView totalAmountView = new TextView(getContext());

        final int id = 600000;
        totalTrans.setId(id);
        final int id1 = 500000;
        totalAmountView.setId(id1);

        totalTrans.setText(R.string.total_spent_amount);
        totalAmountView.setText(Double.valueOf(totalAmount).toString());

        totalTrans.setPadding(2, 0, 15, 0);
        totalTrans.setTextColor(Color.BLACK);
        totalAmountView.setPadding(2, 0, 15, 0);
        totalAmountView.setTextColor(Color.BLACK);

        totalAms.addView(totalTrans);
        totalAms.addView(totalAmountView);


        tableLayout.addView(totalAms);

    }

    @NonNull
    private Button getDeleteButton(final Integer id) {
        final Button deleteTransaction = new Button(getContext());
        deleteTransaction.setText(R.string.delete_btn);
        deleteTransaction.setOnClickListener(v -> {
            this.transactionsDb.deleteTransaction(id);
            refetchTransactions();
        });
        deleteTransaction.setPadding(2, 0, 15, 0);
        return deleteTransaction;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }

    private TextView getTextField(final int idIndex, final Supplier<String> text) {
        final TextView view = new TextView(getContext());
        view.setId(idIndex);
        view.setText(text.get());
        view.setPadding(2, 0, 5, 0);
        view.setTextColor(Color.BLACK);
        return view;
    }

    public static void hideKeyboard(final Activity activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}