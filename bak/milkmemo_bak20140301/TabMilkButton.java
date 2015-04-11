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
import android.widget.*;

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
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentActivity fa = getActivity();

		View inputTab = inflater.inflate(R.layout.frag_milk_button, null);

		//ミルク時間ボタン押下時
		//ミルク時間ボタンオブジェクト生成
		Button inputMilkBtn = (Button)inputTab.findViewById(R.id.add_milk_time);
		//うんち時間ボタンオブジェクト生成
		Button inputUnchiBtn = (Button)inputTab.findViewById(R.id.add_unchi_time);
		
		ClickAction milkClick = new ClickAction(INT_MILK, fa);
		ClickAction unchiClick = new ClickAction(INT_UNCHI, fa);
		
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
	FragmentActivity fa;
	
	//コンストラクタ
	public ClickAction(final int btnKind, final FragmentActivity fa){
		this.btnKind = btnKind;
		this.fa = fa;
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
			
			//テスト中　ダイアログ表示
			showDateTimeDialog();

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

	/**
	 *  日時ダイアログ表示メソッド
	 */
	private void showDateTimeDialog(){
		DateTimeDialogFragment dateTimeDialog = new DateTimeDialogFragment();

		/**
		 *  現在日時をダイアログにセット
		 */
		Calendar calendar = Calendar.getInstance();
		
		Bundle args = new Bundle();
		args.putInt("year", calendar.get(Calendar.YEAR));
		args.putInt("month", calendar.get(Calendar.MONTH));
		args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
		args.putInt("hourOfDay", calendar.get(Calendar.HOUR_OF_DAY));
		args.putInt("minute", calendar.get(Calendar.MINUTE));
		dateTimeDialog.setArguments(args);

		/**
		 *  コールバックに選択された日時をセット
		 */
		CallBackListener callOnDateTimeSet = new CallBackListener();
		dateTimeDialog.setCallBack(callOnDateTimeSet);
		dateTimeDialog.show(fa.getSupportFragmentManager(), "Date Time Picker");
		

	}
}
class CallBackListener implements DateTimePickerDialog.OnDateTimeSetListener{
	public void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute){
		//ユーザが入力したダイアログ情報が引数に入ってくる
		//テストコード
		System.out.println(year);
		System.out.println(monthOfYear);
		System.out.println(dayOfMonth);
		System.out.println(hourOfDay);
		System.out.println(minute);
	
	}
}

		
		
