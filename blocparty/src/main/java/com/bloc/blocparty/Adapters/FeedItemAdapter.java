package com.bloc.blocparty.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bloc.blocparty.Models.ImageHandler;
import com.bloc.blocparty.Models.SocialItem;
import com.bloc.blocparty.R;

import java.util.ArrayList;

/**
 * Created by matthewarnold on 16/01/15.
 */
public class FeedItemAdapter extends ArrayAdapter<SocialItem> {

    // working copy of the feed
    private ArrayList<SocialItem> mFeed;
    private ImageHandler mImageHandler;

    // constructor
    public FeedItemAdapter(Context context, int textViewResourceId, ArrayList<SocialItem> items) {

        super(context, textViewResourceId, items);
        this.mFeed = items;
        this.mImageHandler = new ImageHandler(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check whether the view needs inflating (it may be recycled)
        if (convertView == null) {

            // inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_feed_item, null);

        }

        // get the SocialItem at [position] from the array list
        final SocialItem item = mFeed.get(position);

        // get refs to the interface controls
        final ImageView profilePic = (ImageView) convertView.findViewById(R.id.feed_profile_pic);
        final ImageView mainImage = (ImageView) convertView.findViewById(R.id.feed_main_image);
        TextView name = (TextView) convertView.findViewById(R.id.feed_name);
        TextView comment = (TextView) convertView.findViewById(R.id.feed_comment);
        ImageButton likeBtn = (ImageButton) convertView.findViewById(R.id.feed_like_btn);
        ImageButton menuBtn = (ImageButton) convertView.findViewById(R.id.feed_popup_menu_btn);

        if (item != null) {

            // transfer details from the social item to the view
            name.setText(item.getUserName());
            comment.setText(item.getComment());

            // get the profile picture
            mImageHandler.loadImage(
                    item.getUserId(),
                    item.getUserProfilePicLink(),
                    new ImageHandler.ImageHandlerListener() {
                        @Override
                        public void onImageLoaded(Bitmap image) {
                            profilePic.setImageBitmap(image);
                        }
                    }
            );

            // get the actual picture
            mImageHandler.loadImage(
                    item.getUniqueId(),
                    item.getImageLink(),
                    new ImageHandler.ImageHandlerListener() {
                        @Override
                        public void onImageLoaded(Bitmap image) {
                            mainImage.setImageBitmap(image);
                        }
                    }
            );

            // set the like button picture depending on whether the item is liked or not
            if (item.isLike()) {
                likeBtn.setBackgroundResource(android.R.drawable.btn_star_big_on);
            }
            else {
                likeBtn.setBackgroundResource(android.R.drawable.btn_star_big_off);
            }

            // set up a listener for the like button
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    item.toggleLike();
                }
            });

            // set up the popup menu
            menuBtn.setFocusable(false);
            final PopupMenu popupMenu = new PopupMenu(getContext(), menuBtn);
            popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, getContext().getString(R.string.menu_save));
            popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, getContext().getString(R.string.menu_share));
            popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, getContext().getString(R.string.menu_collection));

            // set up the listener for the menu items
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case 0:
                            // save image
                            break;
                        case 1:
                            // share image
                            break;
                        case 2:
                            // add user to collection
                            break;
                    }
                    return false;
                }
            });

            // set up a listener for the actual menu button
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();;
                }
            });

        }

        return convertView;
    }
}
