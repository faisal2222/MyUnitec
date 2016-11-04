package unitec.ac.nz.myunitec;

/**
 * Created by Faisal on 11/10/2016.
 */
public class Module {

    public String id;
    public String name;
    public String semester;
    public String year;
    public String grade;
    public String status;

    public Module(String id, String name, String semester, String year, String grade, String status) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
        this.status = status;
    }
}
