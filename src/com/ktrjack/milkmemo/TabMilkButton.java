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
import android.widget.LinearLayout;
import android.widget.ScrollView;
//Add ViewPager 20140430
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
//Add end

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

	//DBオブジェクト
	SQLiteDatabase db;
	ButtonDbBeans dbbeans;

	//constructor
	public TabMilkButton() {
	}

	public static final String ARG_SECTION_NUMBER = "section_number";
	
	/**
	 * 各ボタン生成、リスナーの登録
	 */
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentActivity fa = getActivity();
		//メモ・継承したクラスであれば戻り値にできる
		//inputTabをViewGroupにしてあとからビューを追加する
		ViewGroup inputTab = (ViewGroup)inflater.inflate(R.layout.frag_milk_button, null);
	    
		ScrollView scrollView = new ScrollView(fa);
    		// View に ScrollView を設定
    		inputTab.addView(scrollView);

		//ボタンを配置するリニアレイアウトを設定	
        	LinearLayout linearLayout = new LinearLayout(inputTab.getContext());
        	linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);

		//ボタンラベル文字列の生成
		/*
		ArrayList<String> btnLabel = new ArrayList<String>();
		btnLabel.add(STR_MILK);
		btnLabel.add(STR_UNCHI);
		btnLabel.add(STR_WATER);
		btnLabel.add(STR_RICE);
		*/

		//ボタンラベルをDBテーブルから生成 add 20140318
		dbbeans = new ButtonDbBeans(inputTab.getContext());
		db = dbbeans.getWritableDatabase();
		dbbeans.setDatabase(db);
		ArrayList<String> btnLabel = dbbeans.selectButtonMaster();
		db.close();

		Button[] inputBtn = new Button[btnLabel.size()];

		//ループでボタンを生成していく
		for ( int i = 0; i < btnLabel.size(); i++){
			//ボタン表示形式設定
			inputBtn[i] = new Button(inputTab.getContext());
			inputBtn[i].setText(btnLabel.get(i));
			inputBtn[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			
			//クリック時アクション設定
			ClickAction clickAction = new ClickAction(btnLabel.get(i), fa);
			inputBtn[i].setOnClickListener(clickAction);
			
			//ビューに追加	
			linearLayout.addView(inputBtn[i]);
			
		}

/*		

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
*/
		return inputTab;

	}
	/*
	 * 再描画
	 */
	@Override
	public void onResume(){
		super.onResume();
	}
}

/**
* ボタンクリック時アクションクラス
*
*/

class ClickAction implements OnClickListener{
	
	//private int btnKind;
	private String btnLabel;
	//アクティビティ
	FragmentActivity fa;
	
	//コンストラクタ
	//public ClickAction(final int btnKind, final FragmentActivity fa){
	public ClickAction(final String btnLabel, final FragmentActivity fa){

		this.btnLabel = btnLabel;
		this.fa = fa;
	}
	
	@Override
	public void onClick(View v) {
		System.out.println("onClick!!");
		//ダイアログ表示
		showDateTimeDialog(v,this.btnLabel);

	}

	/**
	 *  日時ダイアログ表示メソッド
	 */
	private void showDateTimeDialog(View v, String btnLabel){

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
		CallBackListener callOnDateTimeSet = new CallBackListener(v, btnLabel, fa);
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
	//int btnKind;
	String btnLabel;
	// ADD 20140430
	// PagerAdapter実装
	MilkSwipePagerAdapter mMilkSwipePagerAdapter;
	ViewPager mViewPager;
	FragmentActivity fa;

	public CallBackListener(final View v, final String btnLabel, final FragmentActivity fa){
		this.v = v;
		this.btnLabel = btnLabel;
		this.fa = fa;

	}

	public void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute){
		//ユーザが入力したダイアログ情報が引数に入ってくる
		//日時フォーマットをDB挿入用に整形
		//yearパラメータは0=1900年なので実際の年から1900を減算する
		//※Date型のコンストラクタは使用非推奨だったためCalendarを使用する方法に変更
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
		
		//データインサート
		dbbeans.insertDateTable(btnLabel, date);  

		Toast.makeText(v.getContext(), "新しいデータ「" + btnLabel + "」の時間を追加しました", Toast.LENGTH_LONG).show();
		
		//DBオブジェクトクローズ
		db.close();

		//add 20140430
		mMilkSwipePagerAdapter = new MilkSwipePagerAdapter(fa.getSupportFragmentManager());
		mViewPager = (ViewPager)fa.findViewById(R.id.pager);
		//スワイプでタブ移動したときの処理を追加（タブを選択しなおす）
		mViewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						fa.getActionBar().setSelectedNavigationItem(position);
					}
				});

		mViewPager.setAdapter(mMilkSwipePagerAdapter);
		mViewPager.setCurrentItem(0);	//カレントタブ
	}
}

		
		
