package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.SettingsActivity;

public class EnterPromoCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_promo_code);

        final PromoCodeSent promoCodeSent = new PromoCodeSent(this, new PromoCodeSent.OnResponseListener() {
            @Override
            public void onError() {
                Toast toast = Toast.makeText(EnterPromoCodeActivity.this, "Error", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(EnterPromoCodeActivity.this, "Success", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        final EditText codeEditText = (EditText) findViewById(R.id.enter_promo_code__edit_text_code);

        Button sendButton = (Button) findViewById(R.id.enter_promo_code__send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeEditText.getText().toString();
                promoCodeSent.send(enteredCode);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.my_promo_code_back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(EnterPromoCodeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}
