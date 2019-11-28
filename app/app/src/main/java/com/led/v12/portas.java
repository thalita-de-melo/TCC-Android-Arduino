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
import android.widget.ImageView;
import android.widget.TextView;

public class portas extends AppCompatActivity {

    private Button btnEntrada, btnQuarto;

    private ImageView btnEntrada1;

    //TextView txtResultado;

    final Handler handler = new Handler(); // atualiza


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portas1);

        handler.postDelayed(atualizaStatus,0);


        //btnEntrada = (Button) findViewById(R.id.btnEntrada);
        //btnQuarto = (Button) findViewById(R.id.btnQuarto);

        btnEntrada1 = (ImageView) findViewById(R.id.btnEntrada1);

        //txtResultado = (TextView)findViewById(R.id.txtResultado1);

        //solicita("");

        /*

        btnEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("controlS1");
            }
        });  */

        /*btnQuarto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("");
            }
        });

        btnEntrada1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("controlS1");
            }
        });

        */
    }

    private void solicita(String comando){
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = "http://192.168.0.110/" + comando;  //url arduino + manda o comando de acender ou apagar

        if(networkInfo != null && networkInfo.isConnected()){
            new DownloadWebpageTask1().execute(url);
        } else {
            //txtResultado.setText("Sem conexão");
        }
    }

    private Runnable atualizaStatus = new Runnable() {  // função atualizar a activity
        @Override
        public void run() {
            solicita("");
            handler.postDelayed(this, 2000);
        }
    };




    private class DownloadWebpageTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Conexao conexao = new Conexao();

            return conexao.getArduino(urls[0]);
        }


        @Override
        protected void onPostExecute(String result){

            int img1 = R.drawable.btn7;
            int img2 = R.drawable.btn8;

            if(result != null){
                //txtResultado.setText(result);  //notifica se a conexão está funcionando

                //muda o texto do botão no app



                if(result.contains("Servo1 - Ligado")){
                   // btnEntrada.setText("Entrada aberta");
                    btnEntrada1.setImageResource(img2);
                }

                if(result.contains("Servo1 - Desligado")) {
                    //btnEntrada.setText("Entrada fechada");
                    btnEntrada1.setImageResource(img1);
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
