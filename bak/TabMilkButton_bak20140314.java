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
import android.widget.TimePicker;
import android.widget.DatePicker;

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
	public static final int INT_WATER = 2;
	public static final int INT_RICE = 3;

	public static final String STR_MILK = "ミルク";
	public static final String STR_UNCHI = "うんち";
	public static final String STR_WATER = "バケツ水交換";
	public static final String STR_RICE = "お米";

	//フラグメントアクティビティ用インスタンス変数
	FragmentActivity fa;

	//constructor
	public TabMilkButton() {
	}

	public static final String ARG_SECTION_NUMBER = "section_number";
	
	/**
	 * onAttach処理で親クラスのアクティビティを取得する
	 */
	/*@Override
	public void onAttach(Activity ac){
		this.fa = (FragmentActivity)ac;
	}*/
	/**
	 * 各ボタン生成、リスナーの登録
	 */
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentActivity fa = getActivity();

		View inputTab = inflater.inflate(R.layout.frag_milk_button, null);

		//ミルク時間ボタン押下時
		//ミルク時間ボタンオブジェクト生成
		Button inputMilkBtn = (Button)inputTab.findViewById(R.id.add_milk_time);
		//うんち時間ボタンオブジェクト生成
		Button inputUnchiBtn = (Button)inputTab.findViewById(R.id.add_unchi_time);
		//水交換時間ボタンオブジェクト生成
		Button inputWaterBtn = (Button)inputTab.findViewById(R.id.add_water_time);
		//お米時間ボタンオブジェクト生成 add 20140311
		Button inputRiceBtn = (Button)inputTab.findViewById(R.id.add_rice_time);

		ClickAction milkClick = new ClickAction(INT_MILK, fa);
		ClickAction unchiClick = new ClickAction(INT_UNCHI, fa);
		ClickAction waterClick = new ClickAction(INT_WATER, fa);
		ClickAction riceClick = new ClickAction(INT_RICE, fa);
		
		inputMilkBtn.setOnClickListener(milkClick);
		inputUnchiBtn.setOnClickListener(unchiClick);
		inputWaterBtn.setOnClickListener(waterClick);
		inputRiceBtn.setOnClickListener(riceClick);


		return inputTab;
	}
	

}

/**
* ボタンクリック時アクションクラス
*
*/

class ClickAction implements OnClickListener{
	
	private int btnKind;
	//アクティビティ
	FragmentActivity fa;
	
	//コンストラクタ
	public ClickAction(final int btnKind, final FragmentActivity fa){
		this.btnKind = btnKind;
		this.fa = fa;
	}
	
	@Override
	public void onClick(View v) {
		//ダイアログ表示
		showDateTimeDialog(v,this.btnKind);
	}

	/**
	 *  日時ダイアログ表示メソッド
	 */
	private void showDateTimeDialog(View v, int btnKind){
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
		System.out.println(calendar.get(Calendar.YEAR));
		dateTimeDialog.setArguments(args);

		/**
		 *  コールバックに選択された日時をセット
		 */
		CallBackListener callOnDateTimeSet = new CallBackListener(v, btnKind);
		dateTimeDialog.setCallBack(callOnDateTimeSet);
		dateTimeDialog.show(fa.getSupportFragmentManager(), "Date Time Picker");
		

	}
}
/**
 * コールバックリスナー実装クラス
 * ボタン押下時処理を行う
 */
class CallBackListener implements DateTimePickerDialog.OnDateTimeSetListener{
	View v;
	int btnKind;
	public CallBackListener(final View v, final int btnKind){
		this.v = v;
		this.btnKind = btnKind;
	}

	public void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute){
		//ユーザが入力したダイアログ情報が引数に入ってくる
		//日時フォーマットをDB挿入用に整形
		//yearパラメータは0=1900年なので実際の年から1900を減算する
		//Date date = new Date( year - 1900, monthOfYear, dayOfMonth, hourOfDay, minute);
		Calendar calendar = Calendar.getInstance();
		calendar.set( year, monthOfYear, dayOfMonth, hourOfDay, minute);
		Date date = calendar.getTime();


		//DBオブジェクト
		SQLiteDatabase db;
		MilkMemoDbBeans dbbeans;
		
		//新規データ入力
		dbbeans = new MilkMemoDbBeans(v.getContext());
		db = dbbeans.getWritableDatabase();
		//DBオブジェクトセット
		dbbeans.setDatabase(db);
		
		switch(this.btnKind){
		case TabMilkButton.INT_MILK:
			//SQL insert実行
			dbbeans.insertDateTable(TabMilkButton.STR_MILK, date);  
			break;
		case TabMilkButton.INT_UNCHI:
			dbbeans.insertDateTable(TabMilkButton.STR_UNCHI, date);  
			break;
		case TabMilkButton.INT_WATER:
			dbbeans.insertDateTable(TabMilkButton.STR_WATER, date);  
			break;
		// お米ボタン押下 add 20140311
		case TabMilkButton.INT_RICE:
			dbbeans.insertDateTable(TabMilkButton.STR_RICE, date);  
			break;
		}

		//DBオブジェクトクローズ
		db.close();
	}
}

		
		
