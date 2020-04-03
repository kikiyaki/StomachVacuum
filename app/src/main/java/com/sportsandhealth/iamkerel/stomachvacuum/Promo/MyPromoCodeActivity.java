package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.SettingsActivity;

public class MyPromoCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_promo_code);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.my_promo_code_back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        String code;
        PromoCode promoCode = new PromoCode(this);
        if (promoCode.isExist()) {
            code = promoCode.code();
        } else {
            code = "none";
        }
        TextView codeTextView = (TextView) findViewById(R.id.my_promo_code);
        codeTextView.setText(code);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Метод для кнопки назад или кнопки выхода на макете
     */
    public void goBack() {
        Intent intent = new Intent(MyPromoCodeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}
