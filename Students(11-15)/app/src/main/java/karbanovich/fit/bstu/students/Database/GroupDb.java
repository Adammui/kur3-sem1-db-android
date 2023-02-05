package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import karbanovich.fit.bstu.students.Model.Group;

public class GroupDb {

    private static final String GROUP_TABLE = "[GROUP]";


    public static long add(SQLiteDatabase db, Group group) {
        ContentValues values = new ContentValues();

        values.put("IDFACULTY", group.getFacultyId());
        values.put("COURSE", group.getCourse());
        values.put("NAME", group.getName());
        values.putNull("HEAD");

        return db.insert(GROUP_TABLE, null, values);
    }

    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + GROUP_TABLE + ";", null);
    }

    public static long updateHead(SQLiteDatabase db, int groupId, String head) {
        ContentValues values = new ContentValues();
        values.put("HEAD", head);
        return db.update(GROUP_TABLE, values, "IDGROUP = ?", new String[] { String.valueOf(groupId) });
    }
}
