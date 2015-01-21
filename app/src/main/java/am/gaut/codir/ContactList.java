package am.gaut.codir;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

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
        new DownloadImageTask((ImageView) rowView.findViewById(R.id.imgThumb)).execute(thumbs[p]);

        return rowView;
    }

    /**
     * Capitalize first letters of a name, join and return
     * @param fname
     * @param lname
     * @return
     */
    public static String getFullName(String fname, String lname) {
        fname = fname.substring(0,1).toUpperCase() + fname.substring(1);
        lname = lname.substring(0,1).toUpperCase() + lname.substring(1);

        return fname + " " + lname;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mThumb;

        public DownloadImageTask(ImageView mThumb) {
            this.mThumb = mThumb;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mThumb.setImageBitmap(result);
        }
    }
}
