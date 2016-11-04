package unitec.ac.nz.myunitec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Faisal on 22/09/2016.
 */
public class ProgrammesAdapter extends ArrayAdapter<Programme> {

    public ProgrammesAdapter(Context contex, ArrayList<Programme> programmes) {
        super(contex, 0, programmes);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Programme programme = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_programme, parent, false);
        }

        TextView txtProgrammeID = (TextView) convertView.findViewById(R.id.txtProgrammeID);
        TextView txtProgrammeName = (TextView) convertView.findViewById(R.id.txtProgrammeName);
        TextView txtProgrammeEnrollmentDate = (TextView) convertView.findViewById(R.id.txtEnrollmentDate);

        txtProgrammeID.setText(programme.id);
        txtProgrammeName.setText(programme.name);
        txtProgrammeEnrollmentDate.setText(programme.enrollmentDate);

        return convertView;
    }
}
