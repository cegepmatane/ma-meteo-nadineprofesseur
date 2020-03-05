package org.appducegep.mameteo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.*;

public class PageMeteo extends AppCompatActivity {

    private TextView libelleTitre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_meteo);
        System.out.println("onCreate()");
        libelleTitre = (TextView) findViewById(R.id.message);

        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("new Tread()");
                String json = "";
                try {
                    String URL = "https://api.darksky.net/forecast/e1ad16a2b7ba4b82167cb140f8b17640/48.841,-67.497";
                    URL url = new URL(URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        json = stringBuilder.toString();
                    }
                    finally{
                        urlConnection.disconnect();
                    }
                }
                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
                System.out.println(json);
                try {
                    JSONObject obj = new JSONObject(json);
                    String latitude = obj.getString("latitude");
                    System.out.println("Latitude : " + latitude);
                    JSONObject maintenant = obj.getJSONObject("currently");
                    System.out.println("temps actuel : " + maintenant.getString("summary") );

                    String soleilOuNuage = maintenant.getString("summary");
                    String temperature = maintenant.getString("temperature");
                    String temperatureRessentie = maintenant.getString("apparentTemperature");
                    String humidite = maintenant.getString("humidity");
                    String pression = maintenant.getString("pressure");
                    String vent = maintenant.getString("windSpeed");

                    TextView affichageMeteo = (TextView)findViewById(R.id.vueMeteoActuelle);
                    affichageMeteo.setText("");
                    affichageMeteo.append(soleilOuNuage + "\n");
                    affichageMeteo.append("\n\n\n");
                    affichageMeteo.append("Temperature : " + temperature + " (Ressentie : "+temperatureRessentie+")\n");
                    affichageMeteo.append("Humidite : " + humidite + "\n");
                    affichageMeteo.append("Pression : " + pression + "\n");
                    affichageMeteo.append("Vent : " + vent + "\n");


                    JSONArray listeAlertes = obj.getJSONArray("alerts");
                    for(int position = 0; position < listeAlertes.length(); position++)
                    {
                        JSONObject alerte = listeAlertes.getJSONObject(position);
                        String titre = alerte.getString("title");
                        System.out.println("Alerte : " + titre);
                        //String severite = alerte.getString("severite");
                        String url = alerte.getString("uri");
                        String description = alerte.getString("description");
                        //JSONArray regions = alerte.getJSONArray("regions");

                        //affichageMeteo.append("!!!!!! ALERTE !!!!!!");
                        //affichageMeteo.append(titre + "(" + url + ")");
                        affichageMeteo.append(description);

                    }



                    MeteoDAO meteoDAO = new MeteoDAO(getApplicationContext());
                    meteoDAO.ajouterMeteo(soleilOuNuage, vent);

                }catch(JSONException e)
                {

                }

            }
        }).start();

    }
}
