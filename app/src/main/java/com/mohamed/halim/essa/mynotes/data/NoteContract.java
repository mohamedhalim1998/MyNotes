package com.mohamed.halim.essa.mynotes.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteContract {
    // private constructor o prevent class creation
    private NoteContract() {}
    //content authority to the content
    public static final String CONTENT_AUTHORITY = "com.mohamed.halim.essa.mynotes";
    // the base uri to append with the path
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // a path to the note table
    public static final String NOTE_PATH = "note";


    // define the constant values for the note table
    public static final class NoteEntry implements BaseColumns {
        // content uri for the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,NOTE_PATH);

        //  The MIME type of the {@link #CONTENT_URI} for a list of pets.

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + NOTE_PATH;


         //The MIME type of the {@link #CONTENT_URI} for a single pet.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + NOTE_PATH;

        // the name of the table
        public static final String TABLE_NAME = "note";
        // unique id for the note
        // type : Integer
        public static final String _ID = BaseColumns._ID;
        // column foe the title of the note
        // type : TEXT
        public static final String COLUMN_NOTE_TITLE = "title";
        // column foe the details of the note
        // type : TEXT
        public static final String COLUMN_NOTE_DETAILS = "details";

    }
}
