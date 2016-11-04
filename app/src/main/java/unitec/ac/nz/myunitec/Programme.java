package unitec.ac.nz.myunitec;

import java.io.Serializable;

/**
 * Created by Faisal on 22/09/2016.
 */
public class Programme implements Serializable {

    public String id;
    public String name;
    public String enrollmentDate;

    public Programme(String id, String name, String enrollmentDate) {
        this.id = id;
        this.name = name;
        this.enrollmentDate = enrollmentDate;
    }
}
