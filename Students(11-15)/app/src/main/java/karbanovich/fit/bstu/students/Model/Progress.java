package karbanovich.fit.bstu.students.Model;

import java.time.LocalDate;

public class Progress {

    private int studentId;
    private int subjectId;
    private LocalDate date;
    private int mark;
    private String teacher;

    public Progress(int studentId, int subjectId, LocalDate date, int mark, String teacher) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.mark = mark;
        this.teacher = teacher;
    }

    public Progress() { }


    public int getStudentId() {return studentId;}
    public int getSubjectId() {return subjectId;}
    public LocalDate getDate() {return date;}
    public int getMark() {return mark;}
    public String getTeacher() {return teacher;}

    public void setStudentId(int studentId) {this.studentId = studentId;}
    public void setSubjectId(int subjectId) {this.subjectId = subjectId;}
    public void setDate(LocalDate date) {this.date = date;}
    public void setMark(int mark) {this.mark = mark;}
    public void setTeacher(String teacher) {this.teacher = teacher;}
}
