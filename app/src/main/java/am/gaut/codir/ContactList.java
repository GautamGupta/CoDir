package am.gaut.codir;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactList extends ArrayAdapter<String> {
    public static final String TAG = "CoDirMainCL";

    private final Activity mContext;

    private final String[] fnames;
    private final String[] lnames;
    private final String[] cells;
    private final String[] thumbs;

    public ContactList(Activity mContext, String[] fnames, String[] lnames, String[] cells, String[] thumbs) {
        super(mContext, R.layout.contact_list_row, fnames);

        this.mContext = mContext;

        this.fnames = fnames;
        this.lnames = lnames;
        this.cells  = cells;
        this.thumbs = thumbs;
    }

    @Override
    public View getView(int p, View view, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.contact_list_row, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
        TextView txtCell = (TextView) rowView.findViewById(R.id.txtCell);
        ImageView imgThumb = (ImageView) rowView.findViewById(R.id.imgThumb);

        txtName.setText(getFullName(fnames[p], lnames[p]));
        txtCell.setText(cells[p]);
        new DownloadImageTask(imgThumb).execute(thumbs[p]);

        return rowView;
    }

    /**
     * Capitalize first letters of a name, join and return
     * @param fname
     * @param lname
     * @return
     */
    public static String getFullName(String fname, String lname) {
        return capitalizeFirstLetter(fname + " " + lname);
    }

    public static String capitalizeFirstLetter(String source) {
        StringBuffer res = new StringBuffer();

        String[] strArr = source.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }

        return res.toString().trim();
    }
}

