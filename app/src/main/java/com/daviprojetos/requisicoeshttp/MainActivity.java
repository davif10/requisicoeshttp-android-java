package com.daviprojetos.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button botaoRecuperar;
    private TextView textoResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                String urlApi = "https://blockchain.info/ticker";
                String moeda = "BRL";

                //String urlApi = "https://blockchain.info/tobtc?currency="+moeda+"&value=500";
                String cep = "01310100";
                String urlCep = "https://viacep.com.br/ws/"+cep+"/json/";
                task.execute(urlApi);

            }
        });
    }

    class MyTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream ;
            InputStreamReader inputStreamReader;
            StringBuffer buffer = null;//Variável para montar todos os caracteres em uma string
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                //Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //inputStreamReader lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //Usado para a leitura dos caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";
                while((linha = reader.readLine()) != null ){

                    buffer.append(linha);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException npe){
                System.out.println("Erro: "+npe);
            }

                return buffer.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String objetoValor = null;
            String valorMoeda = null;
            String simbolo = null;
            /*
            String logradouro = null;
            String cep = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;*/
            try {
                JSONObject jsonObject = new JSONObject(s);
                objetoValor = jsonObject.getString("BRL");

                JSONObject jsonObjectReal = new JSONObject(objetoValor);
                valorMoeda = jsonObjectReal.getString("last");
                simbolo = jsonObjectReal.getString("symbol");

                /*JSONObject jsonObject = new JSONObject(s);
                logradouro = jsonObject.getString("logradouro");//Recebe a chave do JSON
                cep = jsonObject.getString("cep");
                bairro = jsonObject.getString("bairro");
                complemento = jsonObject.getString("complemento");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textoResultado.setText(simbolo+" "+valorMoeda);
            //textoResultado.setText(logradouro+" / "+cep+" / "+complemento+" / "+bairro+" / "+localidade+" / "+uf+" .");
        }
    }
}