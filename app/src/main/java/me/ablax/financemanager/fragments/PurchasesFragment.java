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
import me.ablax.financemanager.databinding.PurchasesFragmentBinding;
import me.ablax.financemanager.db.PurchasesDb;
import me.ablax.financemanager.db.TransactionsDb;
import me.ablax.financemanager.dto.PurchaseDto;
import me.ablax.financemanager.dto.Transaction;

public class PurchasesFragment extends Fragment {

    private PurchasesFragmentBinding binding;
    private TransactionsDb transactionsDb;
    private PurchasesDb purchasesDb;
    private int transactionId;

    public static void hideKeyboard(final Activity activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final Context context = this.getContext();
        this.transactionsDb = new TransactionsDb(context);
        this.purchasesDb = new PurchasesDb(context);

        this.binding = PurchasesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.transactionId = getArguments().getInt("transactionId");
        final Transaction transaction = this.transactionsDb.getTransaction(transactionId);
        this.binding.greetText.setText(binding.greetText.getText().toString().replace("%s", transaction.getName()));
        this.binding.addProduct.setOnClickListener(v -> {
            final Integer amount = Integer.parseInt(binding.quantity.getText().toString());
            final String transactionName = binding.productName.getText().toString();

            purchasesDb.addPurchase(new PurchaseDto(transactionId, transactionName, amount));
            Snackbar.make(view, "Successfully added product", Snackbar.LENGTH_LONG).setAction("Prouct", null).show();
            binding.quantity.setText("");
            binding.productName.setText("");
            hideKeyboard(this.requireActivity());
            refetchProducts();
        });
        this.binding.clearAllBtn.setOnClickListener(v -> {
            Snackbar
                    .make(view, "Confirm delete all?", Snackbar.LENGTH_LONG)
                    .setAction("YES", event -> {
                        purchasesDb.deleteAllPurchaseForTransaction(transactionId);
                        refetchProducts();
                        Snackbar.make(view, "All products successfully deleted!.", Snackbar.LENGTH_SHORT).show();
                    }).show();
        });
        refetchProducts();
    }

    private void refetchProducts() {
        final List<PurchaseDto> transactions = purchasesDb.getPurchaseForTransaction(transactionId);
        final TableLayout tableLayout = binding.tableLayout;
        tableLayout.removeAllViews();
        int totalAmount = 0;
        for (final PurchaseDto purchaseDto : transactions) {
            final TableRow row = new TableRow(getContext());

            final Integer id = purchaseDto.getId();
            row.setId(id);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

            final Integer transAmount = purchaseDto.getQuantity();
            totalAmount += transAmount;

            Arrays.asList(
                    getTextField(900000 + id, transAmount::toString),
                    getTextField(800000 + id, purchaseDto::getName),
                    getDeleteButton(id)
            ).forEach(row::addView);

            tableLayout.addView(row);
        }

        final TextView totalTrans = getTextField(600000, R.string.total_products_for_transaction);
        final int finalTotalAmount = totalAmount;
        final TextView totalAmountView = getTextField(500000, () -> Integer.valueOf(finalTotalAmount).toString());

        final TableRow totalAms = new TableRow(getContext());
        totalAms.addView(totalTrans);
        totalAms.addView(totalAmountView);

        tableLayout.addView(totalAms);

    }

    @NonNull
    private Button getDeleteButton(final Integer id) {
        final Button deleteTransaction = new Button(getContext());
        deleteTransaction.setText(R.string.delete_btn);
        deleteTransaction.setOnClickListener(v -> {
            this.purchasesDb.deletePurchase(transactionId, id);
            refetchProducts();
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

    private TextView getTextField(final int idIndex, final int id) {
        final TextView view = new TextView(getContext());
        view.setId(idIndex);
        view.setText(id);
        view.setPadding(2, 0, 5, 0);
        view.setTextColor(Color.BLACK);
        return view;
    }

}