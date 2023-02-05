package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;

import karbanovich.fit.bstu.students.Model.Progress;

public class ProgressDb {

    private static final String FACULTY_TABLE = "FACULTY";
    private static final String GROUP_TABLE = "[GROUP]";
    private static final String STUDENT_TABLE = "STUDENT";
    private static final String SUBJECT_TABLE = "SUBJECT";
    private static final String PROGRESS_TABLE = "PROGRESS";


    public static long add(SQLiteDatabase db, Progress progress) {
        ContentValues values = new ContentValues();

        values.put("IDSTUDENT", progress.getStudentId());
        values.put("IDSUBJECT", progress.getSubjectId());
        values.put("EXAMDATE", progress.getDate().toString());
        values.put("MARK", progress.getMark());
        values.put("TEACHER", progress.getTeacher());

        return db.insert(PROGRESS_TABLE, null, values);
    }

    public static Cursor getBestStudByFaculty(SQLiteDatabase db, int facultyId, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_GroupTbl_Fac on " + GROUP_TABLE + "("
                + "IDFACULTY)");

        db.execSQL("create index if not exists idx_ProgressTbl_ExamDate on " + PROGRESS_TABLE + "("
                    + "EXAMDATE)");


        String createView =
                "create temp view if not exists BESTSTUD_view"
                 + " as select " + STUDENT_TABLE + ".NAME," + " avg(" + PROGRESS_TABLE + ".MARK)"
                 + " from " + PROGRESS_TABLE
                     + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                     + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                 + " where " + GROUP_TABLE + ".IDFACULTY = " + facultyId
                     + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                     + " and not exists (select MARK from " + PROGRESS_TABLE
                         + " where MARK < 4"
                             + " and " + PROGRESS_TABLE + ".IDSTUDENT = " + STUDENT_TABLE + ".IDSTUDENT"
                             + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "'))"
                 + " group by " + PROGRESS_TABLE + ".IDSTUDENT"
                 + " order by avg(" + PROGRESS_TABLE + ".MARK) desc"
                 + " limit 5";

        db.execSQL(createView);

        return db.rawQuery("select * from BESTSTUD_view", null);
    }

    public static Cursor getUnderperfStudByFaculty(SQLiteDatabase db, int facultyId, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_GroupTbl_Fac on " + GROUP_TABLE + "("
                + "IDFACULTY)");

        db.execSQL("create index if not exists idx_UnderperfStud on " + PROGRESS_TABLE + "("
                + "EXAMDATE) where MARK < 4");


        String createView =
                "create temp view if not exists UNDERPERFSTUD_view"
                 + " as select "
                     + STUDENT_TABLE + ".NAME as NAME,"
                     + " (select count(MARK)"
                         + " from " + PROGRESS_TABLE
                         + " where MARK < 4"
                             + " and " + PROGRESS_TABLE + ".IDSTUDENT = " + STUDENT_TABLE + ".IDSTUDENT"
                             + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')) as COUNTMARKS"
                + " from " + PROGRESS_TABLE
                    + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                    + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                + " where " + GROUP_TABLE + ".IDFACULTY = " + facultyId
                    + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                    + " and countMarks >= 1"
                + " group by " + PROGRESS_TABLE + ".IDSTUDENT"
                + " order by countMarks desc";

        db.execSQL(createView);

        return db.rawQuery("select NAME, COUNTMARKS from UNDERPERFSTUD_view", null);
    }

    public static Cursor getComparByFaculty(SQLiteDatabase db, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_ProgressTbl_ExamDate on " + PROGRESS_TABLE + "("
                + "EXAMDATE)");


        String createView =
                "create temp view if not exists COMPARFAC_view"
                 + " as select "
                     + FACULTY_TABLE + ".FACULTY as FACULTY,"
                     + " avg(" + PROGRESS_TABLE + ".MARK) as AVGMARK"
                 + " from " + PROGRESS_TABLE
                     + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                     + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                     + " join " + FACULTY_TABLE + " on " + FACULTY_TABLE + ".IDFACULTY = " + GROUP_TABLE + ".IDFACULTY"
                 + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                 + " group by " + FACULTY_TABLE + ".IDFACULTY";

        db.execSQL(createView);

        String query =
                "select FACULTY, AVGMARK"
                 + " from COMPARFAC_view"
                 + " order by AVGMARK";

        return db.rawQuery(query, null);
    }

    public static Cursor getComparByGroups(SQLiteDatabase db, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_ProgressTbl_ExamDate on " + PROGRESS_TABLE + "("
                + "EXAMDATE)");


        String createView =
                "create temp view if not exists COMPARGR_view"
                 + " as select "
                     + GROUP_TABLE + ".NAME, "
                     + GROUP_TABLE + ".COURSE, "
                     + SUBJECT_TABLE + ".SUBJECT, "
                     + " avg(" + PROGRESS_TABLE + ".MARK) as AVGMARK"
                 + " from " + PROGRESS_TABLE
                     + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                     + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                     + " join " + SUBJECT_TABLE + " on " + PROGRESS_TABLE + ".IDSUBJECT = " + SUBJECT_TABLE + ".IDSUBJECT"
                 + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                 + " group by " + GROUP_TABLE + ".NAME, " + GROUP_TABLE + ".COURSE, " + SUBJECT_TABLE +".IDSUBJECT";

        db.execSQL(createView);

        String query = "select * from COMPARGR_view order by AVGMARK desc";

        return db.rawQuery(query, null);
    }

    public static Cursor getAvgMarkByGroup(SQLiteDatabase db, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_ProgressTbl_ExamDate on " + PROGRESS_TABLE + "("
                + "EXAMDATE)");


        String createView =
                "create temp view if not exists AVGMARKGR_view"
                 + " as select " + GROUP_TABLE + ".NAME, " + GROUP_TABLE + ".COURSE, avg(" + PROGRESS_TABLE + ".MARK)"
                 + " from " + PROGRESS_TABLE
                     + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                     + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                 + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                 + " group by " + GROUP_TABLE + ".IDGROUP"
                 + " order by avg(" + PROGRESS_TABLE + ".MARK) desc";

        db.execSQL(createView);

        return db.rawQuery("select * from AVGMARKGR_view", null);
    }

    public static Cursor getAvgMarkByStudents(SQLiteDatabase db, int groupId, String startDate, String endDate) {

        db.execSQL("create index if not exists idx_GroupTbl_Fac on " + STUDENT_TABLE + "("
                + "IDGROUP)");

        db.execSQL("create index if not exists idx_ProgressTbl_ExamDate on " + PROGRESS_TABLE + "("
                + "EXAMDATE)");


        String createView =
                "create temp view if not exists AVGMARKSTUD_view"
                 + " as select "
                     + STUDENT_TABLE + ".NAME as NAME,"
                     + " avg(" + PROGRESS_TABLE + ".MARK) as AVGMARK"
                 + " from " + PROGRESS_TABLE
                     + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                 + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                     + " and " + STUDENT_TABLE + ".IDGROUP = " + groupId
                 + " group by " + STUDENT_TABLE + ".IDSTUDENT";

        db.execSQL(createView);

        return db.rawQuery("select * from AVGMARKSTUD_view", null);
    }

    public static void getCompareByGroups2(SQLiteDatabase db, String startDate, String endDate) {

    }
}
