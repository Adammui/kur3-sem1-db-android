package karbanovich.fit.bstu.students.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import karbanovich.fit.bstu.students.Database.DbHelper;

public class GroupProvider extends ContentProvider {
    static final String AUTHORITY = "com.karbanovich.providers.GroupsList";
    static final String PATH ="list";
    public static final Uri Group_Uri = Uri.parse("content://"+AUTHORITY+"/"+PATH);
    private DbHelper dbOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int GROUPS = 100;
    public static final int GROUPS_ID = 101;
    public static final int STUDENTS = 102;
    public static final int STUDENTS_ID = 103;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //  content://com.karbanovich.students.authority/GROUP
        matcher.addURI(AUTHORITY,PATH, GROUPS);
        //  content://com.karbanovich.students.authority/GROUP/ID
        matcher.addURI(StudentsContract.CONTENT_AUTHORITY, StudentsContract.GROUP_TABLE + "/#", GROUPS_ID);

        //  content://com.karbanovich.students.authority/STUDENT
        matcher.addURI(StudentsContract.CONTENT_AUTHORITY, StudentsContract.STUDENT_TABLE, STUDENTS);
        //  content://com.karbanovich.students.authority/STUDENT/ID
        matcher.addURI(StudentsContract.CONTENT_AUTHORITY, StudentsContract.STUDENT_TABLE + "/#", STUDENTS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = DbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        switch (match) {
            case GROUPS:
                return db.rawQuery("select [GROUP].IDGROUP,"
                                        + " [GROUP].HEAD,"
                                        + " count(STUDENT.IDSTUDENT)"
                                    + " from [GROUP] "
                                        + " left join STUDENT on [GROUP].IDGROUP = STUDENT.IDGROUP"
                                    + " group by [GROUP].IDGROUP;", null);
            case GROUPS_ID:
                long taskId = StudentsContract.getGroupId(uri);
                return db.rawQuery("select [GROUP].HEAD,"
                                        + " count(STUDENT.IDSTUDENT)"
                                    + " from [GROUP]"
                                        + " left join STUDENT on [GROUP].IDGROUP = STUDENT.IDGROUP"
                                    + " where [GROUP].IDGROUP = " + taskId
                                    + " group by [GROUP].IDGROUP;", null);
            case STUDENTS:
                return db.rawQuery("select * from STUDENT where IDGROUP = " + selection, null);
            case STUDENTS_ID:
                long taskStudId = StudentsContract.getStudentId(uri);
                return db.rawQuery("select NAME,"
                        + " avg(MARK)"
                        + " from STUDENT"
                        + " left join PROGRESS on PROGRESS.IDSTUDENT = STUDENT.IDSTUDENT"
                        + " where STUDENT.IDSTUDENT = " + taskStudId
                        + " group by STUDENT.IDSTUDENT;", null);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
       // Cursor cursor= db.query("[GROUP]",projection, selection, selectionArgs, null,null,sortOrder);
       // cursor.setNotificationUri(getContext().getContentResolver(),Group_Uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case GROUPS:
                return StudentsContract.CONTENT_GROUP_TYPE;
            case GROUPS_ID:
                return StudentsContract.CONTENT_GROUP_ITEM_TYPE;
            case STUDENTS:
                return StudentsContract.CONTENT_STUDENT_TYPE;
            case STUDENTS_ID:
                return StudentsContract.CONTENT_STUDENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Uri returnUri = Uri.parse("");
        long recordId;

        if(match == GROUPS) {
            recordId = db.insert("[GROUP]", null, values);

            if(recordId > 0)
                returnUri = StudentsContract.buildGroupUri(recordId);
            else
                throw new SQLException("Failed to insert: " + uri.toString());
        }

        if(match == STUDENTS) {
            recordId = db.insert("STUDENT", null, values);

            if(recordId > 0)
                returnUri = StudentsContract.buildStudentUri(recordId);
            else
                throw new SQLException("Failed to insert: " + uri.toString());
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if(match != GROUPS && match != GROUPS_ID && match != STUDENTS && match != STUDENTS_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if(match == GROUPS) {
            db.execSQL("delete from [GROUP];");
            return 0;
        }

       if(match == GROUPS_ID) {
           long taskId = StudentsContract.getGroupId(uri);
           selectionCriteria = StudentsContract.GroupColumns.IDGROUP + " = " + taskId;

           if ((selection != null) && (selection.length() > 0))
               selectionCriteria += " and (" + selection + ")";
       }

        if(match == STUDENTS) {
            db.execSQL("delete from STUDENT where IDGROUP = " + selection + ";");
            return 0;
        }

        if(match == STUDENTS_ID) {
            long taskId = StudentsContract.getStudentId(uri);
            db.execSQL("delete from STUDENT where IDSTUDENT = " + taskId + ";");
            return 0;
        }

       return db.delete("[GROUP]", selectionCriteria, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String selectionCriteria = selection;

        if(match != GROUPS && match != GROUPS_ID && match != STUDENTS && match != STUDENTS_ID)
            throw new IllegalArgumentException("Unknown URI: " + uri);

        if(match == GROUPS_ID) {
            long taskId = StudentsContract.getGroupId(uri);
            selectionCriteria = StudentsContract.GroupColumns.IDGROUP + " = " + taskId;

           if((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
           }
        }

        if(match == STUDENTS_ID) {
            long taskId = StudentsContract.getStudentId(uri);
            db.update("STUDENT", values, "IDSTUDENT = " + taskId, selectionArgs);
            return 0;
        }

        return db.update("[GROUP]", values, selectionCriteria, selectionArgs);
    }
}
