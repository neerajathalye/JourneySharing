package com.group12.journeysharing;

import com.group12.journeysharing.model.User;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void homeActivityTest()
    {

        User user = new User();
        user.setFirstName("Neeraj");
        user.setLastName("Athalye");
        user.setFullName();
        user.setRating(4.0);

//        assertEquals(true, HomeActivity.mDrawerLayout.isDrawerOpen(GravityCompat.START));

        assertEquals("Neeraj Athalye", user.getFullName());
        assertEquals(4.0, user.getRating(), 0.001);
        assertEquals("1.0", BuildConfig.VERSION_NAME);

    }

    @Test
    public void signUpTest()
    {

        User user = new User();
        user.setFirstName("Neeraj");
        user.setLastName("Athalye");
        user.setFullName();
        user.setRating(4.0);

//        assertEquals(true, HomeActivity.mDrawerLayout.isDrawerOpen(GravityCompat.START));

        assertEquals("Neeraj Athalye", user.getFullName());
        assertEquals(4.0, user.getRating(), 0.001);
        assertEquals("1.0", BuildConfig.VERSION_NAME);

    }



}