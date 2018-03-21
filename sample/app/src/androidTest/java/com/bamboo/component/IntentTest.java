package com.bamboo.component;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bamboo.component.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by tangshuai on 2018/3/17.
 */


@RunWith(AndroidJUnit4.class)
public class IntentTest {

    @Rule
    public IntentsTestRule<MainActivity> clearTaskActivityIntentsTestRule = new IntentsTestRule<>(MainActivity.class);


    @Test
    public void cleanTaskFlag() {

        onView(withText("ClearTask")).perform(ViewActions.click());

        //测试是否对应的Intent被发送
        intended(allOf(
                IntentMatchers.hasAction("hhhh"),
                hasComponent(hasShortClassName(".intenttest.IntentTestActivity")),
                IntentMatchers.hasFlag(Intent.FLAG_ACTIVITY_CLEAR_TASK)));
    }

    @Test
    public void cleanTopFlag() {

        onView(withText("ClearTop")).perform(ViewActions.click());

        //测试是否对应的Intent被发送
        intended(allOf(
                hasComponent(hasShortClassName(".intenttest.IntentTestActivity")),
                IntentMatchers.hasFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }


}
