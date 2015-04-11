package com.ktrjack.milkmemo;

import android.app.*;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class AddButton extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_button_main);
		//ボタン生成
		Button addBtn = (Button)findViewById(R.id.btn_add_button);
		//ボタンアクション生成
		AddBtnClick addBtnClick = new AddBtnClick(this);
		//ボタンにイベントを付与
		addBtn.setOnClickListener(addBtnClick);

	}
}
/**
 * ボタン追加ボタン押下時処理
 */
class AddBtnClick implements View.OnClickListener{
    	//DBオブジェクト
	protected SQLiteDatabase db;
	protected ButtonDbBeans dbbeans;
	//アクティビティ
	protected Activity ac;

	AddBtnClick(final Activity ac){
		this.ac = ac;
	}

        @Override
	public void onClick(View view) {
	/*
	 * 	ボタン押下時の処理
	 * 	ToDo
	 * 	ボタンDBにデータインサート
	*/
        	EditText editAddBtn = (EditText)ac.findViewById(R.id.edit_add_button);
		String strBtnName = editAddBtn.getText().toString();
	        				
		if ( strBtnName.length() > 0 ){
	        	//新規ボタンデータ入力
	        	dbbeans = new ButtonDbBeans(view.getContext());
	        	db = dbbeans.getWritableDatabase();
			dbbeans.setDatabase(db);
	        	dbbeans.insertButtonMaster(strBtnName);  
	      		db.close();
			//データ挿入が成功したことを表示するトーストを焼く
			Toast.makeText(view.getContext(), "新規ボタンを生成しました！", Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent();
			intent.setClassName(ac.getApplicationContext(),"com.ktrjack.milkmemo.MilkMemo");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ac.startActivity(intent);
        	}else {
			//テキストボックスが空の場合の警告メッセージ
        		Toast.makeText(view.getContext(), "ボタン名を入力してください", Toast.LENGTH_SHORT).show();
        	}

	}
}


