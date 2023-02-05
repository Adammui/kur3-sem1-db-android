package ast.fit.bstu.db10;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "LAB_10.db";
    static int SCHEMA = 1;
    static String TABLE_GROUPS_NAME = "Groups";
    static String TABLE_STUDENTS_NAME = "Students";

    static String GROUPS_COLUMN_IDGROUP = "_id";
    static String GROUPS_COLUMN_FACULTY = "Faculty";
    static String GROUPS_COLUMN_COURSE = "Course";
    static String GROUPS_COLUMN_NAME = "Name";
    static String GROUPS_COLUMN_HEAD = "Head";

    static String STUDENTS_COLUMN_IDGROUP = "IDGroup";
    static String STUDENTS_COLUMN_IDSTUDENT = "_id";
    static String STUDENTS_COLUMN_NAME = "Name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("pragma foreign_keys = on");
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createGroupsTable(db);
        createStudentsTable(db);
    }

    private void createStudentsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS_NAME + " ("
                + STUDENTS_COLUMN_IDGROUP + " INTEGER,"
                + STUDENTS_COLUMN_IDSTUDENT + " INTEGER primary key autoincrement, "
                + STUDENTS_COLUMN_NAME + " TEXT,"
                + "  FOREIGN KEY(" + STUDENTS_COLUMN_IDGROUP + ") REFERENCES " + TABLE_GROUPS_NAME + "(" + GROUPS_COLUMN_IDGROUP + ") ON UPDATE CASCADE ON DELETE CASCADE"
                + ");");
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                " (" + STUDENTS_COLUMN_IDGROUP + ", " + STUDENTS_COLUMN_NAME + ") " +
                " VALUES (1, 'Vladislav');");
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                " (" + STUDENTS_COLUMN_IDGROUP + ", " + STUDENTS_COLUMN_NAME + ") " +
                " VALUES (1, 'Mark');");
    }

    private void createGroupsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_GROUPS_NAME + " ("
                + GROUPS_COLUMN_IDGROUP + " INTEGER PRIMARY KEY autoincrement,"
                + GROUPS_COLUMN_FACULTY + " TEXT, "
                + GROUPS_COLUMN_COURSE + " INTEGER,"
                + GROUPS_COLUMN_NAME + " TEXT,"
                + GROUPS_COLUMN_HEAD + " TEXT"
                + ");");
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " " +
                ") VALUES ( 'FIT', 3,'POIBMS','Not specified');");
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " " +
                ") VALUES ( 'FIT', 1,'POIT','Not specified');");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertGroups(SQLiteDatabase db, String fac, int course, String name, String head) {
        db.execSQL("INSERT INTO " + TABLE_GROUPS_NAME
                + " ("
                + GROUPS_COLUMN_FACULTY + ", "
                + GROUPS_COLUMN_COURSE + " ,"
                + GROUPS_COLUMN_NAME + " ,"
                + GROUPS_COLUMN_HEAD + " )" +
                " VALUES ('" + fac + "', " + course + ", '" + name + "', '" + head + "');");
    }

    public void deleteGroups(SQLiteDatabase db, int id) {
        db.execSQL("Delete FROM " + TABLE_GROUPS_NAME +
                " Where "+ GROUPS_COLUMN_IDGROUP +"=" + id + ";");
    }

    public void updateGroups(SQLiteDatabase db, int id, int new_id) {
        db.execSQL("Update " + TABLE_GROUPS_NAME +
                " Set " + GROUPS_COLUMN_IDGROUP + "='" + new_id +
                "' Where " + GROUPS_COLUMN_IDGROUP + "=" + id + ";");
        ;
    }

    public void insertStudent(SQLiteDatabase db, int idg, String name) {
        db.execSQL("INSERT INTO " + TABLE_STUDENTS_NAME +
                "(" + STUDENTS_COLUMN_IDGROUP + "," + STUDENTS_COLUMN_NAME + ")" +
                " VALUES (" + idg + ", '" + name + "');");
    }

}