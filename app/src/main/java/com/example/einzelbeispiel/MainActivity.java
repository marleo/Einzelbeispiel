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
    Button btnCalc;
    TextView textServer;
    EditText submitMatr;
    String response;
    Integer matrNum;
    String output = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = findViewById(R.id.btnSubmit); //assign the View Buttons to variables
        submitMatr = findViewById(R.id.submitMatr);
        textServer = findViewById(R.id.textServer);
        btnCalc = findViewById(R.id.btnCalc);

        btnSubmit.setOnClickListener(new View.OnClickListener(){ // Actions when button is pressed
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

        /* Prüfen, ob	zwei	beliebige	Ziffern existieren die	einen
           gemeinsamen	Teiler	>	1	haben.	Werden zwei	Ziffern
           mit	gemeinsamem Teiler	gefunden,	soll	deren	Index
           ausgegeben	werden */

        btnCalc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                matrNum = Integer.parseInt(submitMatr.getText().toString());
                String temp = matrNum.toString();
                int[] matrDigits = new int[temp.length()];
                output = "";

                for(int i = 0; i < temp.length(); i++){ //get Number for Number into my matrDigits Array
                    matrDigits[i] = Character.getNumericValue(temp.charAt(i)); //.getNumericValue because charAt gives ASCII value
                    System.out.println(matrDigits[i]);
                }

                for(int i = 0; i < matrDigits.length / 2; i++) {
                    for(int j = 0; j < matrDigits.length; j++) {
                        if(i != j) {
                            for(int x = 2; x < 10; x++) {
                                if(matrDigits[i] % x == 0 && matrDigits[j] % x == 0 && matrDigits[i]
                                        != 0 && matrDigits[j] != 0) {
                                    output += "Gefunden an Index:" + i + "(Zahl:" +
                                            matrDigits[i] + ")" + " und " + j  + "(Zahl:" +
                                            matrDigits[j] + ")\n";
                                }
                            }
                        }
                    }
                }
                textServer.setText(output);
            }
        });
    }


    /////////////////////////* Server Connection and Request *//////////////////////////////////////

    public void clientTCP() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sentence;

                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                    DataOutputStream outToServer = new DataOutputStream(
                            clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader
                            (new InputStreamReader(clientSocket.getInputStream()));

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
        thread.join(); // wait for transaction to finish before continue
    }
}
