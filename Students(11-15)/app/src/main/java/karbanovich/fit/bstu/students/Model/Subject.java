package karbanovich.fit.bstu.students.Model;

public class Subject {

    private int id;
    private String name;

    public Subject(String name) {
        this.name  = name;
    }

    public Subject() { }


    public int getId() {return id;}
    public String getName() {return name;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
