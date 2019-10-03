package com.mohamed.halim.essa.mynotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.mohamed.halim.essa.mynotes.data.NoteContract.NoteEntry;


public class NoteProvider extends ContentProvider {
    // log tag
    private static final String TAG = NoteProvider.class.getSimpleName();
    // uri matcher code for the note table
    private static final int NOTES = 100;
    // uri matcher code for a single note in the table
    private static final int NOTE_ID = 101;

    // to match the content uri to a code
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    //
    private NoteDbHelper helper;

    static {
        // add the notes to the matcher
        matcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.NOTE_PATH, NOTES);
        // add  single ote to the matcher
        matcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.NOTE_PATH + "/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        // create a new helper object to interact with the database
        helper = new NoteDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String orderBy) {
        // get the code from the uri matcher
        int match = matcher.match(uri);
        // get a readable database to query
        SQLiteDatabase db = helper.getReadableDatabase();
        // save the result
        Cursor c;


        switch (match) {
            case NOTES:
                // in case of the table query the table by the  given parameters
                c = db.query(NoteEntry.TABLE_NAME, columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            case NOTE_ID:
                // in case of specific id
                // make the selection to id column
                selection = NoteEntry._ID + "=?";
                // get the id from the uri
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                //query the table with the id from the selection args
                c = db.query(NoteEntry.TABLE_NAME, columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // set notfiction to catch any change
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {
            case NOTES:
                return NoteEntry.CONTENT_LIST_TYPE;
            case NOTE_ID:
                return NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = matcher.match(uri);
        switch (match) {
            case NOTES:
                return insertNote(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot inters into URI " + uri);
        }
    }

    /**
     * insert a new note to the database
     */
    private Uri insertNote(Uri uri, ContentValues contentValues) {
        // get a database to write to
        SQLiteDatabase db = helper.getWritableDatabase();
        // insert the values to the database
        long id = db.insert(NoteEntry.TABLE_NAME, null, contentValues);
        // check if the note inserted correctly
        if (id == -1) {
            Log.e(TAG, "can't insert the note");
            return null;
        }
        // notify the cursor about the change
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        // get a database to write to
        SQLiteDatabase db = helper.getWritableDatabase();
        // get the code from the uri matcher
        int match = matcher.match(uri);
        // rows deleted
        int rows = 0;
        switch (match){
            case NOTES:
                // delete the rows with the given parameters
                rows = db.delete(NoteEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            case  NOTE_ID:
                // change the condition for deletion to id
                whereClause = NoteEntry._ID  + "=?";
                // get the id from uri
                whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // delete the note with the id
                rows = db.delete(NoteEntry.TABLE_NAME, whereClause, whereArgs);
                break;
        }
        if(rows == 0){
            Log.e(TAG, "delete unsuccessful");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {
        // get a database to write to
        SQLiteDatabase db = helper.getWritableDatabase();
        // get the code from the uri matcher
        int match = matcher.match(uri);
        // rows updated
        int rows = 0;
        switch (match){
            case NOTES:
                // update the rows with the given parameters
                rows = updateNote(uri, contentValues, whereClause, whereArgs);
                break;
            case  NOTE_ID:
                // change the condition for deletion to id
                whereClause = NoteEntry._ID  + "=?";
                // get the id from uri
                whereArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // delete the note with the id
                rows = updateNote(uri, contentValues, whereClause, whereArgs);
                break;
        }
        if(rows == 0){
            Log.e(TAG, "update unsuccessful");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    private int updateNote(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.update(NoteEntry.TABLE_NAME, values, whereClause, whereArgs);
    }


}
