package de.uni_bamberg.mi.sem.speedreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUGTAG = "AHD";

    public static final String TEXTTITLE = "TEXT_TITLE";
    private static final String FIRSTSTART = "FIRST_START";
    public static final String MINSPEED = "MINSPEED";
    public static final String MAXSPEED = "MAXSPEED";

    private int minSpeed = 60;
    private int maxSpeed = 250;
    private Database db = new Database(this);
    private String currentTitle;

    //https://developer.android.com/guide/index.html

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean(FIRSTSTART, true);

        if (firstStart) {
            String[] text1 = {"Dostojewski", getString(R.string.text_Dostojewski)};
            String[] text2 = {"Braid", getString(R.string.text_Braid)};
            db.storeText(text1);
            db.storeText(text2);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRSTSTART, false);
            editor.commit();
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            currentTitle = extras.getString(MainActivity.TEXTTITLE);
            ((TextView) findViewById(R.id.main_selectedTitle)).setText(currentTitle);
            Log.d(DEBUGTAG, "Current Title: " + currentTitle);
        }

        addDbButtonListener();
        addHtmlButtonListener();
        addStartButtonListener();
    }

    private void addDbButtonListener() {
        Button button = (Button) findViewById(R.id.btn_normtext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
    }

    private void addHtmlButtonListener() {
        Button button = (Button) findViewById(R.id.btn_httptext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHttpRequest();
            }
        });
    }

    private void sendHttpRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://arstechnica.com/gadgets/2017/03/hands-on-with-android-o-snooze-that-notification-and-customize-everything/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String[] split = response.split("(<p>|</p>)");
                StringBuilder paragraphs = new StringBuilder();

                for (String segment : split) {
                    if (segment.contains("<")) {
                    } else {
                        paragraphs.append(segment + " ");
                    }
                }
                String[] text = {"Ars Technica", paragraphs.toString()};

                if (db.getText("Ars Technica") == "") {
                    db.storeText(text);
                    Toast.makeText(MainActivity.this, "Text " + text[0] + " stored.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Text " + text[0] + " already in Database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DEBUGTAG, error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void addStartButtonListener() {
        Button button = (Button) findViewById(R.id.btn_start);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTitle != null) {
                    Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
                    intent.putExtra(TEXTTITLE, currentTitle);
                    intent.putExtra(MINSPEED, minSpeed);
                    intent.putExtra(MAXSPEED, maxSpeed);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No Text selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_max || id == R.id.main_menu_min) {

        } else {
            int title = Integer.parseInt((String) item.getTitle());

            if (title <= 120) {
                minSpeed = title;
            } else {
                maxSpeed = title;
            }

            Toast.makeText(this, "Min - " + minSpeed + ", Max - " + maxSpeed, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
