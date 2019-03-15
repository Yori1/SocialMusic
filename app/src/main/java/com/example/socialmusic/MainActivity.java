package com.example.socialmusic;

import android.os.Bundle;

public class MainActivity extends DrawerLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setMenuLayoutElements(R.layout.activity_main, R.id.toolbar_main, R.id.drawer_layout_main);
    }
}
