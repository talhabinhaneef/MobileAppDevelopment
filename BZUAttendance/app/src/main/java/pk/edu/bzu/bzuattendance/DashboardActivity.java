package pk.edu.bzu.bzuattendance;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "qadir";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String Name = getIntent().getStringExtra("Name");
        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText("Welcome " + Name);

        getLoadAttendanceHistory();
    }

    // This function gets data from API and loads data into list view
    public  void getLoadAttendanceHistory()
    {
        RequestQueue queue = Volley.newRequestQueue(DashboardActivity.this);
        String url = "http://10.0.2.2/attendance/history";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("RollNumber", "LDBTT-1923-01");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //pbLoading.setVisibility(View.INVISIBLE);
                try {
                    ArrayList<AttendanceHistory> list = new ArrayList<>();
                    JSONArray array = response.getJSONArray("AttendanceHistory");
                    for (int i=0;i<array.length();i++) {
                        JSONObject o = (JSONObject) array.get(i);
                        AttendanceHistory obj = new AttendanceHistory(
                                o.getString("Date"),
                                o.getString("TimeSlot"),
                                o.getString("SubjectCode"),
                                o.getString("Subject"),
                                o.getString("Teacher"),
                                o.getString("Status")
                        );
                        list.add(obj);
                    }
                    AttendanceHistoryAdapter adapter = new AttendanceHistoryAdapter(DashboardActivity.this, list);
                    ListView lvAttendanceHistory = (ListView) findViewById(R.id.lvAttendanceHistory);
                    lvAttendanceHistory.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pbLoading.setVisibility(View.INVISIBLE);
                //Toast.makeText(LoginActivity.this, "Volley Error: "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

    }
}
