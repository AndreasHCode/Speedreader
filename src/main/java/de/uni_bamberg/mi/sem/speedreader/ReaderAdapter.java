package de.uni_bamberg.mi.sem.speedreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andreas on 17.06.2017.
 */

public class ReaderAdapter extends BaseAdapter implements android.widget.ListAdapter {

    private Context context;
    private List<Text> texts;
    private LayoutInflater inflater;

    public ReaderAdapter(Context context, List<Text> texts) {
        this.context = context;
        this.texts = texts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // https://de.wikibooks.org/wiki/Googles_Android/_ListActivity

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Views views;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list, null);
            views = new Views();
            views.id = (TextView) convertView.findViewById(R.id.list_idTextView);
            views.title = (TextView) convertView.findViewById(R.id.list_titleTextView);

            convertView.setTag(views);
        } else {
            views = (Views) convertView.getTag();
        }

        Text text = texts.get(position);

        views.id.setText(String.valueOf(position + 1));
        views.title.setText(text.getTitle());

        return convertView;
    }

    private static class Views {
        TextView id;
        TextView title;
    }
}
