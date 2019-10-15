package main.java.com.termux.filemanage.filemanager.bookmarks;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


import com.termux.R;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import main.java.com.termux.filemanage.filemanager.compatibility.BookmarkListActionHandler;
import main.java.com.termux.filemanage.filemanager.compatibility.BookmarkMultiChoiceModeHelper;

/**
 * @author George Venios
 */
public class BookmarkListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new BookmarkListAdapter((FragmentActivity) getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set list properties
        getListView().setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    ((BookmarkListAdapter) getListAdapter()).setScrolling(false);
                } else
                    ((BookmarkListAdapter) getListAdapter()).setScrolling(true);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        getListView().requestFocus();
        getListView().requestFocusFromTouch();

        setEmptyText(getString(R.string.bookmark_empty));

        // Handle item selection.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(getListView());
        } else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            BookmarkMultiChoiceModeHelper.listView_setMultiChoiceModeListener(getListView(), getActivity());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        BookmarkListActionHandler.handleItemSelection(item, getListView());
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        MenuInflater inf = new MenuInflater(getActivity());
        inf.inflate(R.menu.bookmarks, menu);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String path = ((BookmarkListAdapter.Bookmark) getListAdapter().getItem(position)).path;
        ((BookmarkListActivity) getActivity()).onListItemClick(path);
    }
}
