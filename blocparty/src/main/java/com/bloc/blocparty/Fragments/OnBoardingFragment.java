package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bloc.blocparty.R;

/**
 * OnBoarding Fragment
 */
public class OnBoardingFragment extends Fragment {

    private int mPage = 0;
    private static final String PREF_ONBOARDED = "com.blocparty.onboarded";

    public OnBoardingFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_onboarding, container, false);

        // get refs
        final ImageView image = (ImageView) v.findViewById(R.id.image_tutorial);
        Button skip = (Button) v.findViewById(R.id.btn_skip_tutorial);
        final Button next = (Button) v.findViewById(R.id.btn_next_tutorial);

        // set the image to the first tutorial page
        image.setImageResource(R.drawable.tutorial0);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishTutorial();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mPage) {

                    case 0:

                        image.setImageResource(R.drawable.tutorial1);
                        break;

                    case 1:

                        image.setImageResource(R.drawable.tutorial2);
                        break;

                    case 2:

                        image.setImageResource(R.drawable.tutorial3);
                        break;

                    case 3:

                        image.setImageResource(R.drawable.tutorial4);

                        // change the text of the button to 'finish'
                        next.setText(R.string.tutorial_btn_finish);
                        break;

                    case 4:

                        // set shared pref to true and go to feed fragment
                        finishTutorial();
                        break;

                }

                mPage++;
            }
        });

        return v;

    }

    private void finishTutorial() {

        // set the shared pref to true
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit().putBoolean(PREF_ONBOARDED, true).apply();

        // go to the feed fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new FeedFragment(), "FeedFragment")
                .commit();

    }
}
