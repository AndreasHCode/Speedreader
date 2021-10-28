package de.uni_bamberg.mi.sem.speedreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ReaderActivity extends AppCompatActivity {

    private Database db = new Database(this);
    private int minSpeed = 60;
    private int maxSpeed = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        Bundle extras = getIntent().getExtras();
        String title = "";

        Log.d(MainActivity.DEBUGTAG, "Trying to get Extras");

        if (extras != null) {
            title = extras.getString(MainActivity.TEXTTITLE);
            minSpeed = extras.getInt(MainActivity.MINSPEED);
            maxSpeed = extras.getInt(MainActivity.MAXSPEED);
        }

        startReader(title);
    }

    private void startReader(final String title) {
        final String text = db.getText(title);
        final String[] textSplit = text.split(" ");
        final int baseWordsPerMinute = minSpeed;
        final int maxWordsPerMinute = maxSpeed;

        Thread updaterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int speed = baseWordsPerMinute;
                try {
                    for (String aTextSplit : textSplit) {
                        if (speed < maxWordsPerMinute) {
                            speed++;
                        }
                        Thread.sleep((60000 / speed));
                        updateTextView(aTextSplit, speed);
                    }
                } catch (InterruptedException e) {
                    Log.d(MainActivity.DEBUGTAG, e.getMessage());
                }
            }
        });

        updaterThread.start();
    }

    private void updateTextView(final String text, final int speed) {
        final TextView textView = (TextView) findViewById(R.id.reader_textView);
        final TextView speedView = (TextView) findViewById(R.id.reader_text_speed);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                textView.setText(text);
                speedView.setText(String.valueOf(speed));
            }
        });
    }

}
