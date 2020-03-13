package com.example.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    Button btnSubmit;
    TextView textServer;
    EditText submitMatr;
    String response;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = findViewById(R.id.btnSubmit);
        submitMatr = findViewById(R.id.submitMatr);
        textServer = findViewById(R.id.textServer);

        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    try {
                        clientTCP();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textServer.setText(response);
            }
        });

    }

    public void clientTCP() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String sentence;

                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    sentence = submitMatr.getText().toString();
                    outToServer.writeBytes(sentence + '\n');
                    response = inFromServer.readLine();

                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();


    }
}
