package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.SettingsActivity;

public class NoPromoCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_promo_code);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.no_promo_code__back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        Button sendButton = (Button) findViewById(R.id.no_promo_code__send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromoCode promoCode = new PromoCode(NoPromoCodeActivity.this);

                if (promoCode.isExist()) {
                    Intent intent = new Intent(NoPromoCodeActivity.this,
                            MyPromoCodeActivity.class);
                    startActivity(intent);
                } else {
                    PromoCodeCreated promoCodeCreated =
                            new PromoCodeCreated(NoPromoCodeActivity.this);
                    promoCodeCreated.create();

                    if (promoCode.isExist()) {
                        Intent intent = new Intent(NoPromoCodeActivity.this,
                                MyPromoCodeActivity.class);
                        startActivity(intent);
                    }
                }
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
        Intent intent = new Intent(NoPromoCodeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}
