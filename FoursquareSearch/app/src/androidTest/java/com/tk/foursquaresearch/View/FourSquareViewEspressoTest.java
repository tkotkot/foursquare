package com.tk.foursquaresearch.View;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.runner.AndroidJUnit4;

import com.tk.foursquaresearch.R;
import com.tk.foursquaresearch.presenter.FourSquarePresenterFactory;
import com.tk.foursquaresearch.presenter.FourSquarePresenterInterface;
import com.tk.foursquaresearch.view.FourSquareViewActivity;
import com.tk.foursquaresearch.view.FourSquareViewInterface;
import com.tk.foursquaresearch.view.util.CustomItemData;
import com.tk.foursquaresearch.view.util.CustomListAdapter;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FourSquareViewEspressoTest {
    FourSquarePresenterInterface mockInterface;

    @Rule
    public ActivityTestRule<FourSquareViewActivity> rule = new ActivityTestRule<FourSquareViewActivity>(FourSquareViewActivity.class, false, false);

    @Before
    public void initTest() {
        mockInterface = mock(FourSquarePresenterInterface.class);
        FourSquarePresenterFactory.setPresenterForTest(mockInterface);
    }

    @Test
    public void testInitNotReadyComesReady() {
        when(mockInterface.isReady()).thenReturn(false);

        rule.launchActivity(new Intent(""));

        verify(mockInterface, times(1)).initialize();
        verify(mockInterface, times(1)).setView(any(FourSquareViewInterface.class));
        verify(mockInterface, times(1)).isReady();

        onView(withId(R.id.clearButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.editText)).check(matches(not(isEnabled())));

        FourSquareViewActivity view = rule.getActivity();
        view.searchReady();

        onView(withId(R.id.clearButton)).check(matches(isEnabled()));
        onView(withId(R.id.editText)).check(matches(isEnabled()));
    }

    @Test
    public void testReadyEnterSearch() {
        when(mockInterface.isReady()).thenReturn(true);

        rule.launchActivity(new Intent(""));

        verify(mockInterface, times(1)).initialize();
        verify(mockInterface, times(1)).setView(any(FourSquareViewInterface.class));
        verify(mockInterface, times(1)).isReady();

        onView(withId(R.id.clearButton)).check(matches(isEnabled()));
        onView(withId(R.id.editText)).check(matches(isEnabled()));

        FourSquareViewActivity view = rule.getActivity();
        ListView listView = (ListView) view.findViewById(R.id.searchList);
        assertEquals(listView.getCount(), 0);

        onView(withId(R.id.editText))
                .perform(typeText("H"));

        verify(mockInterface, times(1)).search("H");

        onView(withId(R.id.editText))
                .perform(typeText("o"));

        verify(mockInterface, times(1)).search("Ho");

        view.updateSearchResults(generateOneHit());

        onView(isRoot()).perform(waitFor(2000));

        assertEquals(listView.getCount(), 1);
        onData(instanceOf(CustomItemData.class))
                .inAdapterView(allOf(withId(R.id.searchList), isDisplayed()))
                .atPosition(0)
                .check(matches(isDisplayed()));

        CustomListAdapter adapter = (CustomListAdapter) listView.getAdapter();
        assertEquals(adapter.getItem(0).getTop(), "N 1");
        assertEquals(adapter.getItem(0).getMiddle(), "A 1");
        assertEquals(adapter.getItem(0).getBottom(), "D 1 meters");
    }

    @Test
    public void testReadyEnterSearch5Hit() {
        when(mockInterface.isReady()).thenReturn(true);

        rule.launchActivity(new Intent(""));

        verify(mockInterface, times(1)).initialize();
        verify(mockInterface, times(1)).setView(any(FourSquareViewInterface.class));
        verify(mockInterface, times(1)).isReady();

        onView(withId(R.id.clearButton)).check(matches(isEnabled()));
        onView(withId(R.id.editText)).check(matches(isEnabled()));

        FourSquareViewActivity view = rule.getActivity();
        ListView listView = (ListView) view.findViewById(R.id.searchList);
        assertEquals(listView.getCount(), 0);

        onView(withId(R.id.editText))
                .perform(typeText("H"));

        verify(mockInterface, times(1)).search("H");

        onView(withId(R.id.editText))
                .perform(typeText("o"));

        verify(mockInterface, times(1)).search("Ho");

        view.updateSearchResults(generateFiveHits());

        onView(isRoot()).perform(waitFor(2000));

        assertEquals(listView.getCount(), 5);

        CustomListAdapter adapter = (CustomListAdapter) listView.getAdapter();
        assertEquals(adapter.getItem(2).getTop(), "N 3");
        assertEquals(adapter.getItem(2).getMiddle(), "A 3");
        assertEquals(adapter.getItem(2).getBottom(), "D 3 meters");
    }

    @Test
    public void testClearButton() {
        when(mockInterface.isReady()).thenReturn(true);

        rule.launchActivity(new Intent(""));

        verify(mockInterface, times(1)).initialize();
        verify(mockInterface, times(1)).setView(any(FourSquareViewInterface.class));
        verify(mockInterface, times(1)).isReady();

        onView(withId(R.id.clearButton)).check(matches(isEnabled()));
        onView(withId(R.id.editText)).check(matches(isEnabled()));

        FourSquareViewActivity view = rule.getActivity();
        ListView listView = (ListView) view.findViewById(R.id.searchList);
        assertEquals(listView.getCount(), 0);
        onView(withId(R.id.editText)).check(matches(withText("")));

        onView(withId(R.id.editText))
                .perform(typeText("Hotel"));

        view.updateSearchResults(generateFiveHits());

        onView(isRoot()).perform(waitFor(2000));

        assertEquals(listView.getCount(), 5);
        onView(withId(R.id.editText)).check(matches(withText("Hotel")));

        onView(withId(R.id.clearButton)).perform(click());

        assertEquals(listView.getCount(), 0);
        onView(withId(R.id.editText)).check(matches(withText("")));
    }


    private List<HashMap<String,String>> generateOneHit() {
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        list.add(generateHit("1"));
        return list;
    }

    private List<HashMap<String,String>> generateFiveHits() {
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        list.add(generateHit("1"));
        list.add(generateHit("2"));
        list.add(generateHit("3"));
        list.add(generateHit("4"));
        list.add(generateHit("5"));
        return list;
    }

    private HashMap<String,String> generateHit(String id) {
        HashMap<String,String> hit = new HashMap<String,String>();
        hit.put("name", "N " + id);
        hit.put("address", "A " + id);
        hit.put("distance", "D " + id);
        return hit;
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}

