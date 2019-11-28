package com.led.v12;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class Iluminacao extends AppCompatActivity {

    private Switch btnCoz, btnJardim, btnSala, btnQua, btnSala2, btnPisc;
    //Button btnLed1, btnLed2, btnLed3; //declara variaveis
    //Switch btnVent;

    final Handler handler = new Handler(); // atualiza

    private TextView txtResultado;
    //String comando = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iluminacao);

        handler.postDelayed(atualizaStatus,0);

        btnCoz = (Switch) findViewById(R.id.btnCoz);
        btnJardim = (Switch) findViewById(R.id.btnJardim);
        btnSala = (Switch) findViewById(R.id.btnSala);
        btnQua = (Switch) findViewById(R.id.btnQua);
        btnSala2 = (Switch) findViewById(R.id.btnSala2);

        txtResultado = (TextView)findViewById(R.id.textView);

        //solicita("");

        /*
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            new DownloadWebpageTask().execute("http://192.168.0.108/teste");
        } else {
            txtResultado.setText("no netowrk connection available");
        } */




        btnJardim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("led1");  // acende ou apaga o led
            }
        });

        btnCoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("led2");
            }
        });


        btnSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("led3");
            }
        });

        btnSala2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("led4");
            }
        });

        btnQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("led5");
            }
        });


    }


    private void solicita(String comando){
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = "http://192.168.0.110/" + comando;  //url arduino + manda o comando

        if(networkInfo != null && networkInfo.isConnected()){
            new DownloadWebpageTask().execute(url);
        } else {
            txtResultado.setText("Sem conexão");
        }
    }


    private Runnable atualizaStatus = new Runnable() {  // função atualizar a activity
        @Override
        public void run() {
            solicita("");
            handler.postDelayed(this, 2000);
        }
    };



    private class DownloadWebpageTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            Conexao conexao = new Conexao();

            return conexao.getArduino(urls[0]);
        }


        @Override
        protected void onPostExecute(String result){

            //int img1 = R.drawable.btn7;
            //int img2 = R.drawable.btn8;

            if(result != null){
                //txtResultado.setText(result);  //notifica se a conexão está funcionando

                //muda o texto do botão no app



                if(result.contains("Led 1 - Ligado")){
                    // btnEntrada.setText("Entrada aberta");
                    //btnEntrada1.setImageResource(img2);
                    btnJardim.setChecked(true);
                }

                if(result.contains("Led 1 - Desligado")) {
                    //btnEntrada.setText("Entrada fechada");
                   // btnEntrada1.setImageResource(img1);
                    btnJardim.setChecked(false);
                }
                /*
                if(result.contains("Servo1 - Ligado")){
                    btnQuarto.setText("Quarto aberta");
                }

                if(result.contains("Servo1 - Desligado")) {
                    btnQuarto.setText("Quarto fechada");
                }
                */


            } else {
                //txtResultado.setText("ocorreu um erro");
            }
        }

    }
}