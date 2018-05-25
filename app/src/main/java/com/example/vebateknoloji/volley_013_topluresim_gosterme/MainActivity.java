package com.example.vebateknoloji.volley_013_topluresim_gosterme;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    StringBuilder b;
    String URL = "http://www.restoranduragi.com/webservisim/resimleri_sirala.php";
    //String[] resimler;
    List<resim> listt = null;
    TextView txt;
    ImageView resimgoster;
    LinearLayout linear;
    int yardir = 0;
    ImageView i2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listt = new ArrayList<resim>();
        //txt = (TextView)findViewById(R.id.txt_veri);
       // resimgoster = (ImageView)findViewById(R.id.resimgoster);
        linear = (LinearLayout)findViewById(R.id.linear);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int count = 0;
                while(count < response.length()) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(count);

                        resim resim = new resim();
                        resim.setNumara(jsonObject.getInt("no"));
                        resim.setResimadi(jsonObject.getString("resim"));

                        listt.add(resim);
                        count++;

                    } catch (JSONException eq) {
                        txt.setText("hata:" + eq.getMessage());
                    }
                }//while



                for(;yardir< listt.size();yardir++) {
                    //b.append(listt.get(i).getResimadi() + " \n");

                    final String resimIsmi = listt.get(yardir).getResimadi().trim().toString();

                    String resimURL = "http://www.restoranduragi.com/webservisim/yuklenen_resimler/"+ resimIsmi;
                    ImageRequest image = new ImageRequest(resimURL, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {

                            // resimgoster.setImageBitmap(response);
                             i2 = new ImageView(MainActivity.this);
                            //i2.setRotationX(9);
                            //i2.setPivotX(550);
                            //i2.setTop(550);
                            i2.setImageBitmap(response);
                            TextView t2 = new TextView(getApplicationContext());
                                     t2.setTextSize(25);
                                     t2.setGravity(Gravity.CENTER);
                                     t2.setText(resimIsmi);


                            linear.addView(t2);
                            linear.addView(i2);
                            linear.setBackgroundColor(Color.BLACK);

                        }
                    }, 0, 0, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });// IMAGEREQUEST

                    requestQueue.add(image);
                }//for

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }
}
