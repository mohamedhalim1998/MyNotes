package com.mohamed.halim.essa.mynotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.mohamed.halim.essa.mynotes.data.NoteContract.NoteEntry;

public class NoteAdapter extends CursorAdapter {

    /**
     * create a new object of the adapter
     * @param context : for the app
     * @param c : the cursor
     */
    public NoteAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NOTE_TITLE));
        String details = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NOTE_DETAILS));

        TextView titleTextView = view.findViewById(R.id.title_textview);
        TextView detailsTextView = view.findViewById(R.id.details_textview);

        titleTextView.setText(title);
        detailsTextView.setText(details);
    }
}
