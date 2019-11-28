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

public class ventilador extends AppCompatActivity {

    //private Button btnLigaVenti;
    private ImageView btnLigaVenti;
    //private TextView textView6;

    final Handler handler = new Handler(); // atualiza

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventilador);

        handler.postDelayed(atualizaStaus,0);

        btnLigaVenti = (ImageView) findViewById(R.id.btnLigaVenti);
        //textView6 = (TextView) findViewById(R.id.textView6);

        //solicita("");

        btnLigaVenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("vent1");
                //textView6.setText("oi");
            }
        });
    }

    private void solicita(String comando) {
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = "http://192.168.0.110/" + comando;  //url arduino + manda o comando de acender ou apagar

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(url);
            //textView6.setText("Conexão");
        } else {
            //textView6.setText("Sem conexão");
        }
    }

    private Runnable atualizaStaus = new Runnable() {  // função atualizar a activity
        @Override
        public void run() {
            solicita("");
            handler.postDelayed(this, 2000);
        }
    };


    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Conexao conexao = new Conexao();

            return conexao.getArduino(urls[0]);
        }


        @Override
        protected void onPostExecute(String result){

            //int img1 = R.drawable.btn7;  muda a imagem do botao
            //int img2 = R.drawable.btn8;

            if(result != null){
                //txtResultado.setText(result);  //notifica se a conexão está funcionando

                //muda o texto do botão no app

                int img1 = R.drawable.btn9;
                int img2 = R.drawable.btn10;



                if(result.contains("Vent - Ligado")){
                    // btnEntrada.setText("Entrada aberta");
                    //btnEntrada1.setImageResource(img2);
                    //textView6.setText("ligado");
                    btnLigaVenti.setImageResource(img2);
                }

                if(result.contains("Vent - Desligado")) {
                    //btnEntrada.setText("Entrada fechada");
                    //btnEntrada1.setImageResource(img1);
                    //textView6.setText("desligado");
                    btnLigaVenti.setImageResource(img1);
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


