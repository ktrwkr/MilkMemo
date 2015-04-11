package com.ktrjack.milkmemo;

import java.util.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.app.DialogFragment;
//import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DelButtonDialogFragment extends DialogFragment {
	private View buttonList;
	private ListView listView;
	private Activity ac;
	long id;
	private String dbid;
	private String buttonName;

	public DelButtonDialogFragment(){
	}

	public DelButtonDialogFragment(
			final View buttonList, 
			final ListView listView, 
			final Activity ac,
			final long id, 
			final String dbid, 
			final String buttonName){

		this.buttonList = buttonList;
		this.listView = listView;
		this.ac = ac;
		this.id = id;
		this.dbid = dbid;
		this.buttonName = buttonName;
	}
 	@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
		//アラートダイアログ生成
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(buttonList.getContext());
		alertBuilder.setTitle(ac.getText(R.string.alrt_title));
		alertBuilder.setMessage(ac.getText(R.string.alrt_message));

		BtnMasterDeleteOk btnOk = new BtnMasterDeleteOk(buttonList, listView, ac, id, dbid, buttonName);
		BtnMasterDeleteCancel btnCancel = new BtnMasterDeleteCancel();

		alertBuilder.setPositiveButton("はい", btnOk);
		alertBuilder.setNegativeButton("キャンセル", btnCancel);
		
		alertBuilder.setCancelable(true);
		
		return alertBuilder.create();
	
	}
}

/**
 * 確認ダイアログで「はい」押下時のイベント
 * 
 * @param View buttonList 
 * @param ListView listView
 * @param FragmentActivity fa
 * @param long id
 * @param String dbid
 * @param String buttonName
 *
 */
class BtnMasterDeleteOk implements DialogInterface.OnClickListener{
	//DBオブジェクト
	private SQLiteDatabase db;
	private ButtonDbBeans dbbeans;
	private View buttonList;
	private ListView listView;
	private Activity ac;
	private FragmentActivity fa;
	long id;
	private String dbid;
	private String buttonName;

	public BtnMasterDeleteOk(
			final View buttonList, 
			final ListView listView, 
			final Activity ac,
			final long id, 
			final String dbid, 
			final String buttonName){

		this.buttonList = buttonList;
		this.listView = listView;
		this.ac = ac;
		this.id = id;
		this.dbid = dbid;
		this.buttonName = buttonName;
	}

	
	//ToDo OKボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//データ削除-----------------------------------------------
		dbbeans = new ButtonDbBeans(listView.getContext());
		db = dbbeans.getWritableDatabase();
		//DBオブジェクトセット
		dbbeans.setDatabase(db);
		 //　データ削除関数呼び出し
		dbbeans.deleteButtonMaster(dbid); 
		db.close();
		
		Toast.makeText(buttonList.getContext(), "「" + buttonName + "」 ボタンを削除しました。", Toast.LENGTH_LONG).show();
		
		//メイン画面に移動
		Intent intent = new Intent();
		intent.setClassName(ac.getApplicationContext(),"com.ktrjack.milkmemo.MilkMemo");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ac.startActivity(intent);
	}
}
class BtnMasterDeleteCancel implements DialogInterface.OnClickListener{
	//ToDo キャンセルボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//Do nothing	
	}
}
