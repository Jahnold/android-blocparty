package com.bloc.blocparty.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.blocparty.Models.Collection;
import com.bloc.blocparty.Models.ImageHandler;
import com.bloc.blocparty.R;

import java.util.ArrayList;

/**
 *  Collection Item Adapter
 */
public class CollectionItemAdapter extends ArrayAdapter<Collection> {


    // working copy of the feed
    private ArrayList<Collection> mCollections;

    private ImageHandler mImageHandler = new ImageHandler(getContext());

    // constructor
    public CollectionItemAdapter(Context context, int textViewResourceId, ArrayList<Collection> items) {

        super(context, textViewResourceId, items);
        this.mCollections = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check whether the view needs inflating (it may be recycled)
        if (convertView == null) {

            convertView =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_collections_item, null);

        }

        // get the collection at position
        Collection collection = mCollections.get(position);

        if (collection != null) {

            // get refs to all the items
            TextView name = (TextView) convertView.findViewById(R.id.coll_text_name);
            TextView count = (TextView) convertView.findViewById(R.id.coll_text_count);
            ImageView profile1 = (ImageView) convertView.findViewById(R.id.coll_profile_1);
            ImageView profile2 = (ImageView) convertView.findViewById(R.id.coll_profile_2);
            ImageView profile3 = (ImageView) convertView.findViewById(R.id.coll_profile_3);
            ImageView profile4 = (ImageView) convertView.findViewById(R.id.coll_profile_4);

            int collSize = collection.getFriends().size();

            // set values from the collection
            name.setText(collection.getName());
            count.setText(String.valueOf(collSize) + " Users");

            // set the four profile pictures
            if (collSize > 0) {
                setProfilePicture(profile1, 0, collection);
            }
            if (collSize > 1) {
                setProfilePicture(profile2, 1, collection);
            }
            if (collSize > 2) {
                setProfilePicture(profile3, 2, collection);
            }
            if (collSize > 3) {
                setProfilePicture(profile4, 3, collection);
            }

        }

        return convertView;
    }

    private void setProfilePicture(final ImageView imageView, int position, Collection collection) {

        if (collection.getFriends().get(position) != null) {

            mImageHandler.loadImage(
                    collection.getFriends().get(position).getUniqueId(),
                    "",
                    new ImageHandler.ImageHandlerListener() {
                        @Override
                        public void onImageLoaded(Bitmap image) {
                            imageView.setImageBitmap(image);
                        }
                    }
            );

        }

    }

}
