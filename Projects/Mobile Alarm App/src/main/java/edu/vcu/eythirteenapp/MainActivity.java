package edu.vcu.eythirteenapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.database.AlarmDatabaseManager;
import edu.vcu.eythirteenapp.database.CustomCursorAdapter;

/**
 * The {@code MainActivity} class is the navigational hub of this application. Its purpose is to
 * display to the user the list of {@code Alarm} objects that they have created, provide navigational
 * access to the {@code AlarmEditActivity} (where existing alarms can be edited and new ones
 * created), as well as the option to activate/deactivate any existing alarms.
 *
 * @author Dakota Brown
 */
public class MainActivity extends FragmentActivity {
	
	// ListView that contains alarms
	private ListView mListView;
	private CustomCursorAdapter mAdapter;
	private AlarmDatabaseManager mDatabaseManager;
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Called when the activity is starting. Inflates the layout of this {@code MainActivity} and populates
	 * the {@code ListView} with the appropriate {@code Alarm}s stored in a SQL database.
	 *
	 * @param savedInstanceState
	 * 		If the activity is being re-initialized after previously being shut down then this Bundle
	 * 		contains
	 * 		the data it most recently supplied in {@link #onSaveInstanceState}; otherwise, it is null.
	 * @see #onSaveInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = findViewById(R.id.mainActivityList);    // Get reference to ListView
		
		mDatabaseManager = new AlarmDatabaseManager(this);
		
		/* Populate mAdapter with SQL table data and notify ListView of data population */
		Cursor cursor = mDatabaseManager.fetch();
		mAdapter = new CustomCursorAdapter(this, cursor);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	
	public void onClickAdd(View view) {
		this.startActivity(new Intent(this, AlarmEditActivity.class));
	}
	
	
	public void onClickEdit(View view) {
		TextView editButton = findViewById(R.id.editButton);
		
		if (view.isSelected()) {
			editButton.setText(R.string.edit_button_text);    // Revert EditButton text to "Edit"
			mListView.setOnItemClickListener(null); // Do nothing
			view.setSelected(false);
		} else {
			editButton.setText(R.string.edit_button_clicked_text);    // Set EditButton text to "Done"
			view.setSelected(true);
			
			final MainActivity MAIN_ACTIVITY = this;    // Grab reference to method caller
			
			/* Allow user to choose which Alarm to edit */
			mListView.setOnItemClickListener((adapterView, itemView, pos, row_id) -> {
				/* Get the Alarm ID of the selected Alarm so that the alarm can be sent
				 * to receiving Activity */
				Intent alarmEdit = new Intent(MAIN_ACTIVITY, AlarmEditActivity.class);
				
				TextView alarmID = itemView.findViewById(R.id.alarmID);
				int _id = Integer.parseInt(alarmID.getText().toString());
				Alarm intentAlarm = mDatabaseManager.fetchById(_id);
				
				Bundle intentBundle = new Bundle();
				intentBundle.putParcelable("alarm", intentAlarm);
				alarmEdit.putExtra("alarm", intentBundle);
				
				MAIN_ACTIVITY.startActivity(alarmEdit);
			});
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		try {
			mDatabaseManager.close();
		} finally {
			super.onDestroy();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		TextView textView = findViewById(R.id.editButton);
		textView.setText(R.string.edit_button_text);
		
		/* Remove onItemClickListener and reset EditButton text */
		textView.setSelected(false);
		mListView.setOnItemClickListener(null);
		
		Cursor cursor = mDatabaseManager.fetch();
		mAdapter.swapCursor(cursor);
	}
	
}
