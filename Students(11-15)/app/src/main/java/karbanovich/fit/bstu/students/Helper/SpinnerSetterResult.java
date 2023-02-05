package karbanovich.fit.bstu.students.Helper;

import java.util.ArrayList;

public class SpinnerSetterResult<T> {

    private ArrayList<T> list;
    private String[] str;

    public SpinnerSetterResult(ArrayList<T> list, String[] str) {
        this.list = list;
        this.str = str;
    }

    public ArrayList<T> getList() {return list;}
    public String[] getStr() {return str;}
}
