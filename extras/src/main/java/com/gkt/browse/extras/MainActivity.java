package com.gkt.browse.extras;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (bluetoothAdapter.isEnabled())
        {

            Toast.makeText(getApplicationContext(),"bluetooth enabled default",Toast.LENGTH_LONG).show();

        }

        else
        {
            Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);

            if (bluetoothAdapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(),"bluetooth enabled ",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
