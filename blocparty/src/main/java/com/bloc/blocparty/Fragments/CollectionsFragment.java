package com.bloc.blocparty.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bloc.blocparty.Adapters.CollectionItemAdapter;
import com.bloc.blocparty.Models.Collection;
import com.bloc.blocparty.Models.CollectionFactory;
import com.bloc.blocparty.Models.Friend;
import com.bloc.blocparty.R;

import java.util.ArrayList;

/**
 *  Collections Fragment
 */
public class CollectionsFragment extends Fragment {

    public static final int ADD = 0;
    public static final int LOAD = 1;

    private CollectionItemAdapter mAdapter;
    private Friend mFriend;
    private int action = LOAD;


    public void setAction(int action) {
        this.action = action;
    }
    public void setFriend(Friend friend) {
        mFriend = friend;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view and get a ref to the list view
        View v = inflater.inflate(R.layout.fragment_collections, null);
        ListView collectionsList = (ListView) v.findViewById(R.id.collections_list);

        // load all the collections from the db
        ArrayList<Collection> collections = CollectionFactory.getAll();

        // create the adapter and set it to our list view
        mAdapter = new CollectionItemAdapter(
                getActivity(),
                0,
                collections
        );
        collectionsList.setAdapter(mAdapter);

        // setup the click listener for the list items
        collectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the collection that was clicked on
                Collection collection =  mAdapter.getItem(position);

                // get a ref to the feed fragment
                FeedFragment feedFragment = (FeedFragment) getFragmentManager().findFragmentByTag("FeedFragment");

                // depends on whether we are loading a collection or adding a friend to a collection
                if (action == ADD) {

                    // set the collection id and save
                    mFriend.setCollectionId(collection.getId());
                    mFriend.save();

                    // show a toast to confirm action
                    Toast.makeText(getActivity(),R.string.toast_added_to_collection,Toast.LENGTH_SHORT).show();

                }
                else {

                    // pass the feed fragment the collection and set it to load collection
                    feedFragment.setCollection(collection);
                    feedFragment.setFeedType(FeedFragment.LOAD_COLLECTION);
                    feedFragment.clearFeed();

                }

                // load the feed fragment into view
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, feedFragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // if it's not already loaded the inflate the menu for the 'add collection' button
        if (menu.findItem(R.id.action_new_collection) == null) {

            inflater.inflate(R.menu.collections,menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_new_collection) {

            // create a new collection dialog and set the listener
            NewCollectionDialogFragment dialog = new NewCollectionDialogFragment();
            dialog.setListener(new NewCollectionDialogFragment.NewCollectionListener() {
                @Override
                public void onNewCollectionConfirm(DialogFragment dialog, String name) {

                    // create a new collection
                    Collection newCollection = new Collection();
                    newCollection.setName(name);
                    newCollection.save();
                    mAdapter.add(newCollection);

                }
            });

            // show our dialog
            dialog.show(getFragmentManager(),"NewCollectionDialog");

            return true;

        }
        else {

            return super.onOptionsItemSelected(item);

        }

    }
}
