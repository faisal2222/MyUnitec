package unitec.ac.nz.myunitec;

import java.io.Serializable;

/**
 * Created by mmahmoud on 5/11/16.
 */

public class Grade implements Serializable{

    public String assesment;
    public String username;
    public String moduleid;
    public String semester;
    public String year;
    public String grade;

    public Grade(String assesment, String username, String moduleid, String semester, String year, String grade) {
        this.assesment = assesment;
        this.username = username;
        this.moduleid = moduleid;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
    }
}
