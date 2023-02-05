package rudenia.fit.bstu.lab15.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentsDb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS FACULTY ( "
                + "IDFACULTY       INTEGER PRIMARY KEY, "
                + "FACULTY         TEXT    UNIQUE, "
                + "DEAN            TEXT    NOT NULL, "
                + "OFFICETIMETABLE TEXT    NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUP_ ("
                + "IDGROUP INTEGER PRIMARY KEY, "
                + "FACULTY TEXT    NOT NULL, "
                + "COURSE  INTEGER CHECK (COURSE > 0 AND COURSE < 7), "
                + "NAME    TEXT    NOT NULL, "
                + "HEAD    TEXT    DEFAULT(0), "
                + "FOREIGN KEY(FACULTY) REFERENCES FACULTY(FACULTY) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS STUDENT ("
                + "IDGROUP   INTEGER NOT NULL, "
                + "IDSTUDENT INTEGER PRIMARY KEY, "
                + "NAME      TEXT    NOT NULL, "
                + "BIRTHDATE DATE    NOT NULL, "
                + "ADDRESS   TEXT    NOT NULL, "
                + "FOREIGN KEY(IDGROUP) REFERENCES GROUP_(IDGROUP) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS SUBJECT ("
                + "IDSUBJECT INTEGER PRIMARY KEY, "
                + "SUBJECT   TEXT    NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS PROGRESS ("
                + "IDSTUDENT INTEGER NOT NULL, "
                + "IDSUBJECT INTEGER NOT NULL, "
                + "EXAMDATE  DATE    NOT NULL, "
                + "MARK      INTEGER CHECK (MARK >= 0 AND MARK <= 10), "
                + "TEACHER   TEXT    NOT NULL, "
                + "FOREIGN KEY(IDSUBJECT) REFERENCES SUBJECT(IDSUBJECT) "
                + "ON DELETE CASCADE ON UPDATE CASCADE, "
                + "FOREIGN KEY(IDSTUDENT) REFERENCES STUDENT(IDSTUDENT) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE FACULTY;");
        db.execSQL("DROP TABLE GROUP_");
        db.execSQL("DROP TABLE STUDENT;");
        db.execSQL("DROP TABLE SUBJECT");
        db.execSQL("DROP TABLE PROGRESS;");
        onCreate(db);
    }


    public void addFaculty(SQLiteDatabase db, int idFaculty, String nameFaculty, String dean, String timetable ) {
        ContentValues cv = new ContentValues();
        cv.put("IDFACULTY", idFaculty);
        cv.put("FACULTY", nameFaculty);
        cv.put("DEAN", dean);
        cv.put("OFFICETIMETABLE", timetable);
        db.insert("FACULTY", null, cv);
        cv.clear();
    }

    public void addGroup(SQLiteDatabase db, int idGroup, String faculty, int course, String name, String head) {
        ContentValues cv = new ContentValues();
        cv.put("IDGROUP", idGroup);
        cv.put("FACULTY", faculty);
        cv.put("COURSE", course);
        cv.put("NAME", name);
        cv.put("HEAD", head);
        db.insert("GROUP_", null, cv);
        cv.clear();
    }

    public void addStudent(SQLiteDatabase db, int idStudent, int idGroup, String name, String birthDate, String address) {
        ContentValues cv = new ContentValues();
        cv.put("IDSTUDENT", idStudent);
        cv.put("IDGROUP", idGroup);
        cv.put("NAME", name);
        cv.put("BIRTHDATE", birthDate);
        cv.put("ADDRESS", address);
        db.insert("STUDENT", null, cv);
        cv.clear();
    }

    public void addSubject(SQLiteDatabase db, int idSubject, String name){
        ContentValues cv = new ContentValues();
        cv.put("IDSUBJECT", idSubject);
        cv.put("SUBJECT", name);
        db.insert("SUBJECT", null, cv);
        cv.clear();
    }

    public void addProgress(SQLiteDatabase db, int idStudent, int idSubject ,String examDate, int mark, String teacher){
        ContentValues cv = new ContentValues();
        cv.put("IDSTUDENT", idStudent);
        cv.put("IDSUBJECT", idSubject);
        cv.put("EXAMDATE", examDate);
        cv.put("MARK", mark);
        cv.put("TEACHER", teacher);
        db.insert("PROGRESS", null, cv);
        cv.clear();
    }

    public void initDatabase(SQLiteDatabase db) {
        addFaculty(db, 1,"ФИТ", "Шиман","8:00 – 15:00");
        addFaculty(db, 2,"ИЭФ", "Перов","8:00 – 15:00");
        addGroup(db, 1,"ФИТ", 3, "ПОИБМС", "Иванов");
        addGroup(db, 2,"ИЭФ", 3, "NET", "Смирнов");
        addStudent(db, 1, 1, "Иванов", "1998-11-11","ул.Садовая 12");
        addStudent(db, 2, 1, "Сидоров", "1998-02-12","просп.Рокосовского 14");
        addStudent(db, 3, 1, "Петров", "1987-01-09","ул.Иванова 2");
        addStudent(db, 4, 2, "Смирнов", "1991-11-10","ул.Горовца 11");
        addStudent(db, 5, 2, "Азаренко", "1995-06-11","просп.Машерова 114а");
        addStudent(db, 6, 2, "Брусевич", "1997-15-07","ул.Седых 9");
        addSubject(db, 1, "ОАиП");
        addSubject(db, 2, "Бизнес");
        addProgress(db, 1,1,"2010-06-06", 6, "Преподаватель");
        addProgress(db, 2,1,"2010-06-06", 4, "Преподаватель");
        addProgress(db, 3,1,"2010-06-06", 7, "Преподаватель");
        addProgress(db, 4,1,"2017-06-06", 2, "Преподаватель");
        addProgress(db, 5,1,"2017-06-06", 2, "Преподаватель");
        addProgress(db, 6,1,"2017-06-06", 6, "Преподаватель");
        addProgress(db, 1,2,"2014-06-06", 5, "Преподаватель2");
        addProgress(db, 2,2,"2014-06-06", 9, "Преподаватель2");
        addProgress(db, 3,2,"2014-06-06", 3, "Преподаватель2");
        addProgress(db, 4,2,"2016-06-06", 7, "Преподаватель2");
        addProgress(db, 5,2,"2016-06-06", 4, "Преподаватель2");
        addProgress(db, 6,2,"2016-06-06", 4, "Преподаватель2");
    }

    public void createViews(SQLiteDatabase db) {
        db.execSQL("drop view if exists studentProgressGroup; ");
        db.execSQL("create view if not exists studentProgressGroup as " +
                "select s.IDGROUP, g.FACULTY, g.NAME groupName, s.NAME strudentName, p.MARK, p.EXAMDATE from PROGRESS p, STUDENT s, GROUP_ g " +
                "where s.IDSTUDENT =  p.IDSTUDENT and s.IDGROUP = g.IDGROUP; "
        );

        db.execSQL("drop view if exists subjectProgress; ");
        db.execSQL("create view if not exists subjectProgress as " +
                "select st.IDGROUP, s.SUBJECT, st.NAME, p.MARK, p.EXAMDATE from SUBJECT s, PROGRESS p, STUDENT st " +
                "where s.IDSUBJECT =  p.IDSUBJECT and st.IDSTUDENT = p.IDSTUDENT; "
        );
    }

    public void createIndex(SQLiteDatabase db) {
        db.execSQL("create index if not exists idx_examdate " +
                "on PROGRESS (EXAMDATE);"
        );

        db.execSQL("create index if not exists idx_mark  "+
                "on PROGRESS (MARK);"
        );
    }

    public void createTriggers(SQLiteDatabase db) {
        db.execSQL("create trigger tr_Insert_Student " +
                "before insert on STUDENT " +
                "when (select count(STUDENT.IDSTUDENT) from STUDENT where IDGROUP = new.IDGROUP) > 6 " +
                "begin " +
                "select raise(abort, 'Операция INSERT запрещена');" +
                "end;"
        );

        db.execSQL("create trigger tr_Delete_Student " +
                "before delete on STUDENT " +
                "when (select count(STUDENT.IDSTUDENT) from STUDENT where IDGROUP = new.IDGROUP) < 3 " +
                "begin " +
                "select raise(abort, 'Операция DELETE запрещена');" +
                "end;"
        );

        db.execSQL("create trigger tr_Update_Student " +
                "instead of update on studentProgressGroup " +
                "when (select count(*) from STUDENT) < 3 " +
                "begin " +
                "update STUDENT set NAME = new.NAME where IDSTUDENT = old.IDSTUDENT;" +
                "end;"
        );
    }
}