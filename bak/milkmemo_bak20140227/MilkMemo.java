package com.ktrjack.milkmemo;
/**
 *	MilkMemoツール
 *	赤ちゃんのミルクとうんちをした時間を記録します。
 *	2014/02/25 新規作成
 *
 */
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MilkMemo extends FragmentActivity implements ActionBar.TabListener{
    	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    	private static final String ARG_SECTION_NUMBER = "section_number";
    	private static String T_MILKMEMO = "MilkMemo";

   	 //DBオブジェクト
	private SQLiteDatabase db;
	private MilkMemoDbBeans dbbeans;
	Cursor crsr = null;
	// ADD 20140224 フラグメント生成をonCreateに移動
	private Fragment memoFragment;
	private Fragment listFragment;
	
	
	/** Called when the activity is first created. */
    	@Override
    	public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.milkmain);

        	// set action bar
        	final ActionBar actionBar = getActionBar();
    		if (actionBar == null ){
    			System.out.println(ActionBar.NAVIGATION_MODE_TABS);
    		}
        	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
		//Set Fragments add 20140224
    		// タブセレクト処理軽量化のためあらかじめ生成しておく
    		this.memoFragment = new TabMilkButton();
    		this.listFragment = new TabHistory();
    	
        	// Tabs add to Action bar
		// title_section1 : Milk and Unchi
		// title_section2 : History
        	actionBar.addTab(actionBar.newTab().setText(R.string.title_section1).setTabListener(this));
        	actionBar.addTab(actionBar.newTab().setText(R.string.title_section2).setTabListener(this));
    	
        	//DB生成
    		dbbeans = new MilkMemoDbBeans(this.getApplicationContext());
		db = dbbeans.getWritableDatabase();
		db.close();
    	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
        	if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        	getMenuInflater().inflate(R.menu.activity_milk_memo, menu);
        	return true;
	}

    

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	}

	/**
	*  tab Select action 
	*/
	@Override
    	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        	// When the given tab is selected, show the tab contents in the container
		Fragment fragment = new Fragment();
        
        	//Bundle args = new Bundle();
        	int i = tab.getPosition();

		switch(i){
		case 0:
			fragment = this.memoFragment;
			break;
		case 1:
			fragment = this.listFragment;
			break;
		default:
			Log.e(T_MILKMEMO,"ERROR invalid argument");
		}

		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, i + 1);
		fragment.setArguments(args);
    	
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
       

    	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
}
