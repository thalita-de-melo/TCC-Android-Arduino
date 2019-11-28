package com.led.v12;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class preferencias extends AppCompatActivity {

    final Handler handler = new Handler(); // atualiza

    private Switch btnInc, btnChuva;

    private int chuva = 0, fogo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        btnInc = (Switch) findViewById(R.id.btnInc);
        btnChuva = (Switch) findViewById(R.id.btnChuva);

        btnInc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  // verifica se o switch esta selecionado
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    fogo = 1;
                } else {
                    // The toggle is disabled
                    fogo = 0;
                }
            }
        });

        btnChuva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  // verifica se o switch esta selecionado
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    chuva = 1;
                } else {
                    // The toggle is disabled
                    chuva = 0;
                }
            }
        });


        btnChuva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("controlChuva");
            }
        });

        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicita("controlFogo");
            }
        });

        handler.postDelayed(atualizaStatus,0);
    }


    private void criarNotificacaoSimples(){
        int id = 1;
        String titulo = "Fumaça detectada";  // titulo da notificação
        String texto = "Notificação";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent p = getPendingIntent(id, intent, this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    private void criarNotificacaoChuva(){
        int id = 1;
        String titulo = "Chovendo";  // titulo da notificação
        String texto = "Começou a chover";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent p = getPendingIntent(id, intent, this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }


    private PendingIntent getPendingIntent(int id, Intent intent, Context context){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }



    private void solicita(String comando) {
        ConnectivityManager connMgr = (ConnectivityManager)  //pega informações da rede e verifica se tem conexão
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String url = "http://192.168.0.110/" + comando;  //url arduino + manda o comando de acender ou apagar

        if (networkInfo != null && networkInfo.isConnected()) {
            new preferencias.DownloadWebpageTask().execute(url);
            //textView6.setText("Conexão");
        } else {
            //textView6.setText("Sem conexão");
        }
    }

    private Runnable atualizaStatus = new Runnable() {  // função atualizar a activity
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



                if(result.contains("Incendio - Fumaça detectada") && fogo == 1){  // exibe notificação
                    // btnEntrada.setText("Entrada aberta");
                    //btnEntrada1.setImageResource(img2);
                    //textView7.setText("ligado");
                    criarNotificacaoSimples();
                }

                if(result.contains("Incendio - Nada detectado")) {
                    //btnEntrada.setText("Entrada fechada");
                    //btnEntrada1.setImageResource(img1);
                    //textView7.setText("desligado");

                }

                if(result.contains("Chuva - Chovendo") && chuva == 1){     // exibe notificação
                    // btnEntrada.setText("Entrada aberta");
                    //btnEntrada1.setImageResource(img2);
                    //textView7.setText("ligado");
                    criarNotificacaoChuva();
                }

                if(result.contains("Chuva - Sem chuva")) {
                    //btnEntrada.setText("Entrada fechada");
                    //btnEntrada1.setImageResource(img1);
                    //textView7.setText("desligado");

                }

                if(result.contains("Chuva - Ligado")){
                    btnChuva.setChecked(true);
                }

                if(result.contains("Chuva - Desligado")) {
                    btnChuva.setChecked(false);
                }

                if(result.contains("Fogo - Ligado")){
                    btnInc.setChecked(true);
                }

                if(result.contains("Fogo - Desligado")) {
                    btnInc.setChecked(false);
                }


            } else {
                //txtResultado.setText("ocorreu um erro");
            }
        }
    }
}
