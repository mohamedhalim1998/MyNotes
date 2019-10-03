package com.mohamed.halim.essa.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mohamed.halim.essa.mynotes.data.NoteContract.NoteEntry;

public class NoteDbHelper extends SQLiteOpenHelper {
    // the name of the database
    private static final String DATABASE_NAME = "notes.db";

    // the current version of the database
    private static final int VERSION = 1;

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // sql statement to create a note table
        String SQL_CREATE_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteEntry.COLUMN_NOTE_TITLE + " TEXT, "+
                NoteEntry.COLUMN_NOTE_DETAILS + " TEXT );";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // sql statement to drop the table
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ NoteEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
