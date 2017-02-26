package com.example.com.wingsbangladesh.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.com.wingsbangladesh.Adapter.ConcernedPickUpAdapter;
import com.example.com.wingsbangladesh.Model.CustomModel;
import com.example.com.wingsbangladesh.Interface.ItemClickListener;
import com.example.com.wingsbangladesh.Model.ModelPrint;
import com.example.com.wingsbangladesh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by sabbir on 2/22/17.
 */

public class ConcernedMarchantPickupActivity extends AppCompatActivity implements ItemClickListener {

    String  URL,name,id;
   // ModelPrint m;
    ModelPrint print;
    private List<ModelPrint> modelPrintList = new ArrayList<>();
    // private List<ModelPickUpSummary> modellIst = new ArrayList<>();

    CustomModel custom;
    private RecyclerView recyclerView;
    private ConcernedPickUpAdapter mAdapter;
    LinearLayout lnrLayout;
    Button logout;
    ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.concerned_marchant_pickup_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        logout=(Button)findViewById(R.id.logout);
        settings=(ImageView)findViewById(R.id.setting);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(ConcernedMarchantPickupActivity.this,SettingActivity.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().hide();
        Intent intent=getIntent();
         id = intent.getStringExtra("id");

   new GetData().execute();


        recyclerView.addOnItemTouchListener(new MarchantInfoActivity.RecyclerTouchListener(ConcernedMarchantPickupActivity.this, recyclerView, new MarchantInfoActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                print = new ModelPrint();
                print = modelPrintList.get(position);


                //Details
                Button btn = (Button) view.findViewById(R.id.button);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent=new Intent(ConcernedMarchantPickupActivity.this,BarcodeActivity.class);
                        intent.putExtra("id",  print.getBarcodeId());
                        intent.putExtra("barcode_token",print.getBarCode());


                        startActivity(intent);



                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




    }

    @Override
    public void onBackPressed()
    {
    }



    public void APICall() {

        URL="http://paperfly.mybdweb.com/barcode_list.php";

        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                // mEntries.add(jsonObject.toString());


                                String barcode_id=jsonObject.getString("barcode_id");
                                String paperfy_order_id=jsonObject.getString("paperfy_order_id");
                                String barcode=jsonObject.getString("barcode");
                                String marchent_code=jsonObject.getString("marchent_code");

                                System.out.println("marchent_code::"+marchent_code);

                                ModelPrint  m=new ModelPrint();
                                m.setBarcodeId(barcode_id);
                                m.setPaperFlyOrder(paperfy_order_id);
                                m.setBarCode(barcode);
                                m.setMarchentCode(marchent_code);


                                System.out.println("barcode::"+barcode);

                                modelPrintList.add(m);

                                System.out.println("modelPrintList::"+modelPrintList);


                                mAdapter = new ConcernedPickUpAdapter(modelPrintList);


                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);

                                recyclerView.setAdapter(mAdapter);

                            }
                            catch(JSONException e) {
                                // mEntries.add("Error: " + e.getLocalizedMessage());
                            }
                        }

                        // allDone();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ConcernedMarchantPickupActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // mEntries = new ArrayList<>();
        // requestQueue.add(request);

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ConcernedMarchantPickupActivity.this);
        requestQueue.add(request);
    }

  /*  public void APICall() {

        URL="http://paperfly.mybdweb.com/barcode_list.php/"+id;

        System.out.println("URL::"+URL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {


                        System.out.println("jsonObject::"+jsonObject);
                      //  for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                             //  JSONObject a = jsonObject.getJSONObject();
                                // mEntries.add(jsonObject.toString());


                                String barcode_id=jsonObject.getString("barcode_id");
                                String paperfy_order_id=jsonObject.getString("paperfy_order_id");
                                String barcode=jsonObject.getString("barcodes");
                                String marchent_code=jsonObject.getString("marchent_code");

                                System.out.println("marchent_code::"+marchent_code);

                                ModelPrint  m=new ModelPrint();
                                m.setBarcodeId(barcode_id);
                                m.setPaperFlyOrder(paperfy_order_id);
                                m.setBarCode(barcode);
                                m.setMarchentCode(marchent_code);


                                System.out.println("barcode::"+barcode);

                                modelPrintList.add(m);

                                System.out.println("modelPrintList::"+modelPrintList);


                                mAdapter = new ConcernedPickUpAdapter(modelPrintList);


                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);

                                recyclerView.setAdapter(mAdapter);





                            }
                            catch(JSONException e) {
                                // mEntries.add("Error: " + e.getLocalizedMessage());
                            }
                        }

                        // allDone();

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                     //   Toast.makeText(MarchantInfoActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // mEntries = new ArrayList<>();
        // requestQueue.add(request);

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(ConcernedMarchantPickupActivity.this);
        requestQueue.add(request);
    }*/

    @Override
    public void onClick(View view, int position) {

    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, ClickListener clickListener1) {
            clickListener = clickListener1;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private  class GetData extends AsyncTask<Void, Void, Boolean> {
        SweetAlertDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new SweetAlertDialog(ConcernedMarchantPickupActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... uRls) {




            APICall();

            // uploadImage();

            return null;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            //pDialog.setVisibility(View.INVISIBLE);
            //   prog.setVisibility(View.GONE);


            //   prog.setVisibility(View.GONE);
            pDialog.dismiss();





        }

    }
}




