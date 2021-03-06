package com.example.user.zippypark;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 3/19/2020.
 */

public class ReservationHistoryActivity extends AppCompatActivity {

    ListView listView;
    Button homeButton;
    int barcode;
    List<Reservation> reservations;
    static CustomAdapter customAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_history);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode");
        reservations = new ArrayList<>(10);

        homeButton = findViewById(R.id.resHistory_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationHistoryActivity.this,
                        HomeMenuActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        new GetReservationsTask().execute();
    }

    public static void updateAdapter() {
        customAdapter.notifyDataSetChanged();
    }

    class GetReservationsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(Calendar.getInstance().getTime());

                Statement stmt = MainActivity.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Records WHERE " +
                        "Barcode=" + barcode + " ORDER BY Date DESC");

                if(!rs.next()) Log.d("ResHistory", "no reservations for this user");

                while (rs.next()) {

//                          String msg = "Date" + rs.getDate("Date") +
//                                  "StartTime" + rs.getTime("StartTime") +
//                                  "EndTime" + rs.getTime("EndTime") +
//                                  "Barcode" + rs.getInt("Barcode");
//
//                          Log.d("ResHistory", msg);

                    Reservation r = new Reservation(rs.getDate("Date"),
                            rs.getTime("StartTime"),
                            rs.getTime("EndTime"),
                            rs.getInt("Barcode"),
                            rs.getInt("AssignedSpot"),
                            rs.getDouble("Charge"),
                            rs.getInt("rID"),
                            rs.getInt("VIP"));
                    reservations.add(r);
//                    Log.d("CurrentRes", ""+reservations.contains(r));

//                    msg = "Date" + r.getDate() +
//                            "StartTime" + r.getStartTime() +
//                            "EndTime" + r.getEndTime() +
//                            "Barcode" + r.getBarcode();
//
//                    Log.d("CurrentReservationsInside", msg);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            for(Reservation r:reservations){
//                String msg = "Date" + r.getDate() +
//                        "StartTime" + r.getStartTime() +
//                        "EndTime" + r.getEndTime() +
//                        "Barcode" + r.getBarcode();
//
//                Log.d("CurrentReservationsOutside", msg);
//            }
            if(reservations != null && !reservations.isEmpty()) {
                listView = findViewById(R.id.listview);
                customAdapter = new CustomAdapter(reservations, getApplicationContext());
                listView.setAdapter(customAdapter);
            }
        }
    }

}

