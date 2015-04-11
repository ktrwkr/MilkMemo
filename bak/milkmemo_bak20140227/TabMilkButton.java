package com.ktrjack.milkmemo;

import android.app.Activity;
import android.app.Activity;
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
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.*;

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
