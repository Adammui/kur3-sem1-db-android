package karbanovich.fit.bstu.students.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "STUDENTSDB";
    private static final String FACULTY_TABLE = "FACULTY";
    private static final String GROUP_TABLE = "[GROUP]";
    private static final String STUDENT_TABLE = "STUDENT";
    private static final String SUBJECT_TABLE = "SUBJECT";
    private static final String PROGRESS_TABLE = "PROGRESS";

    private static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    public static DbHelper getInstance(Context context) {
        if(instance == null) instance = new DbHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FACULTY_TABLE + " (                    "
                    + "IDFACULTY integer primary key autoincrement not null,"
                    + "FACULTY text not null,                               "
                    + "DEAN text not null,                                  "
                    + "STARTWORK text not null,                             "   //format: HH:mm
                    + "ENDWORK text not null                              );"   //format: HH:mm
        );
        db.execSQL("create table " + GROUP_TABLE + " (                                     "
                    + "IDGROUP integer primary key autoincrement not null,                 "
                    + "IDFACULTY integer,                                                  "
                    + "COURSE integer check (COURSE > 0 and COURSE < 7),                   "
                    + "NAME text not null,                                                 "
                    + "HEAD text,                                                          "
                    + "foreign key(IDFACULTY) references " + FACULTY_TABLE + "(IDFACULTY));"
        );
        db.execSQL("create table " + STUDENT_TABLE + " (                           "
                    + "IDSTUDENT integer primary key autoincrement not null,       "
                    + "IDGROUP integer not null,                                   "
                    + "NAME text not null,                                         "
                    + "BIRTHDAY text not null,                                     "  //format: yyyy-MM-dd
                    + "ADDRESS text not null,                                      "
                    + "foreign key(IDGROUP) references " + GROUP_TABLE + "(IDGROUP)"
                       + " on delete cascade on update cascade                   );"
        );
        db.execSQL("create table " + SUBJECT_TABLE + " (                    "
                    + "IDSUBJECT integer primary key autoincrement not null,"
                    + "SUBJECT text not null                              );"
        );
        db.execSQL("create table " + PROGRESS_TABLE + " (                                  "
                    + "IDSTUDENT integer not null,                                         "
                    + "IDSUBJECT integer not null,                                         "
                    + "EXAMDATE text not null,                                             "    //format: yyyy-MM-dd
                    + "MARK integer not null,                                              "
                    + "TEACHER text not null,                                              "
                    + "foreign key(IDSTUDENT) references " + STUDENT_TABLE + "(IDSTUDENT)  "
                       + " on delete cascade on update cascade,                            "
                    + "foreign key(IDSUBJECT) references " + SUBJECT_TABLE + "(IDSUBJECT));"
        );
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + PROGRESS_TABLE);
        db.execSQL("drop table if exists " + SUBJECT_TABLE);
        db.execSQL("drop table if exists " + STUDENT_TABLE);
        db.execSQL("drop table if exists " + GROUP_TABLE);
        db.execSQL("drop table if exists " + FACULTY_TABLE);
        onCreate(db);
    }
}
