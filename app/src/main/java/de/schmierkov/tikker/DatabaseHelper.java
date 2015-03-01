package de.schmierkov.tikker;

import de.schmierkov.tikker.model.Message;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "tikker";
    private static final String TABLE_NAME = "messages";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "message TEXT, " +
                "user TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        this.onCreate(db);
    }

    private static final String KEY_ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_TEXT = "text";

    private static final String[] COLUMNS = {KEY_ID,KEY_USER,KEY_TEXT};

    public void addMessage(Message message){
        Log.d("addMessage", message.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER, message.user);
        values.put(KEY_TEXT, message.text);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public Message getMessage(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_NAME,
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Message message = new Message();
        message.setId(Integer.parseInt(cursor.getString(0)));
        message.setUser(cursor.getString(1));
        message.setText(cursor.getString(2));

        Log.d("getMessage("+id+")", message.toString());

        return message;
    }


    // Get All Books
    public List<Message> getAllMessages() {
        List<Message> messages = new LinkedList<Message>();

        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Message message = null;
        if (cursor.moveToFirst()) {
            do {
                message = new Message();
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setUser(cursor.getString(1));
                message.setText(cursor.getString(2));

                messages.add(message);
            } while (cursor.moveToNext());
        }

        Log.d("getAllMessages()", messages.toString());

        return messages;
    }

    public int updateMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user", message.getUser());
        values.put("text", message.getText());

        int i = db.update(TABLE_NAME,
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[] { String.valueOf(message.getId()) });

        db.close();

        return i;
    }

    public void deleteMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,
                KEY_ID+" = ?",
                new String[] { String.valueOf(message.getId()) });

        db.close();

        Log.d("deleteBook", message.toString());

    }
}