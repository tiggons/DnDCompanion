package com.test1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void runTest(View v)
    {
    	CharacterData character = new CharacterData();
    	XMLCharacterSheetReader charSheetReader;
    	boolean mExternalStorageAvailable = false;
    	String state = Environment.getExternalStorageState();
    	File file;

    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    		 mExternalStorageAvailable = true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    mExternalStorageAvailable = true;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	    mExternalStorageAvailable = false;
    	}
    	if (mExternalStorageAvailable)
    	{
    		File dir = Environment.getExternalStorageDirectory();
    		if (!dir.canRead())
    		{
                Log.d("READ","can't read root");
    		}
    		file = new File(dir,"FLARK.dnd4e");
    		if (file.exists())
    		{
    			Log.d("FILE", "EXISTS");
    		}else
    		{
    			Log.d("FILE", "NOT EXISTS");
    		}
    		charSheetReader = new XMLCharacterSheetReader(file.getAbsolutePath());
    		character = charSheetReader.loadCharacterSheet();
    		character.print();
    	}
    }
    
    
}
