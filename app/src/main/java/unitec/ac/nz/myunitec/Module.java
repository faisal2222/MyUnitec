package unitec.ac.nz.myunitec;

import java.io.Serializable;

/**
 * Created by Faisal on 11/10/2016.
 */
public class Module implements Serializable {

    public String id;
    public String name;
    public String semester;
    public String year;
    public String status;

    public Module(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Module(String id, String name, String semester, String year, String status) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.status = status;
    }
}
