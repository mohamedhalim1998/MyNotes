package com.mohamed.halim.essa.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.mohamed.halim.essa.mynotes.data.NoteContract;

public class DetailsActivity extends AppCompatActivity {

    // edit text to hold the title of the note
    EditText mTitle;
    // edit text to hold the details of the note
    EditText mDetails;
    // uri for the id to edit it
    Uri editModeUri;
    // indicate if the note changed
    boolean hasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // get the view from @id.title_edit_text
        mTitle = findViewById(R.id.title_edit_text);
        // get the view from @id.details_edit_text
        mDetails = findViewById(R.id.details_edit_text);
        // state that the note has changed if touched
        mTitle.setOnTouchListener(mTouchListener);
        mDetails.setOnTouchListener(mTouchListener);

        // get the calling intent
        Intent i = getIntent();
        // get the edit uri if found
        editModeUri = i.getData();
        // if the edit uri found
        if (editModeUri != null) {
            // change the title for the activity to edit node
            setTitle(getString(R.string.note_edit_mode));
            // display the title and the details of the note
            displayNote();
        } else {
            // change the title to add note
            setTitle(getString(R.string.note_add_mode));
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            hasChanged = true;
            return false;
        }
    };

    /**
     * display the title and the details of the note
     */
    private void displayNote() {
        // get the note cursor
        Cursor cursor = getContentResolver().query(editModeUri,
                null,
                null,
                null,
                null);
        // check if the cursor has data
        if (cursor.getCount() > 0) {
            // get the first row of data
            cursor.moveToFirst();
            // get the title of the note
            String title = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE));
            // get the details of the note
            String details = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_DETAILS));

            // set the title to its edit text
            mTitle.setText(title);
            // set the details to its edit text
            mDetails.setText(details);
        }
        // close the cursor after opration
        cursor.close();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (editModeUri == null) {
            MenuItem item = menu.findItem(R.id.delete_action);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the menu items
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get the menu item id
        switch (item.getItemId()) {
            case R.id.done_action:
                if (editModeUri != null) {
                    // in edit mode update the note
                    updateNote();
                } else {
                    // add anew note
                    addNote();
                }
                // close the activity
                finish();
                return true;
            case R.id.delete_action:
                // display the delete confirmation dialog
                deleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if(hasChanged){
                    discardConfirmationDialog();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * shows a confirmation dialog when deleting the note
     */
    private void deleteConfirmationDialog() {
        // create a new builder to build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the waring message for the dialog
        builder.setMessage(R.string.delete_confirmation_message);
        // set the confirm action for the dialog
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // if confirmed delete the note
                deleteNote();
                finish();
            }
        };
        // set the cancel action for the dialog
        DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    // dismiss the dialog and continue
                    dialogInterface.dismiss();
                }
            }
        };
        // set the positive button to the confirm action
        builder.setPositiveButton(R.string.delete_confirmation_yes, positive);
        // set the negative button to the cancel action
        builder.setNegativeButton(R.string.delete_confirmation_no, negative);
        // build the dialog from the dialog builder
        AlertDialog alertDialog = builder.create();
        // display the dialog
        alertDialog.show();
    }

    /**
     * delete a note from the database
     */
    private void deleteNote() {
        getContentResolver().delete(editModeUri, null, null);
    }

    /**
     * update a note
     */
    private void updateNote() {
        ContentValues values = getNoteValues();
        getContentResolver().update(editModeUri, values, null, null);
    }

    /**
     * add a new note
     */
    private void addNote() {
        ContentValues values = getNoteValues();
        getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI, values);
    }

    /**
     * get the title and the details from the input fields
     *
     * @return contentValues hold the input values
     */
    private ContentValues getNoteValues() {
        ContentValues values = new ContentValues();
        String title = mTitle.getText().toString();
        String details = mDetails.getText().toString();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, title);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_DETAILS, details);
        return values;
    }


    /**
     * shows a confirmation dialog when discard the change
     */
    private void discardConfirmationDialog() {
        // create a new builder to build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the waring message for the dialog
        builder.setMessage(R.string.discard_confirmation_message);
        // set the confirm action for the dialog
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // if confirmed delete the note
                finish();
            }
        };
        // set the cancel action for the dialog
        DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    // dismiss the dialog and continue
                    dialogInterface.dismiss();
                }
            }
        };
        // set the positive button to the confirm action
        builder.setPositiveButton(R.string.discard_confirmation_yes, positive);
        // set the negative button to the cancel action
        builder.setNegativeButton(R.string.delete_confirmation_no, negative);
        // build the dialog from the dialog builder
        AlertDialog alertDialog = builder.create();
        // display the dialog
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(hasChanged){
            discardConfirmationDialog();
            return;
        }
        super.onBackPressed();
    }
}
