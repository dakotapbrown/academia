package edu.vcu.eythirteenapp.Iteration03Tests;


import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.PrecisionDescriber;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.Tapper;
import android.support.test.espresso.core.internal.deps.guava.base.Optional;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.vcu.eythirteenapp.AlarmEditActivity;
import edu.vcu.eythirteenapp.AlarmWakeActivity;
import edu.vcu.eythirteenapp.MainActivity;
import edu.vcu.eythirteenapp.R;
import edu.vcu.eythirteenapp.alarm.Alarm;
import edu.vcu.eythirteenapp.database.AlarmDatabaseHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStory09Test {
	public static final long TIMEOUT = TimeUnit.MINUTES.toMillis(1L)
			                                   + TimeUnit.SECONDS.toMillis(1L);
	
	// Save an alarm with Math off, wait for it to sound
	// Check if pressing dismiss does not present math question
	
	private static Alarm alarm;
	private static Intent intent;
	private static Bundle bundle;
	
	@Rule
	public ActivityTestRule<AlarmEditActivity> editActivity
			= new ActivityTestRule<>(AlarmEditActivity.class, false, false);
	
	@Rule
	public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);
	
	private UiDevice device;
	
	
	@Before
	public void setUp() {
		alarm = new Alarm()
				        .setTime(
						        LocalTime.now()
								        .plusMinutes(1)
								        .withSecond(0)
								        .withNano(0))
				        .setVolume((byte) 0);
		
		intent = new Intent();
		bundle = new Bundle();
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
	}
	
	
	@After
	public void cleanUp() {
		main.getActivity().deleteDatabase(AlarmDatabaseHelper.DB_NAME);
		main.getActivity().deleteDatabase(AlarmDatabaseHelper.TABLE_NAME);
		main.finishActivity();
	}
	
	
	@Test
	public void Scenario01() {
		alarm.setExpressionEnabled(false);
		bundle.putParcelable("alarm", alarm);
		intent.putExtra("alarm", bundle);
		
		editActivity.launchActivity(intent);
		onView(withId(R.id.saveButton)).perform(click());
		
		device.wait(Until.hasObject(By.clazz(AlarmWakeActivity.class)), TIMEOUT);
		
		onView(withId(R.id.snoozeButton)).check(matches(allOf(isClickable(), isEnabled())));
		onView(withId(R.id.dismissButton)).perform(click());
		
		assertTrue(this.main.getActivity().hasWindowFocus());
	}
	
	
	@Test
	public void Scenario02() {
		editActivity.launchActivity(intent);
		onView(withId(R.id.saveButton)).perform(click());
		
		device.wait(Until.hasObject(By.clazz(AlarmWakeActivity.class)), TIMEOUT);
		
		onView(withId(R.id.dismissButton)).perform(click());
		
		onView(withText(R.string.mathQuestionDialog)).check(matches(isDisplayed()));
	}
	
	
	@Test
	public void Scenario03() {
		editActivity.launchActivity(intent);
		
		device.wait(Until.hasObject(By.res(String.valueOf(R.id.expressionEnabledSwitch))), TIMEOUT);
		onView(withId(R.id.expressionEnabledSwitch)).perform(UserStory09Test.click());
		
		onView(withText(R.string.expressionDisabledDialog)).check(matches(isDisplayed()));
	}
	
	
	public static ViewAction click() {
		return actionWithAssertions(
				new ClickAction(
						Tap.SINGLE,
						GeneralLocation.VISIBLE_CENTER,
						Press.FINGER,
						InputDevice.SOURCE_UNKNOWN,
						MotionEvent.BUTTON_PRIMARY));
	}
	
	
	
	/**
	 * Copyright (C) 2014 The Android Open Source Project
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *   http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */
	public static final class ClickAction implements ViewAction {
		
		
		
		private static final String TAG = "GeneralClickAction";
		
		final CoordinatesProvider coordinatesProvider;
		final Tapper tapper;
		final PrecisionDescriber precisionDescriber;
		private final Optional<ViewAction> rollbackAction;
		private final int inputDevice;
		private final int buttonState;
		
		
		/*
		 * @deprecated
		 * Use {@link #GeneralClickAction(Tapper, CoordinatesProvider, PrecisionDescriber, int, int)}
		 * instead
		 */
		@Deprecated
		public ClickAction(
				Tapper tapper,
				CoordinatesProvider coordinatesProvider,
				PrecisionDescriber precisionDescriber) {
			this(tapper, coordinatesProvider, precisionDescriber, 0, 0, null);
		}
		
		
		public ClickAction(
				Tapper tapper,
				CoordinatesProvider coordinatesProvider,
				PrecisionDescriber precisionDescriber,
				int inputDevice,
				int buttonState) {
			this(tapper, coordinatesProvider, precisionDescriber, inputDevice, buttonState, null);
		}
		
		
		/*
		 * @deprecated
		 * Use {@link #GeneralClickAction(Tapper, CoordinatesProvider, PrecisionDescriber, int, int,
		 * ViewAction)} instead
		 */
		@Deprecated
		public ClickAction(
				Tapper tapper,
				CoordinatesProvider coordinatesProvider,
				PrecisionDescriber precisionDescriber,
				ViewAction rollbackAction) {
			this(tapper, coordinatesProvider, precisionDescriber, 0, 0, rollbackAction);
		}
		
		
		public ClickAction(
				Tapper tapper,
				CoordinatesProvider coordinatesProvider,
				PrecisionDescriber precisionDescriber,
				int inputDevice,
				int buttonState,
				ViewAction rollbackAction) {
			this.coordinatesProvider = coordinatesProvider;
			this.tapper = tapper;
			this.precisionDescriber = precisionDescriber;
			this.inputDevice = inputDevice;
			this.buttonState = buttonState;
			this.rollbackAction = Optional.fromNullable(rollbackAction);
		}
		
		
		@Override
		@SuppressWarnings("unchecked")
		public Matcher<View> getConstraints() {
			Matcher<View> standardConstraint = isDisplayingAtLeast(10); // changed to 10%
			if (rollbackAction.isPresent()) {
				return allOf(standardConstraint, rollbackAction.get().getConstraints());
			} else {
				return standardConstraint;
			}
		}
		
		
		@Override
		public String getDescription() {
			return tapper.toString().toLowerCase() + " click";
		}
		
		
		@Override
		public void perform(UiController uiController, View view) {
			float[] coordinates = coordinatesProvider.calculateCoordinates(view);
			float[] precision = precisionDescriber.describePrecision();
			
			Tapper.Status status = Tapper.Status.FAILURE;
			int loopCount = 0;
			// Native event injection is quite a tricky process. A tap is actually 2
			// seperate motion events which need to get injected into the system. Injection
			// makes an RPC call from our app under test to the Android system server, the
			// system server decides which window layer to deliver the event to, the system
			// server makes an RPC to that window layer, that window layer delivers the event
			// to the correct UI element, activity, or window object. Now we need to repeat
			// that 2x. for a simple down and up. Oh and the down event triggers timers to
			// detect whether or not the event is a long vs. short press. The timers are
			// removed the moment the up event is received (NOTE: the possibility of eventTime
			// being in the future is totally ignored by most motion event processors).
			//
			// Phew.
			//
			// The net result of this is sometimes we'll want to do a regular tap, and for
			// whatever reason the up event (last half) of the tap is delivered after long
			// press timeout (depending on system load) and the long press behaviour is
			// displayed (EG: show a context menu). There is no way to avoid or handle this more
			// gracefully. Also the longpress behavour is app/widget specific. So if you have
			// a seperate long press behaviour from your short press, you can pass in a
			// 'RollBack' ViewAction which when executed will undo the effects of long press.
			
			while (status != Tapper.Status.SUCCESS && loopCount < 3) {
				try {
					status = tapper.sendTap(uiController, coordinates, precision, inputDevice, buttonState);
					if (Log.isLoggable(TAG, Log.DEBUG)) {
						Log.d(
								TAG,
								"perform: "
										+ String.format(
										"%s - At Coordinates: %d, %d and precision: %d, %d",
										this.getDescription(),
										(int) coordinates[0],
										(int) coordinates[1],
										(int) precision[0],
										(int) precision[1]));
					}
				} catch (RuntimeException re) {
					throw new PerformException.Builder()
							      .withActionDescription(
									      String.format(
											      "%s - At Coordinates: %d, %d and precision: %d, %d",
											      this.getDescription(),
											      (int) coordinates[0],
											      (int) coordinates[1],
											      (int) precision[0],
											      (int) precision[1]))
							      .withViewDescription(HumanReadables.describe(view))
							      .withCause(re)
							      .build();
				}
				
				int duration = ViewConfiguration.getPressedStateDuration();
				// ensures that all work enqueued to process the tap has been run.
				if (duration > 0) {
					uiController.loopMainThreadForAtLeast(duration);
				}
				if (status == Tapper.Status.WARNING) {
					if (rollbackAction.isPresent()) {
						rollbackAction.get().perform(uiController, view);
					} else {
						break;
					}
				}
				loopCount++;
			}
			if (status == Tapper.Status.FAILURE) {
				throw new PerformException.Builder()
						      .withActionDescription(this.getDescription())
						      .withViewDescription(HumanReadables.describe(view))
						      .withCause(
								      new RuntimeException(
										      String.format(
												      "Couldn't "
														      + "click at: %s,%s precision: %s, %s . Tapper:"
														      + " %s coordinate provider: %s precision "
														      + "describer: %s. Tried %s times. With "
														      + "Rollback? %s",
												      coordinates[0],
												      coordinates[1],
												      precision[0],
												      precision[1],
												      tapper,
												      coordinatesProvider,
												      precisionDescriber,
												      loopCount,
												      rollbackAction.isPresent())))
						      .build();
			}
			
			if (tapper == Tap.SINGLE && view instanceof WebView) {
				// WebViews will not process click events until double tap
				// timeout. Not the best place for this - but good for now.
				uiController.loopMainThreadForAtLeast(ViewConfiguration.getDoubleTapTimeout());
			}
		}
	}
}

