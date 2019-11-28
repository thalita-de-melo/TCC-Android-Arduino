package com.led.v12;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    final Handler handler = new Handler(); // atualiza

    private ImageView btnIlumId, btnPortas, btnVent, btnSeg, imgSolo;

    //private TextView txtSolo;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.bar);
        setSupportActionBar(toolbar);

        handler.postDelayed(atualizaStaus,0);

        //Toolbar tb = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(tb);
        
        //Toolbar toolb = (Toolbar) findViewById(R.id.my_toolbar);

        //toolb.setBackgroundColor(Color.parseColor("#090909"));

        imgSolo = (ImageView) findViewById(R.id.imgSolo);

        btnIlumId = (ImageView) findViewById(R.id.btnIlumId);

        btnPortas = (ImageView) findViewById(R.id.btnPortas);

        btnVent = (ImageView) findViewById(R.id.btnVent);

        btnSeg = (ImageView) findViewById(R.id.btnSeg);

        //btnPref =(ImageView) findViewById(R.id.btnPref);

        //txtSolo = (TextView) findViewById(R.id.txtSolo);

        btnIlumId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Iluminacao.class));
            }
        });

        btnPortas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, preferencias.class));
            }
        });

        btnVent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ventilador.class));
            }
        });

        btnSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, seguranca.class));
            }
        });

        /*
        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, preferencias.class));
            }
        }); */

    }

    // criação da toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.help:
                //Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_LONG).show();  // mostra notificação
                Intent ajuda = new Intent(this, ajuda.class);
                startActivity(ajuda);
                break;
                /*
            case R.id.settings:
                Intent settings = new Intent(this, preferencias.class);
                startActivity(settings);
                break;
            case R.id.update:
                break; */
            default:
                // erro
        }
        return super.onOptionsItemSelected(item);
    }


    private void solicita(String comando) {
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = "http://192.168.0.110/" + comando;  //url arduino + manda o comando de acender ou apagar

        if (networkInfo != null && networkInfo.isConnected()) {
            new MainActivity.DownloadWebpageTask().execute(url);
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

                int img1 = R.drawable.img3;
                int img2 = R.drawable.img2;

/*
                if(result.contains("Solo - Muito humido")){  // exibe notificação
                    //txtSolo.setText("Humidade do Solo: Elevada");
                } */

                if(result.contains("Solo - Humidade adequada")) {
                    //txtSolo.setText("Humidade do Solo: Adequada");
                    imgSolo.setImageResource(img2);
                }
                if(result.contains("Solo - Seco")) {
                    //txtSolo.setText("Humidade do Solo: Baixa");
                    imgSolo.setImageResource(img1);
                }



            } else {
                //txtResultado.setText("ocorreu um erro");
            }
        }
    }

}
