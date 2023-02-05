package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Currency;

import karbanovich.fit.bstu.students.Model.Subject;

public class SubjectDb {

    private static final String SUBJECT_TABLE = "SUBJECT";


    public static long add(SQLiteDatabase db, Subject subject) {
        ContentValues values = new ContentValues();
        values.put("SUBJECT", subject.getName());
        return db.insert(SUBJECT_TABLE, null, values);
    }

    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + SUBJECT_TABLE + ";", null);
    }
}
