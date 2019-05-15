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
import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.math.Expression;
import edu.vcu.eythirteenapp.math.ExpressionRandomizer;

public class MathQuestionDialogFragment extends DialogFragment {
	
	private static final int SOLUTION_TEXT_VIEW_ID = View.generateViewId();
	private DialogListener mListener;
	private Expression mathExpression;
	
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final EditText solution = new EditText(getActivity());
		solution.setId(SOLUTION_TEXT_VIEW_ID);
		solution.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		solution.setShowSoftInputOnFocus(true);
		String expression = generateExpression();
		
		
		return (new AlertDialog.Builder(getActivity()))
				       .setTitle(R.string.mathQuestionDialog)
				       .setMessage(expression)
				       .setView(solution)
				       .setPositiveButton(R.string.okayButton,
				                          (dialog, which) -> {
					                          double answer = Double.parseDouble(
							                          solution.getText().toString());
					
					                          if (mathExpression.isSolution(answer)) {
						                          mListener.onDialogButtonClick(dialog, which);
					                          } else {
						                          mListener.onDialogButtonClick(dialog,
						                                                        Dialog.BUTTON_NEGATIVE);
					                          }
				                          })
				       .setCancelable(false)
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
	
	
	private String generateExpression() {
		Bundle args = getArguments();
		assert args != null;
		Alarm alarm = args.getParcelable("alarm");
		assert alarm != null;
		int expressionDifficulty = alarm.getDifficulty();
		
		mathExpression = ExpressionRandomizer.newExpression(expressionDifficulty);
		return mathExpression.getInfixExpression();
	}
	
}
