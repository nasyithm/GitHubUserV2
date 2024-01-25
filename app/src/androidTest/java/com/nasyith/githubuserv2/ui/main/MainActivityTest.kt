package com.nasyith.githubuserv2.ui.main

import android.view.KeyEvent
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.nasyith.githubuserv2.R
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.favoriteuser.FavoriteUserActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val username = "nasyithm"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun testMenuTheme() {
        onView(withId(R.id.menuTheme)).check(matches(isDisplayed()))
        onView(withId(R.id.menuTheme)).perform(click())
        onView(withId(R.id.menuTheme)).perform(click())
    }

    @Test
    fun testMenuFavorite() {
        onView(withId(R.id.menuFavorite)).perform(click())
        onView(withId(R.id.rvFavoriteUser)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchAndDetailUser() {
        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(isAssignableFrom(EditText::class.java)).perform(typeText(username),
            pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withId(R.id.rvUsers)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.rvUsers)).perform(
            RecyclerViewActions.actionOnItem<UserAdapter.ListViewHolder>(hasDescendant(withText(username)), click())
        )

        onView(withId(R.id.detailUsername)).check(matches(withText(username)))
        onView(withId(R.id.btnFavoriteUser)).perform(click())

        ActivityScenario.launch(FavoriteUserActivity::class.java)

        onView(withId(R.id.rvFavoriteUser)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.rvFavoriteUser)).perform(
            RecyclerViewActions.actionOnItem<UserAdapter.ListViewHolder>(hasDescendant(withText(username)), click())
        )
        onView(withId(R.id.btnFavoriteUser)).perform(click())
    }
}