package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import karbanovich.fit.bstu.students.Model.Student;

public class StudentDb {

    private static final String STUDENT_TABLE = "STUDENT";


    public static long add(SQLiteDatabase db, Student student) {

        db.execSQL("drop trigger if exists trgr_STUD_INSERT");

        String createTrigger =
                "create trigger if not exists trgr_STUD_INSERT before insert on " + STUDENT_TABLE
                + " begin"
                    + " select case"
                        + " when (select count(IDSTUDENT)"
                                    + " from " + STUDENT_TABLE
                                    + " where IDGROUP = " + student.getGroupId() + ") >= 6"
                        + " then"
                            + " raise(abort, 'there must be less than 6 students in the group')"
                    + " end;"
                + " end;";

        db.execSQL(createTrigger);

        ContentValues values = new ContentValues();

        values.put("IDGROUP", student.getGroupId());
        values.put("NAME", student.getName());
        values.put("BIRTHDAY", student.getBirthday().toString());
        values.put("ADDRESS", student.getAddress());

        return db.insertOrThrow(STUDENT_TABLE, null, values);
    }

    public static long deleteStudent(SQLiteDatabase db, Student student) {

        db.execSQL("drop trigger if exists trgr_STUD_DELETE");

        String createTrigger =
                "create trigger if not exists trgr_STUD_DELETE before delete on " + STUDENT_TABLE
                    + " begin"
                        + " select case"
                            + " when (select count(IDSTUDENT)"
                                        + " from " + STUDENT_TABLE
                                        + " where IDGROUP = " + student.getGroupId() + ") <= 3"
                        + " then "
                            + " raise(abort, 'there can be no less than 3 students in a group')"
                    + " end;"
                + " end";

        db.execSQL(createTrigger);

        return db.delete(STUDENT_TABLE, "IDSTUDENT = ?", new String[] {String.valueOf(student.getId())});
    }

    public static long updateStudent(SQLiteDatabase db, Student student) {

        db.execSQL("drop view if exists STUD_VIEW");
        db.execSQL("drop trigger if exists trgr_STUD_UPDATE");

        String createView = "create view if not exists STUD_VIEW"
                            + " as select IDSTUDENT, NAME, BIRTHDAY, ADDRESS"
                            + " from " + STUDENT_TABLE;

        db.execSQL(createView);


        String createTrigger = "create trigger trgr_STUD_UPDATE instead of update on STUD_VIEW"
                                +" begin "
                                    + " update " + STUDENT_TABLE
                                            + " set NAME = '" + student.getName() + "',"
                                                 +" BIRTHDAY = '" + student.getBirthday().toString() + "',"
                                                 +" ADDRESS = '" + student.getAddress() +"'"
                                    + " where IDSTUDENT = " + student.getId() + ";"
                                + " end;";

        db.execSQL(createTrigger);

        ContentValues values = new ContentValues();
        values.put("NAME", student.getName());
        values.put("BIRTHDAY", student.getBirthday().toString());
        values.put("ADDRESS", student.getAddress());

        return db.update(STUDENT_TABLE, values, "IDSTUDENT = ?", new String[] {String.valueOf(student.getId())});
    }

    public static Cursor getStudentsByGroupId(SQLiteDatabase db, int groupId) {

        db.execSQL("create index if not exists idx_StudTbl_IDGROUP on " + STUDENT_TABLE + "("
                + "IDGROUP)");

        return db.rawQuery("select * from " + STUDENT_TABLE + " where IDGROUP = " + groupId + ";", null);
    }
}
