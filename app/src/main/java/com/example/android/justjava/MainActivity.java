/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int numberOfCoffees = 0;
    CheckBox whippid, chocoid;
    EditText textdisplay;
    Toast toast1;
    Toast toast2;
    String priceMessage;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(numberOfCoffees);


        // for displaying the toast message
        Context context = getApplicationContext();
        CharSequence text1 = "The quantity cannot be above 100";
        CharSequence text2 = "Invalid";
        int duration = Toast.LENGTH_SHORT;
        // the variable toast1, toast2 are made global
        toast1 = Toast.makeText(context, text1, duration);
        toast2 = Toast.makeText(context, text2, duration);
        // toast display ends and is being used in increment and decrement methods

        // storing the id of checkbox_whippCream in whippid variable and calling the isChecked() method to check boolean value
        whippid = (CheckBox) findViewById(R.id.checkBox_whippCream);
        // same as above
        chocoid = (CheckBox) findViewById(R.id.checkBox_chocolate);

        // creating variable instance that points to EditText " entertext" where user enters the his/her name 
        textdisplay = (EditText) findViewById(R.id.entertext);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        boolean whippvalue = whippid.isChecked();
        boolean chocovalue = chocoid.isChecked();

        // Editable textdisplay1 = textdisplay.getEditableText(); or
        name = textdisplay.getText().toString();

        // making hardcoding string adaptable to all languages by adding them to strings.xml file
        // example is "thank_you" string

        // comment if you dont want to display this information on main activity screen before redirecting event to gmail
        priceMessage = createOrderSummary(numberOfCoffees, whippvalue, chocovalue, name) + "\n Total : " + "$"
                + calculatePrice(whippvalue,chocovalue) + "\n" + getString(R.string.thank_you);
        Log.v("MainActivity", "the calculated price is" + calculatePrice(whippvalue,chocovalue));
        displayMessage(priceMessage);

        composeEmail("aditya@gmail.com", "Order Summary");
    }

    public String createOrderSummary(int numberOfCoffees, boolean whippvalue, boolean chocovalue, String name) {
        // doing localization. for any changes made here also refer to app>res>strings.xml
        return getString(R.string.order_summary_name, name) + " \n Add whipped cream? " + whippvalue + "\n Add Chocolate?" + chocovalue +
//        return " Name: " + textdisplay1 + " \n Add whipped cream? " + whippvalue + "\n Add Chocolate?" + chocovalue +
                " \n Quantity: " + numberOfCoffees;
    }

    private int calculatePrice(boolean whippvalue, boolean chocovalue) {
        // creating base price as local variable else, the value would keep increasing with number of times the button is clicked
        int basePrice = 5;
        if (whippvalue) {
            basePrice += 1;
        }
        if (chocovalue) {
            basePrice += 2;
        }

        int value = numberOfCoffees * basePrice;
        Log.v("Main_activity", "checking the price after checking buttons:" + value);
        return value;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void increment(View view) {
        if (numberOfCoffees <= 100) {
            numberOfCoffees++;
            display(numberOfCoffees);
        } else {
            toast1.show();
        }

    }

    public void decrement(View view) {
        if (numberOfCoffees > 0) {
            numberOfCoffees++;
            display(numberOfCoffees);
        } else {
            toast2.show();
        }

    }

    /**
     * This method displays the given content on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(message);
    }

    public void composeEmail(String addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for:" + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}