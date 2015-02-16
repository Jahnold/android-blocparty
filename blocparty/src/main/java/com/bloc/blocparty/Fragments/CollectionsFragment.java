package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bloc.blocparty.Adapters.CollectionItemAdapter;
import com.bloc.blocparty.Models.Collection;
import com.bloc.blocparty.Models.CollectionFactory;
import com.bloc.blocparty.R;

import java.util.ArrayList;

/**
 *  Collections Fragment
 */
public class CollectionsFragment extends Fragment {

    private static final int ADD = 0;
    private static final int LOAD = 1;

    private CollectionItemAdapter mAdapter;

    private int action = ADD;

    public void setAction(int action) {
        this.action = action;
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

                // depends on whether we are loading a collection or adding a friend to a collection
                if (action == ADD) {


                }
                else {

                }

            }
        });

        return v;

    }
}
