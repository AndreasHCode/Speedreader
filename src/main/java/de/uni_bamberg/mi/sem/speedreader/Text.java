package de.uni_bamberg.mi.sem.speedreader;

/**
 * Created by Andreas and Matthias on 17.06.2017.
 */

public class Text {

    private String title;
    private String text;

    public Text(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
