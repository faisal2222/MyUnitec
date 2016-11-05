package unitec.ac.nz.myunitec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mmahmoud on 5/11/16.
 */

public class GradeAdapter extends ArrayAdapter<Grade> {
    public GradeAdapter(Context contex, ArrayList<Grade> programmes) {
        super(contex, 0, programmes);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Grade grade = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grade, parent, false);
        }

        TextView txtAssesment = (TextView) convertView.findViewById(R.id.txtAssesment);
        TextView txtGrade = (TextView) convertView.findViewById(R.id.txtGrade);

        txtAssesment.setText(grade.assesment);
        txtGrade.setText(grade.grade);

        return convertView;
    }
}
