package com.ktrjack.milkmemo;

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

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.*;

public class MilkMemoComp extends FragmentActivity implements ActionBar.TabListener{
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
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
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
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
        //Fragment fragment = new DummySectionFragment();
        Fragment fragment;
        
        //Bundle args = new Bundle();
        int i = tab.getPosition();

        switch(i){
        case 0:
            //fragment = new TabMilkButton();
        	fragment = this.memoFragment;
            break;
        case 1:
        	//再描画のため自分自身を引数に渡す
			//fragment = new TabHistory(this);
        	fragment = this.listFragment;
			break;

        default:
        	fragment = new DummySectionFragment();
             //
        }

		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
		fragment.setArguments(args);
    	
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
       

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(	LayoutInflater inflater, 
        							ViewGroup container,
        							Bundle savedInstanceState) {
			//Do Nothing
        	
            TextView textView = new TextView(getActivity());
  
            Bundle args = getArguments();

            return textView;
        }
        
    }
	/**
	* 記録ボタン用タブクラス
	*
	*/
	public class TabMilkButton extends Fragment {
		
		//constants
		public static final int INT_MILK = 0;
		public static final int INT_UNCHI = 1;
		public static final String STR_MILK = "ミルク";
		public static final String STR_UNCHI = "うんち";
		
		//private Button button1;
		//constructor
		public TabMilkButton() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(	LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View inputTab = inflater.inflate(R.layout.frag_milk_button, null);

			//ミルク時間ボタン押下時
			//ミルク時間ボタンオブジェクト生成
			Button inputMilkBtn = (Button)inputTab.findViewById(R.id.add_milk_time);
			//うんち時間ボタンオブジェクト生成
			Button inputUnchiBtn = (Button)inputTab.findViewById(R.id.add_unchi_time);
			
			ClickAction milkClick = new ClickAction(INT_MILK);
			ClickAction unchiClick = new ClickAction(INT_UNCHI);
			
			//View v = new View();
			inputMilkBtn.setOnClickListener(milkClick);
			inputUnchiBtn.setOnClickListener(unchiClick);

			return inputTab;
		}
		

	}

	/**
	* ボタンクリック時アクションメソッド
	*
	*/

	class ClickAction implements OnClickListener{
		
		private int btnKind;
	    //DBオブジェクト
		private SQLiteDatabase db;
		private MilkMemoDbBeans dbbeans;
		Cursor crsr = null;
		
		//コンストラクタ
		public ClickAction(final int btnKind){
			this.btnKind = btnKind;
		}
		
		@Override
		public void onClick(View v) {
			//新規データ入力
			dbbeans = new MilkMemoDbBeans(v.getContext());
			db = dbbeans.getWritableDatabase();
			//DBオブジェクトセット
			dbbeans.setDatabase(db);
			
			switch(this.btnKind){
			case TabMilkButton.INT_MILK:
				System.out.println("milk pushed");

				//SQL insert実行
				dbbeans.insertTable(TabMilkButton.STR_MILK);  
				break;
			case TabMilkButton.INT_UNCHI:
				dbbeans.insertTable(TabMilkButton.STR_UNCHI);  
				break;
			}
			//DBオブジェクトクローズ
			db.close();
		}
	}
	
	/**
	* 履歴表示タブクラス
	*
	*/
	public class TabHistory extends Fragment {
	    //DBオブジェクト
		private SQLiteDatabase db;
		private MilkMemoDbBeans dbbeans;
		private ArrayList<ArrayList> allrowdata;
		Cursor crsr;
		
		//private Button button1;
		//constructor
		public TabHistory() {
			this.crsr = null;
			this.allrowdata = new ArrayList<ArrayList>();
		}

		public static final String ARG_SECTION_NUMBER = "section_number";
		//String[][] arMilkHist;
			
		@Override
		public View onCreateView(	LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View inputTab = inflater.inflate(R.layout.frag_milk_unchi_hist, null);
			ArrayList<StringBuffer> rowdata = new ArrayList<StringBuffer> ();;
			StringBuffer tmp;
			//DBオブジェクト生成
			dbbeans = new MilkMemoDbBeans(inputTab.getContext());
			db = dbbeans.getWritableDatabase();
			
			//DBオブジェクトセット
			dbbeans.setDatabase(db);
			//配列型での取得
			allrowdata = (ArrayList)dbbeans.arSlctTableView();
			
			db.close();
			if(allrowdata != null){
				for (int i=0; i<allrowdata.size(); i++){
					tmp = new StringBuffer();
					for ( int j=0; j<allrowdata.get(i).size(); j++){
						tmp.append(allrowdata.get(i).get(j));
						tmp.append(" ");

					}
					rowdata.add(tmp);
				}
		    	//現在いるタブビューのIDで指定してやる（ハマった）
		    	ListView milkList = (ListView)inputTab.findViewById(R.id.lvHist);
		    	//ArrayAdapterの第一引数は現在のタブのコンテキスト
		    	ListAdapter la = (ListAdapter) new ArrayAdapter<String>(inputTab.getContext(), android.R.layout.simple_list_item_1, (List)rowdata);
				
				//現在のタブビューにセット
				milkList.setAdapter(la);
				LongClick lc = new LongClick();
				
				milkList.setOnItemLongClickListener(lc);
			}
			
			return inputTab;
		}
		

	}

	class LongClick implements OnItemLongClickListener{
	    //DBオブジェクト
		private SQLiteDatabase db;
		private MilkMemoDbBeans dbbeans;

		
		/**
	     * 各体重リストのロングクリック時のintentを書く
	     * 
	     * */
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View milkList, int position, long id) {
			// TODO Auto-generated method stub
			System.out.println("Dell");
			ListView listView = (ListView) parent;
			
			String item = listView.getItemAtPosition(position).toString();
	          
			String[] arItem = item.split(" ", 0);
			String datetime = arItem[2] ;
			//テーブルIDの取得
			String dbid = arItem[0];
			
			System.out.println(dbid);
			//データ削除
			dbbeans = new MilkMemoDbBeans(listView.getContext());
			db = dbbeans.getWritableDatabase();
			//DBオブジェクトセット
			dbbeans.setDatabase(db);
			 //　データ削除関数呼び出し
			dbbeans.deleteData(dbid); 
			db.close();
			Toast.makeText(milkList.getContext(), "Oh! No " + datetime + " DELETED!!!", Toast.LENGTH_LONG).show();
			//現在のタブを再描画
			//Fragment fragment = new MilkMemo.DummySectionFragment();
			//Fragmentオブジェクト設定
			Fragment fragment = new TabHistory();
			Bundle args = new Bundle();
			args.putInt(MilkMemo.DummySectionFragment.ARG_SECTION_NUMBER, 2);
			fragment.setArguments(args);
			
			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

			    
			return false; 
		}
	}

}
