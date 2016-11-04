package unitec.ac.nz.myunitec;

import java.io.Serializable;

/**
 * Created by mmahmoud on 5/11/16.
 */

public class Grade implements Serializable{

    public String assement;
    public String username;
    public String moduleid;
    public String grade;

    public Grade(String assement, String username, String moduleid, String grade) {
        this.assement = assement;
        this.username = username;
        this.moduleid = moduleid;
        this.grade = grade;
    }
}
