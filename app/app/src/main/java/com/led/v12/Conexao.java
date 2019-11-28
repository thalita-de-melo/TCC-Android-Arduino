package com.led.v12;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conexao {

    private String dados = null;

    public String getArduino(String urlSring){
        try {
            URL url = new URL(urlSring);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if(httpURLConnection.getResponseCode() == 200){  // verifica o retorno da url

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); //carcteres pt-br

                StringBuilder stringBuilder = new StringBuilder();

                String linha;

                while ((linha = bufferedReader.readLine()) != null){
                    stringBuilder.append(linha); // junta todas as linhas uma no final da outra
                }

                dados = stringBuilder.toString();

                httpURLConnection.disconnect(); // fecha conexao
            }

        } catch (IOException erro){
            return null;
        }

        return dados;
    }


}

