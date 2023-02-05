package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import karbanovich.fit.bstu.students.Model.Faculty;

public class FacultyDb {

    private static final String FACULTY_TABLE = "FACULTY";


    public static long add(SQLiteDatabase db, Faculty faculty) {
        ContentValues values = new ContentValues();

        values.put("FACULTY", faculty.getName());
        values.put("DEAN", faculty.getDean());
        values.put("STARTWORK", faculty.getStartWork().toString());
        values.put("ENDWORK", faculty.getEndWork().toString());

        return db.insert(FACULTY_TABLE, null, values);
    }

    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + FACULTY_TABLE + ";", null);
    }
}
