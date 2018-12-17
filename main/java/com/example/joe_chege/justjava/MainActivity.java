package com.example.joe_chege.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joe_chege.justjava.R;

/**
 * The main activity for the JustJava app
 */
public class MainActivity extends AppCompatActivity {
    //STATIC MEMBER VARIABLES

    //TAG for this class
    private final static String TAG = "JUST JAVA MAIN ACTIVITY";

    //price of a single cup of coffee -- prices in KSh.
    private final static int COST_PER_CUP_OF_COFFEE = 50;

    //price of bread crumbs
    private final static int BREAD_CRUMBS_PRICE = 10;

    //price for whipped cream
    private final static int WHIPPED_CREAM_PRICE = 20;

    //price for chocolate topping
    private final static int CHOCOLATE_TOPPING_PRICE = 30;

    //INSTANCE MEMBER VARIABLES

    //Number of cups of coffee to order
    private int noOfCupsOfCoffee;

    //The total cost to charge for the coffee ordered
    private int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText nameEditText = findViewById(R.id.name_edit_text);
        nameEditText.setHint(R.string.name_enquiry_hint_text);
    }

    /**
     * Adds the number of coffee cups to order
     *
     * @param v a View
     */
    public void addCoffee(View v) {
        noOfCupsOfCoffee++;
        if (noOfCupsOfCoffee > 20) {
            noOfCupsOfCoffee--;
            Toast moreThanTwentyCupsToast = Toast.makeText(this, R.string.more_than_20_cups, Toast.LENGTH_LONG);
            moreThanTwentyCupsToast.show();
        } //END IF
        final TextView cupsToOrderTextView = findViewById(R.id.cups_to_order_text_view);
        cupsToOrderTextView.setText("" + noOfCupsOfCoffee);
    }

    /**
     * Reduces the number of cups of coffee to order
     *
     * @param v a View
     */
    public void reduceCoffee(View v) {
        noOfCupsOfCoffee--;
        if (noOfCupsOfCoffee < 0) {
            noOfCupsOfCoffee = 0;
            Toast lessThanZeroCupsToast = Toast.makeText(this, R.string.are_you_serious, Toast.LENGTH_LONG);
            lessThanZeroCupsToast.show();
        } //END IF
        final TextView cupsToOrderTextView = findViewById(R.id.cups_to_order_text_view);
        cupsToOrderTextView.setText("" + noOfCupsOfCoffee);
    }

    /**
     * Displays order summary message on the screen of this app, and on the email app, ready for sending
     *
     * @param v a View
     */
    public void showMessage(View v) {
        int toppingPrice = 0;  //hold topping cost
        final TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        if (noOfCupsOfCoffee <= 0) {
            orderSummaryTextView.setText("");
            Toast zeroCupsOrderedToast = Toast.makeText(this, R.string.please_order, Toast.LENGTH_LONG);
            zeroCupsOrderedToast.show();
            return;
        } //END IF
        String messageToShow = "";
        final CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_check_box);
        final CheckBox chocolateCheckBox = findViewById(R.id.chocolate_check_box);
        final CheckBox breadCrumbsCheckBox = findViewById(R.id.bread_crumbs_check_box);
        final EditText nameEditText = findViewById(R.id.name_edit_text);
        if (breadCrumbsCheckBox.isChecked()) {
            toppingPrice += BREAD_CRUMBS_PRICE;
        } //END IF

        if (whippedCreamCheckBox.isChecked()) {
            toppingPrice += WHIPPED_CREAM_PRICE;
        } //END IF

        if (chocolateCheckBox.isChecked()) {
            toppingPrice += CHOCOLATE_TOPPING_PRICE;
        } //END IF

        price = noOfCupsOfCoffee * (COST_PER_CUP_OF_COFFEE + toppingPrice);
        StringBuilder temporaryMessage = new StringBuilder(messageToShow);
        temporaryMessage.append(getString(R.string.name_label) + nameEditText.getText() + "\n");
        temporaryMessage.append(getString(R.string.add_bread_crumbs_label_summary) + (breadCrumbsCheckBox.isChecked() ? getString(R.string.agree) : getString(R.string.disagree)) + "\n");
        temporaryMessage.append(getString(R.string.whipped_cream_summary) + (whippedCreamCheckBox.isChecked() ? getString(R.string.agree) : getString(R.string.disagree)) + "\n");
        temporaryMessage.append(getString(R.string.add_chocolate_summary) + (chocolateCheckBox.isChecked() ? getString(R.string.agree) : getString(R.string.disagree)) + "\n");
        temporaryMessage.append(getString(R.string.quantity_summary) + noOfCupsOfCoffee + "\n");
        temporaryMessage.append(getString(R.string.total_label_summary) + price + "\n");
        temporaryMessage.append(getString(R.string.thank_you_summary));
        messageToShow = temporaryMessage.toString();
        orderSummaryTextView.setText(messageToShow); //TODO instead of showing message, send an email, instead.

        Intent orderSummaryMailIntent = new Intent(Intent.ACTION_SENDTO);
        orderSummaryMailIntent.setData(Uri.parse("mailto:"));
        orderSummaryMailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_subject_email) + nameEditText.getText());
        orderSummaryMailIntent.putExtra(Intent.EXTRA_TEXT, messageToShow);

        if (orderSummaryMailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(orderSummaryMailIntent);
        } //END IF
    }

    /**
     * Saves the state of the app when the Activity is being killed
     *
     * @param appAtThisTime the app state to save
     */
    @Override
    protected void onSaveInstanceState(Bundle appAtThisTime) {
        final EditText nameEditText = findViewById(R.id.name_edit_text);
        String nameOfOrderees = nameEditText.getText().toString();
        final CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_check_box);
        final CheckBox chocolateCheckBox = findViewById(R.id.chocolate_check_box);
        final CheckBox breadCrumbsCheckBox = findViewById(R.id.bread_crumbs_check_box);
        final TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);

        appAtThisTime.putInt("noOfCupsOfCoffee", noOfCupsOfCoffee);
        appAtThisTime.putInt("price", price);
        appAtThisTime.putString("nameEditText", nameOfOrderees);
        appAtThisTime.putBoolean("whippedCreamCheckBox", whippedCreamCheckBox.isChecked());
        appAtThisTime.putBoolean("chocolateCheckBox", chocolateCheckBox.isChecked());
        appAtThisTime.putBoolean("breadCrumbsCheckBox", breadCrumbsCheckBox.isChecked());
        appAtThisTime.putString("orderSummaryTextView", orderSummaryTextView.getText().toString());
        super.onSaveInstanceState(appAtThisTime);
    }


    /**
     * Restore the app state when activity is restarted
     *
     * @param appAtThisTime app state to restore
     */
    protected void onRestoreInstanceState(Bundle appAtThisTime) {
        super.onRestoreInstanceState(appAtThisTime);

        final EditText nameEditText = findViewById(R.id.name_edit_text);
        final CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_check_box);
        final CheckBox chocolateCheckBox = findViewById(R.id.chocolate_check_box);
        final CheckBox breadCrumbsCheckBox = findViewById(R.id.bread_crumbs_check_box);
        final TextView cupsToOrderTextView = findViewById(R.id.cups_to_order_text_view);
        final TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);

        noOfCupsOfCoffee = appAtThisTime.getInt("noOfCupsOfCoffee");
        price = appAtThisTime.getInt("price");

        nameEditText.setText(appAtThisTime.getString("nameEditText"));
        whippedCreamCheckBox.setChecked(appAtThisTime.getBoolean("whippedCreamCheckBox"));
        chocolateCheckBox.setChecked(appAtThisTime.getBoolean("chocolateCheckBox"));
        breadCrumbsCheckBox.setChecked(appAtThisTime.getBoolean("breadCrumbsCheckBox"));
        cupsToOrderTextView.setText("" + noOfCupsOfCoffee);
        orderSummaryTextView.setText(appAtThisTime.getString("orderSummaryTextView"));
    }

    /**
     * Check or de-checks breadcrumbs checkbox when its containing linear layout is tapped
     *
     * @param v a View
     */
    public void checkDeCheckBreadCrumbs(View v) {
        final CheckBox breadCrumbsCheckBox = findViewById(R.id.bread_crumbs_check_box);

        if (breadCrumbsCheckBox.isChecked())
            breadCrumbsCheckBox.setChecked(false);
        else
            breadCrumbsCheckBox.setChecked(true);
        //END IF-ELSE
    }
}
