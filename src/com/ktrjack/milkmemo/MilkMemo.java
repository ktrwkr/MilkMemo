package com.ktrjack.milkmemo;
/**
 *	MilkMemoツール
 *	赤ちゃんのミルクとうんちをした時間を記録します。
 *	2014/02/25 新規作成
 *	
 *
 */
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//Add ViewPager 20140430
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
//Add end
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.MenuItem;
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
    	public static final String ARG_SECTION_NUMBER = "section_number";
    	public static String T_MILKMEMO = "MilkMemo";

   	 //DBオブジェクト
	private SQLiteDatabase db;
	private MilkMemoDbBeans dbbeans;
	Cursor crsr = null;
	// ADD 20140224 フラグメント生成をonCreateに移動
	private Fragment memoFragment;
	private Fragment listFragment;
	Fragment fragment = new Fragment();
	
	// ADD 20140430
	// PagerAdapter実装
	MilkSwipePagerAdapter mMilkSwipePagerAdapter;
	ViewPager mViewPager;
	////

	/** Called when the activity is first created. */
    	@Override
    	public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.milkmain);

		// Add 20140430
		// PagerAdapter実装
		mMilkSwipePagerAdapter = new MilkSwipePagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager)findViewById(R.id.pager);
		//スワイプでタブ移動したときの処理を追加（タブを選択しなおす）
		mViewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						getActionBar().setSelectedNavigationItem(position);
					}
				});

		mViewPager.setAdapter(mMilkSwipePagerAdapter);
		////

		// set action bar
        	final ActionBar actionBar = getActionBar();
       
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
		//Set Fragments add 20140224
    		// タブセレクト処理軽量化のためあらかじめ生成しておく
    		/*this.memoFragment = new TabMilkButton();
    		this.listFragment = new TabHistory();*/
    	
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
	/**
	 * オプション押下時設定
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent();
		
		switch(item.getItemId()){
		case R.id.menu_add_button:
			//設定押下時処理をここに書く
			intent.setClassName(this.getApplicationContext(),"com.ktrjack.milkmemo.AddButton");
			startActivity(intent);
			return true;
		case R.id.menu_del_button:
			//設定押下時処理をここに書く
			intent.setClassName(this.getApplicationContext(),"com.ktrjack.milkmemo.DelButton");
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			//設定押下時処理をここに書く
			intent.setClassName(this.getApplicationContext(),"com.ktrjack.milkmemo.SettingMenuList");
			startActivity(intent);
			return true;
		default:
		}
		return false;
	}
    

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		if ( fragment != null){
			getSupportFragmentManager().beginTransaction().remove(fragment).commit();
		}
    	}

	/**
	*  tab Select action 
	*/
	@Override
    	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        	// When the given tab is selected, show the tab contents in the container
		//Fragment fragment = new Fragment();
/* 20140430 Commented out
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
*/
		//Change tabs with swipe views.
		//Add 20140430
		mViewPager.setCurrentItem(tab.getPosition());
       

    	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
}
/**
 *  	Swipe対応アダプター
 *	Add 20140430
 */
class MilkSwipePagerAdapter extends FragmentStatePagerAdapter{
	private static int TAB_AMOUNT = 2;		//タブの数（getCountメソッドで返さないと落ちる）
	//　各フラグメント用
	private Fragment memoFragment = new TabMilkButton();
	private Fragment listFragment = new TabHistory();

	Fragment fragment = new Fragment();
	//Fragment fragment;

	public MilkSwipePagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int i){
		//Fragment fragment = new DemoObjectFragment();
		//タブ毎に与えるフラグメントインスタンスを切り替える
		switch(i){
		case 0:
			fragment = this.memoFragment;
			break;
		case 1:
			fragment = this.listFragment;
			break;
		default:
			Log.e(MilkMemo.T_MILKMEMO,"ERROR invalid argument");
		}

		Bundle args = new Bundle();
		// Our object is just an integer :-P
		//args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
		args.putInt(MilkMemo.ARG_SECTION_NUMBER, i + 1);
		System.out.println(i);
		
		fragment.setArguments(args);
		return fragment;
	}
	/**
	 * ※getCountメソッドでタブの総数を返すこと
	 * そうしないとgetItemのsetArgumentsでオーバーフローして落ちる
	 */
	@Override
	public int getCount() {
		return TAB_AMOUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}

	@Override
	public void startUpdate (ViewGroup container){
	//ToDo Do something maybe..(not must)
	}
}

