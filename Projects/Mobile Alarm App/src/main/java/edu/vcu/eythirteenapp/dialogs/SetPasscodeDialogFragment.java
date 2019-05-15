package edu.vcu.eythirteenapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import edu.vcu.eythirteenapp.R;

public class SetPasscodeDialogFragment extends DialogFragment {
	@IdRes
	public static final int PASSCODE_EDIT_VIEW_ID = View.generateViewId();
	private DialogListener mListener;
	
	
	@NonNull
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final EditText input = new EditText(getActivity());
		input.setId(PASSCODE_EDIT_VIEW_ID);
		input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
		input.setShowSoftInputOnFocus(true);
		input.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(4) });
		
		return (new AlertDialog.Builder(getActivity()))
				       .setTitle(R.string.passcodeDialog)
				       .setPositiveButton(R.string.submitButton, mListener::onDialogButtonClick)
				       .setNegativeButton(R.string.cancelButton,  mListener::onDialogButtonClick)
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
