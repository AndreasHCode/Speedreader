package de.uni_bamberg.mi.sem.speedreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends android.app.ListActivity {

    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Text> texts = db.getAllTexts();

        ReaderAdapter adapter = new ReaderAdapter(this, texts);
        setListAdapter(adapter);

    }

    // Communication Toast vs Alertbutton

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Text text = (Text) getListAdapter().getItem(position);
        Toast.makeText(this, text.getTitle() + " selected.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TEXTTITLE, text.getTitle());
        startActivity(intent);
    }
}
