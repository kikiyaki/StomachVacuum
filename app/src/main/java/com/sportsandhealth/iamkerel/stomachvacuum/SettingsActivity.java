package com.sportsandhealth.iamkerel.stomachvacuum;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.EnterPromoCodeActivity;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.MyPromoCodeActivity;
import com.sportsandhealth.iamkerel.stomachvacuum.Promo.PromoCodeUnlock;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.NotificationHelper;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Ставим нужный статус настройками
        TextView notificationStatusText = (TextView) findViewById(R.id.notificationStatus);
        if (NotificationHelper.isSet(this)) {
            notificationStatusText.setText(R.string.switch_on);
            notificationStatusText.setTextColor(getResources().getColor(R.color.basic));
        } else {
            notificationStatusText.setText(R.string.switch_off);
            notificationStatusText.setTextColor(getResources().getColor(R.color.light_grey_blue));
        }

        PromoCodeUnlock promoCodeUnlock = new PromoCodeUnlock(this,
                new PromoCodeUnlock.OnResponseListener() {
                    @Override
                    public void onUnlockTrue() {
                        TextView adFreeStatus = (TextView) findViewById(R.id.adFreeStatus);
                        adFreeStatus.setText(R.string.activated);
                        adFreeStatus.setTextColor(getResources().getColor(R.color.basic));
                    }

                    @Override
                    public void onUnlockFalse() {
                        TextView adFreeStatus = (TextView) findViewById(R.id.adFreeStatus);
                        adFreeStatus.setText(R.string.not_activated);
                        adFreeStatus.setTextColor(getResources().getColor(R.color.light_grey_blue));
                    }

                    @Override
                    public void onError() {
                        TextView adFreeStatus = (TextView) findViewById(R.id.adFreeStatus);
                        adFreeStatus.setText(R.string.not_activated);
                        adFreeStatus.setTextColor(getResources().getColor(R.color.light_grey_blue));
                    }
                });
        if (promoCodeUnlock.isUnlock()) {
            TextView adFreeStatus = (TextView) findViewById(R.id.adFreeStatus);
            adFreeStatus.setText(getResources().getString(R.string.activated));
            adFreeStatus.setTextColor(getResources().getColor(R.color.basic));
        } else {
            promoCodeUnlock.unlock();
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.settings_to_home);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        CardView card0 = (CardView) findViewById(R.id.notificationCard);
        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        CardView card1 = (CardView) findViewById(R.id.adFreeCard);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MyPromoCodeActivity.class);
                startActivity(intent);
            }
        });

        CardView card2 = (CardView) findViewById(R.id.enterPromoCodeCard);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EnterPromoCodeActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(SettingsActivity.this, ProgramSelection.class);
        startActivity(intent);
    }
}
