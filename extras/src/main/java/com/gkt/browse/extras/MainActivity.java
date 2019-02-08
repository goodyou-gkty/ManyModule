package com.gkt.browse.extras;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    private Button enable;
    private Button disable;
    private Button discover;
    private Button paired;
    private ArrayList<String>devices;
    private ListView listView;
    private ArrayAdapter<String>arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enable = (Button)findViewById(R.id.enable);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });


        disable = (Button)findViewById(R.id.disable);

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(),"bluetooth Disabled",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"bluetooth already disabled",Toast.LENGTH_LONG).show();
                }
            }
        });

         discover = (Button)findViewById(R.id.discover);

         discover.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                 startActivity(intent);
             }
         });

         devices = new ArrayList<>();
         listView = (ListView) findViewById(R.id.pairedList);
         paired = (Button)findViewById(R.id.paired);

         paired.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();

                 for(BluetoothDevice bluetoothDevice : set)
                 {
                     devices.add(bluetoothDevice.getName());
                 }

                 arrayAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,devices);
                 listView.setAdapter(arrayAdapter);
             }
         });


    }
}
