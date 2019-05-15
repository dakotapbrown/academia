package edu.vcu.eythirteenapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import edu.vcu.eythirteenapp.R;

public class LabelEditDialogFragment extends DialogFragment {
	
	public static final int LABEL_EDIT_VIEW_ID = View.generateViewId();
	private DialogListener mListener;
	
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final EditText input = new EditText(getActivity());
		input.setId(LABEL_EDIT_VIEW_ID);
		input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		
		return (new AlertDialog.Builder(getActivity()))
				       .setTitle(R.string.labelEditDialog)
				       .setPositiveButton(R.string.submitButton, mListener::onDialogButtonClick)
				       .setNegativeButton(R.string.cancelButton, (dialog, which) -> dismiss())
				       .setCancelable(true)
				       .setView(input)
				       .create();
	}
	
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mListener = (DialogListener) context;
		} catch (ClassCastException cce) {
			throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
		}
	}
}
