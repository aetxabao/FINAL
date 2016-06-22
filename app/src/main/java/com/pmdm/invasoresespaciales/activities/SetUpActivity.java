package com.pmdm.invasoresespaciales.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import com.pmdm.invasoresespaciales.R;
import com.pmdm.invasoresespaciales.game.MyApplication;

public class SetUpActivity extends AppCompatActivity {

    MyApplication myApp;
    Switch switchSound;
    RadioButton radioButtons;
    RadioButton radioJoystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        myApp = (MyApplication) getApplication();

        switchSound = (Switch) findViewById(R.id.switchSound);
        radioButtons = (RadioButton) findViewById(R.id.radioButtons);
        radioJoystick = (RadioButton) findViewById(R.id.radioJoystick);
        switchSound.setChecked(myApp.sound);
        switch (myApp.controls){
            case MyApplication.CONTROLS_BUTTONS:
                radioButtons.setChecked(true);
                break;
            case MyApplication.CONTROLS_JOYSTICK:
                radioJoystick.setChecked(true);
                break;
        }
    }

    /**
     * Regresa al activity principal sin modificar ninguna opción
     * @param v la vista
     * @return devuelve false
     */
    public boolean btnCancel(View v) {
        SetUpActivity.this.finish();
        return false;
    }

    /**
     * Regresa al activity principal modificando la configuración
     * @param v la vista
     * @return devuelve false
     */
    public boolean btnDone(View v) {
        myApp.sound = switchSound.isChecked();
        if (radioButtons.isChecked()) myApp.controls = MyApplication.CONTROLS_BUTTONS;
        if (radioJoystick.isChecked()) myApp.controls = MyApplication.CONTROLS_JOYSTICK;
        SetUpActivity.this.finish();
        return false;
    }

}
