package main.java.com.termux.filemanage.filemanager.lists;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.termux.R;


import java.io.File;

import main.java.com.termux.filemanage.filemanager.FileManagerProvider;
import main.java.com.termux.filemanage.filemanager.PreferenceActivity;
import main.java.com.termux.filemanage.filemanager.files.FileHolder;
import main.java.com.termux.filemanage.filemanager.lists.SimpleFileListFragment;
import main.java.com.termux.filemanage.filemanager.view.PickBar;
import main.java.com.termux.filemanage.intents.FileManagerIntents;

public class PickFileListFragment extends SimpleFileListFragment {
    private PickBar mPickBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLongClickMenus(R.menu.context_pick, R.menu.multiselect);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filelist_pick, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewFlipper modeSelector = (ViewFlipper) view.findViewById(R.id.modeSelector);

        // Folder init
        if (getArguments().getBoolean(FileManagerIntents.EXTRA_DIRECTORIES_ONLY)) {
            modeSelector.setDisplayedChild(0);

            Button button = (Button) view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickFileOrFolder(new File(getPath()), false);
                }
            });
            if (getArguments().containsKey(FileManagerIntents.EXTRA_BUTTON_TEXT)) {
                button.setText(getArguments().getString(FileManagerIntents.EXTRA_BUTTON_TEXT));
            }
        }
        // Files init
        else {
            modeSelector.setDisplayedChild(1);

            mPickBar = (PickBar) view.findViewById(R.id.pickBar);
            mPickBar.setButtonText(getArguments().getString(FileManagerIntents.EXTRA_BUTTON_TEXT));

            mPickBar.setText(getFilename());
            // TODO make file visible, i.e. scroll to item

            mPickBar.setOnPickRequestedListener(new PickBar.OnPickRequestedListener() {
                @Override
                public void pickRequested(String filename) {
                    if (filename.trim().length() == 0) {
                        Toast.makeText(getActivity(), R.string.choose_filename, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Pick
                    pickFileOrFolder(new File(getPath() + (getPath().endsWith("/") ? "" : "/") + filename),
                            getArguments().getBoolean(FileManagerIntents.EXTRA_IS_GET_CONTENT_INITIATED, false));
                }
            });

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileHolder item = (FileHolder) mAdapter.getItem(position);

        if (item.getFile().isFile()) {
            mPickBar.setText(item.getName());
          //  Toast.makeText(getContext(), "1234567", Toast.LENGTH_SHORT).show();
        }
        else
            super.onListItemClick(l, v, position, id);
    }

    /**
     * Act upon picking.
     *
     * @param selection           A {@link File} representing the user's selection.
     * @param getContentInitiated Whether the fragment was called through a GET_CONTENT intent on the IntentFilterActivity. We have to know this so that result is correctly formatted.
     */
    private void pickFileOrFolder(File selection, boolean getContentInitiated) {
        Intent intent = new Intent();
        intent.putExtras(getArguments());

       // Toast.makeText(getContext(), "1234567", Toast.LENGTH_SHORT).show();
        PreferenceActivity.setDefaultPickFilePath(getActivity(), selection.getParent() != null ? selection.getParent() : "/");

        if (getContentInitiated)
            intent.setData(Uri.parse(FileManagerProvider.FILE_PROVIDER_PREFIX + selection));
        else
            intent.setData(Uri.fromFile(selection));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
