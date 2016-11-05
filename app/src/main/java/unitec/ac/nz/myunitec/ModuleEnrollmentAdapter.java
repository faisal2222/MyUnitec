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

public class ModuleEnrollmentAdapter extends ArrayAdapter<Module> {

    public ModuleEnrollmentAdapter(Context contex, ArrayList<Module> modules) {
        super(contex, 0, modules);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Module module = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enrollment_module, parent, false);
        }

        TextView txtModuleID = (TextView) convertView.findViewById(R.id.txtModuleID);
        TextView txtModuleName = (TextView) convertView.findViewById(R.id.txtModuleName);

        txtModuleID.setText(module.id);
        txtModuleName.setText(module.name);

        return convertView;
    }
}
