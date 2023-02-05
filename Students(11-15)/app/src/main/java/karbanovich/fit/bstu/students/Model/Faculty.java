package karbanovich.fit.bstu.students.Model;

import java.time.LocalTime;

public class Faculty {

    private int id;
    private String name;
    private String dean;
    private LocalTime startWork;
    private LocalTime endWork;

    public Faculty(String name, String dean, LocalTime startWork, LocalTime endWork) {
        this.name = name;
        this.dean = dean;
        this.startWork = startWork;
        this.endWork = endWork;
    }

    public Faculty() { }


    public int getId() {return id;}
    public String getName() {return name;}
    public String getDean() {return dean;}
    public LocalTime getStartWork() {return startWork;}
    public LocalTime getEndWork() {return endWork;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setDean(String dean) {this.dean = dean;}
    public void setStartWork(LocalTime startWork) {this.startWork = startWork;}
    public void setEndWork(LocalTime endWork){this.endWork = endWork;}
}
