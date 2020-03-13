package com.example.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;


public class MainActivity extends AppCompatActivity {

    Button btnSubmit;
    TextView textServer;
    EditText submitMatr;
    String response;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        submitMatr = (EditText) findViewById(R.id.submitMatr);
        textServer = (TextView) findViewById(R.id.textServer);

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
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String sentence;

                    System.out.println("Started");
                    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("One more");
                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                    System.out.println("Connected to Server");

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    sentence = submitMatr.getText().toString();

                    System.out.println("Your Matr. Nr is: " + sentence);

                    outToServer.writeBytes(sentence + '\n');

                    response = inFromServer.readLine();

                    System.out.println("Got a response: " + response);

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
