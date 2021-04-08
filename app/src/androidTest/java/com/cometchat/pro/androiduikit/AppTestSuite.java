package com.cometchat.pro.androiduikit;

import com.cometchat.pro.androiduikit.app.ComponentActivityTest;
import com.cometchat.pro.androiduikit.app.MainActivityTest;
import com.cometchat.pro.androiduikit.app.SelectActivityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                MainActivityTest.class,
                SelectActivityTest.class,
                ComponentActivityTest.class
        })

public class AppTestSuite {}
