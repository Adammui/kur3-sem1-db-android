package karbanovich.fit.bstu.students.Model;

public class Group {

    private int id;
    private int facultyId;
    private int course;
    private String name;
    private String head;

    public Group(int facultyId, int course, String name, String head) {
        this.facultyId = facultyId;
        this.course = course;
        this.name = name;
        this.head = head;
    }

    public Group() { }


    public int getId() {return id;}
    public int getFacultyId() {return facultyId;}
    public int getCourse() {return course;}
    public String getName() {return name;}
    public String getHead() {return head;}

    public void setId(int id) {this.id = id;}
    public void setFacultyId(int facultyId) {this.facultyId = facultyId;}
    public void setCourse(int course) {this.course = course;}
    public void setName(String name) {this.name = name;}
    public void setHead(String headman) {this.head = headman;}
}
