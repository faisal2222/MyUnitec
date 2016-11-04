package unitec.ac.nz.myunitec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Faisal on 11/10/2016.
 */
public class ModuleAdpater extends ArrayAdapter<Module> {

    public ModuleAdpater(Context contex, ArrayList<Module> modules) {
        super(contex, 0, modules);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Module module = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_module, parent, false);
        }

        TextView txtModuleID = (TextView) convertView.findViewById(R.id.txtModuleID);
        TextView txtModuleName = (TextView) convertView.findViewById(R.id.txtModuleName);
        TextView txtSemester = (TextView) convertView.findViewById(R.id.txtSemester);
        TextView txtYear = (TextView) convertView.findViewById(R.id.txtYear);
        TextView txtGrade = (TextView) convertView.findViewById(R.id.txtGrade);
        TextView txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);

        txtModuleID.setText(module.id);
        txtModuleName.setText(module.name);
        txtSemester.setText(module.semester);
        txtYear.setText(module.year);
        txtGrade.setText(module.grade);
        txtStatus.setText(module.status);

        return convertView;
    }
}
