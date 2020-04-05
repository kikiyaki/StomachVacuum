package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        final String code;
        final PromoCode promoCode = new PromoCode(this);
        if (promoCode.isExist()) {
            code = promoCode.code();

            TextView codeTextView = (TextView) findViewById(R.id.my_promo_code);
            codeTextView.setText(code);

            Button shareButton = (Button) findViewById(R.id.my_promo_code__share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getResources().getString(R.string.my_promo_code__share_text)
                                    + code);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            });
        } else {
            Intent intent = new Intent(MyPromoCodeActivity.this,
                    NoPromoCodeActivity.class);
            startActivity(intent);
        }
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
