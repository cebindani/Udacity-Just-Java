package com.cebin.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity = 1;
    private final double pricePerCup = 5.0;
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText name = (EditText) findViewById(R.id.name_edit_text);
        String personWhoOrdered = name.getText().toString();

        hasWhippedCream = includeTopping(R.id.checkbox_cream);
        hasChocolate = includeTopping(R.id.checkbox_chocolate);
        double price = calculatePrice(hasWhippedCream, hasChocolate);
        String message = createOrderSummary(price, hasWhippedCream, hasChocolate, personWhoOrdered);


        sendToEmail(personWhoOrdered, message);

        displayMessage(message);
        Toast.makeText(MainActivity.this,getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
    }

    private void sendToEmail(String personWhoOrdered, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_title,personWhoOrdered));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    public void increase(View view) {

        if (quantity == 100) {

            Toast.makeText(this, getString(R.string.error_max_orders), Toast.LENGTH_SHORT).show();
            return;
        }

        quantity += 1;
        displayQuantity(quantity);

    }

    public void decrease(View view) {

        if (quantity == 1) {
            Toast.makeText(this,getString(R.string.error_min_order), Toast.LENGTH_SHORT).show();
            return;
        }

        quantity -= 1;
        displayQuantity(quantity);

    }

    /**
     * @param addWhippedCream
     * @param addChocolate
     */
    private double calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        double basePrice = pricePerCup;
        if (addWhippedCream) {
            basePrice = basePrice + 1.0;
        }
        if (addChocolate) {
            basePrice = basePrice + 2.0;
        }

        return quantity * basePrice;
    }

    /**
     * Create summary of the order
     *
     * @param addChocolate     is
     * @param price            of the order
     * @param addWhippedCream  is whether or not the user wants whipped cream topping
     * @param personWhoOrdered
     * @return
     */
    private String createOrderSummary(double price, boolean addWhippedCream, boolean addChocolate, String personWhoOrdered) {

        String message;
        message = getString(R.string.order_summary_name, personWhoOrdered);
        message += "\n" + getString(R.string.order_summary_cream, addWhippedCream);
        message += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        message += "\n" + getString(R.string.order_summary_quantity, quantity);
        message += getString(R.string.order_summary_total,NumberFormat.getCurrencyInstance().format(price)) ;
        message += "\n" + getString(R.string.thank_you);


        return message;


    }

    private boolean includeTopping(int id) {
        CheckBox checkBox = (CheckBox) findViewById(id);
        return checkBox.isChecked();

    }

}
