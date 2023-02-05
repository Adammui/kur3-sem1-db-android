package karbanovich.fit.bstu.students.Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Student implements Serializable {

    private int id;
    private int groupId;
    private String name;
    private LocalDate birthday;
    private String address;

    public Student(int groupId, String name, LocalDate birthday, String address) {
        this.groupId = groupId;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
    }

    public Student() { }


    public int getId() {return id;}
    public int getGroupId() {return groupId;}
    public String getName() {return name;}
    public LocalDate getBirthday() {return birthday;}
    public String getAddress() {return address;}

    public void setId(int id) {this.id = id;}
    public void setGroupId(int groupId) {this.groupId = groupId;}
    public void setName(String name) {this.name = name;}
    public void setBirthday(LocalDate birthday) {this.birthday = birthday;}
    public void setAddress(String address) {this.address = address;}
}
