package com.bamboo.component;

import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bamboo.component.MainActivity;
import bamboo.component.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by tangshuai on 2018/3/17.
 */

@RunWith(AndroidJUnit4.class)
public class ParamIntentTest {

    @Rule
    public ActivityTestRule<MainActivity> intentsTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void paracelableTest() {
        onView(withText("ParamersTrancefer")).perform(click());

        onView(withId(R.id.tv_param1)).check(matches(withText("this is text1")));
        onView(withId(R.id.tv_param2)).check(matches(withText("this is text2")));
    }

    /**
     * 测试程序里面param2因为用transient进行了描述，所以在序列换传递时不包含该字段，所以最终接收的时候为null
     */
    @Test
    public void serializableTest() {
        onView(withText("SerializableTest")).perform(click());

        onView(withId(R.id.tv_param1)).check(matches(withText("this is text1")));
        onView(withId(R.id.tv_param2)).check(matches(withText("null")));
    }
}
