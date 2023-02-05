package karbanovich.fit.bstu.students.ContentProvider;

import android.content.ContentUris;
import android.net.Uri;

public class StudentsContract {

    //db tables
    static final String GROUP_TABLE = "GROUP";
    static final String STUDENT_TABLE = "STUDENT";

    //authority
    static final String CONTENT_AUTHORITY = "com.karbanovich.students.authority";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //content types
    static final String CONTENT_GROUP_TYPE =
            "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + GROUP_TABLE;
    static final String CONTENT_GROUP_ITEM_TYPE =
            "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + GROUP_TABLE;
    static final String CONTENT_STUDENT_TYPE =
            "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + STUDENT_TABLE;
    static final String CONTENT_STUDENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + STUDENT_TABLE;

    //table columns
    public static class GroupColumns {
        public static final String IDGROUP = "IDGROUP";
        public static final String COURSE = "COURSE";
        public static final String NAME = "NAME";
        public static final String HEAD = "HEAD";

        private GroupColumns() { }
    }
    public static class StudentColumns {
        public static final String IDSTUDENT = "IDSTUDENT";
        public static final String IDGROUP = "IDGROUP";
        public static final String NAME = "NAME";
        public static final String BIRTHDAY = "BIRTHDAY";
        public static final String ADDRESS = "ADDRESS";

        private StudentColumns() { }
    }

    //paths to access tables
    public static final Uri CONTENT_GROUP_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, GROUP_TABLE);
    public static final Uri CONTENT_STUDENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, STUDENT_TABLE);


    //return URI by id
    static Uri buildGroupUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_GROUP_URI, taskId);
    }
    static Uri buildStudentUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_STUDENT_URI, taskId);
    }

    //get id by URI
    static long getGroupId(Uri uri) {
        return ContentUris.parseId(uri);
    }
    static long getStudentId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
