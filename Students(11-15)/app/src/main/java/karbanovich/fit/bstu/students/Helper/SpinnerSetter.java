package karbanovich.fit.bstu.students.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.util.ArrayList;

import karbanovich.fit.bstu.students.Database.DbHelper;
import karbanovich.fit.bstu.students.Database.FacultyDb;
import karbanovich.fit.bstu.students.Database.GroupDb;
import karbanovich.fit.bstu.students.Database.StudentDb;
import karbanovich.fit.bstu.students.Database.SubjectDb;
import karbanovich.fit.bstu.students.Model.Faculty;
import karbanovich.fit.bstu.students.Model.Group;
import karbanovich.fit.bstu.students.Model.Student;
import karbanovich.fit.bstu.students.Model.Subject;

public class SpinnerSetter {

    private static SQLiteDatabase db;


    public static SpinnerSetterResult<Faculty> setFaculties(Context context) {
        db = new DbHelper(context).getReadableDatabase();
        ArrayList<Faculty> listFaculties = new ArrayList<>();
        String[] strFaculties;
        Cursor cursor = FacultyDb.getAll(db);

        if(cursor.getCount() == 0) {
            strFaculties = new String[] { "-" };
            return new SpinnerSetterResult<>(listFaculties, strFaculties);
        }

        strFaculties = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            Faculty faculty = new Faculty();

            faculty.setId(cursor.getInt(cursor.getColumnIndexOrThrow("IDFACULTY")));
            faculty.setName(cursor.getString(cursor.getColumnIndexOrThrow("FACULTY")));

            listFaculties.add(i, faculty);
            strFaculties[i++] = faculty.getId() + " " + faculty.getName();
        }
        cursor.close();
        db.close();
        return new SpinnerSetterResult<>(listFaculties, strFaculties);
    }

    public static SpinnerSetterResult<Group> setGroups(Context context) {
        db = new DbHelper(context).getReadableDatabase();
        ArrayList<Group> listGroups = new ArrayList<>();
        String[] strGroups;
        Cursor cursor = GroupDb.getAll(db);

        if(cursor.getCount() == 0)
            return new SpinnerSetterResult<>(listGroups, new String[] { "-" });

        strGroups = new String[cursor.getCount()];
        int i = 0;

        while(cursor.moveToNext()) {
            Group group = new Group();

            group.setId(cursor.getInt(cursor.getColumnIndexOrThrow("IDGROUP")));
            group.setName(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            group.setCourse(cursor.getInt(cursor.getColumnIndexOrThrow("COURSE")));

            listGroups.add(i, group);
            strGroups[i++] = group.getId() + " " + group.getName() + "-" + group.getCourse();
        }
        cursor.close();
        db.close();
        return new SpinnerSetterResult<>(listGroups, strGroups);
    }

    public static SpinnerSetterResult<Student> setStudents(Context context, int listGroupsSize, int listGroupSelectedId) {
        db = new DbHelper(context).getReadableDatabase();
        ArrayList<Student> listStudentsByGroup = new ArrayList<>();
        String[] strStudentsByGroup;

        if(listGroupsSize == 0)
            return new SpinnerSetterResult<>(listStudentsByGroup, new String[] { "-" });

        Cursor cursor = StudentDb.getStudentsByGroupId(db, listGroupSelectedId);

        if(cursor.getCount() == 0)
            return new SpinnerSetterResult<>(listStudentsByGroup, new String[] { "-" });

        strStudentsByGroup = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            Student student = new Student();

            student.setId(cursor.getInt(cursor.getColumnIndexOrThrow("IDSTUDENT")));
            student.setGroupId(cursor.getInt(cursor.getColumnIndexOrThrow("IDGROUP")));
            student.setName(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            student.setBirthday(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("BIRTHDAY"))));
            student.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS")));

            listStudentsByGroup.add(i, student);
            strStudentsByGroup[i++] = student.getId() + " " + student.getName();
        }
        cursor.close();
        db.close();
        return new SpinnerSetterResult<>(listStudentsByGroup, strStudentsByGroup);
    }

    public static SpinnerSetterResult<Subject> setSubjects(Context context) {
        db = new DbHelper(context).getReadableDatabase();
        ArrayList<Subject> listSubjects = new ArrayList<>();
        String[] strSubjects;
        Cursor cursor = SubjectDb.getAll(db);

        if(cursor.getCount() == 0)
            return new SpinnerSetterResult<>(listSubjects, new String[] { "-" });

        strSubjects = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            Subject subject = new Subject();

            subject.setId(cursor.getInt(cursor.getColumnIndexOrThrow("IDSUBJECT")));
            subject.setName(cursor.getString(cursor.getColumnIndexOrThrow("SUBJECT")));

            listSubjects.add(i, subject);
            strSubjects[i++] = subject.getId() + " " + subject.getName();
        }
        cursor.close();
        db.close();
        return new SpinnerSetterResult<>(listSubjects, strSubjects);
    }
}
