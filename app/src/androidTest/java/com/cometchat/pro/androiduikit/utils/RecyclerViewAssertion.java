package com.cometchat.pro.androiduikit.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;

public class RecyclerViewAssertion implements ViewAssertion {
    private final int expectedCount;

    public RecyclerViewAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter.getItemCount()<1) {
            Assert.fail("No Conversation Found");
        } else if (adapter.getItemCount()<expectedCount) {
            Assert.assertTrue(expectedCount+" conversation found",true);
        } else {
            Assert.assertTrue(adapter.getItemCount()+" conversations",true);
        }
    }
}