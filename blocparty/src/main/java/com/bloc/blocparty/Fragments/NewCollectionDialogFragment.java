package com.bloc.blocparty.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.bloc.blocparty.R;

/**
 *  New Collection Dialog
 */
public class NewCollectionDialogFragment extends DialogFragment {

    private NewCollectionListener mListener;

    public NewCollectionDialogFragment() {}


    public interface NewCollectionListener {
        public void onNewCollectionConfirm(DialogFragment dialog, String name);
    }

    public void setListener(NewCollectionListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // get builder and inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // inflate and set the view for the dialog then add the title
        builder.setView(inflater.inflate(R.layout.fragment_new_collection_dialog,null));
        builder.setTitle(R.string.new_collection_title);

        // setup the buttons
        builder.setPositiveButton(R.string.new_collection_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // get the name of the new collection
                EditText editText = (EditText) getDialog().findViewById(R.id.et_new_collection_name);

                // call the listener confirm method
                mListener.onNewCollectionConfirm(NewCollectionDialogFragment.this, editText.getText().toString());

            }
        });

        builder.setNegativeButton(R.string.new_collection_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // user clicked cancel, do nothing
            }
        });

        return builder.create();

    }
}
