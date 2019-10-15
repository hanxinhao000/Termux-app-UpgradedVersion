package main.java.com.termux.filemanage.filemanager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.termux.R;

import java.util.List;

import main.java.com.termux.filemanage.filemanager.files.FileHolder;
import main.java.com.termux.filemanage.filemanager.view.CheckableFileListItem;
import main.java.com.termux.filemanage.filemanager.view.ViewHolder;

/**
 * Extension of {@link FileHolderListAdapter} that displays checkable items.
 *
 * @author George Venios.
 */
public class MultiselectFileHolderListAdapter extends FileHolderListAdapter {
    public MultiselectFileHolderListAdapter(List<FileHolder> files, Context c) {
        super(files, c, R.layout.item_filelist_multiselect);
    }

    @Override
    protected View newView() {
        View view = new CheckableFileListItem(getContext());

        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) view.findViewById(R.id.icon);
        holder.primaryInfo = (TextView) view.findViewById(R.id.primary_info);
        holder.secondaryInfo = (TextView) view.findViewById(R.id.secondary_info);
        holder.tertiaryInfo = (TextView) view.findViewById(R.id.tertiary_info);

        view.setTag(holder);
        return view;
    }
}
