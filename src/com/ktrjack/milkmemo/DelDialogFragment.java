package com.ktrjack.milkmemo;

import java.util.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
//Add ViewPager 20140430
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
//Add end

public class DelDialogFragment extends DialogFragment {
	private View milkList;
	private ListView listView;
	private FragmentActivity fa;
	long id;
	private String dbid;
	private String datetime;

	public DelDialogFragment(){
	}

	public DelDialogFragment(
			final View milkList, 
			final ListView listView, 
			final FragmentActivity fa, 
			final long id, 
			final String dbid, 
			final String datetime){

		this.milkList = milkList;
		this.listView = listView;
		this.fa = fa;
		this.id = id;
		this.dbid = dbid;
		this.datetime = datetime;
	}
/*	public static DelDialogFragment newInstance(int title) {
        	DelDialogFragment frag = new DelDialogFragment();
        	Bundle args = new Bundle();
        	args.putInt("title", title);
        	frag.setArguments(args);
        	return frag;
    	}
*/
 	@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
		//アラートダイアログ生成
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(milkList.getContext());
		alertBuilder.setTitle(fa.getText(R.string.alrt_title));
		alertBuilder.setMessage(fa.getText(R.string.alrt_message));

		BtnDeleteOk btnOk = new BtnDeleteOk(milkList, listView, fa, id, dbid, datetime);
		BtnDeleteCancel btnCancel = new BtnDeleteCancel();
		alertBuilder.setPositiveButton("はい", btnOk);
		alertBuilder.setNegativeButton("キャンセル", btnCancel);
		alertBuilder.setCancelable(true);
		return alertBuilder.create();
	
		//alertBuilder.show();

	}
}

/**
 * 確認ダイアログで「はい」押下時のイベント
 * 
 * @param View milkList 
 * @param ListView milkList
 * @param FragmentActivity fa
 * @param long id
 * @param String dbid
 * @param String datetime
 *
 */
class BtnDeleteOk implements DialogInterface.OnClickListener{
	//DBオブジェクト
	private SQLiteDatabase db;
	private MilkMemoDbBeans dbbeans;
	private View milkList;
	private ListView listView;
	private FragmentActivity fa;
	long id;
	private String dbid;
	private String datetime;
	// ADD 20140430
	// PagerAdapter実装
	MilkSwipePagerAdapter mMilkSwipePagerAdapter;
	ViewPager mViewPager;

	public BtnDeleteOk(
			final View milkList, 
			final ListView listView, 
			final FragmentActivity fa, 
			final long id, 
			final String dbid, 
			final String datetime){

		this.milkList = milkList;
		this.listView = listView;
		this.fa = fa;
		this.id = id;
		this.dbid = dbid;
		this.datetime = datetime;
	}

	
	//ToDo OKボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//データ削除-----------------------------------------------
		dbbeans = new MilkMemoDbBeans(listView.getContext());
		db = dbbeans.getWritableDatabase();
		//DBオブジェクトセット
		dbbeans.setDatabase(db);
		 //　データ削除関数呼び出し
		dbbeans.deleteData(dbid); 
		db.close();
		
		Toast.makeText(milkList.getContext(), "日時:" + datetime + " のデータを削除しました。", Toast.LENGTH_LONG).show();
		//---------------------------------------------------------
		//現在のタブを再描画
		//Fragmentオブジェクト設定
		Fragment fragment = new TabHistory();
		
		//バンドルクラス生成
		Bundle args = new Bundle();
		
		//タブ位置を保存
		args.putInt(TabHistory.ARG_SECTION_NUMBER, 2);
		
		//フラグメントにバンドルをセット
		fragment.setArguments(args);

		//メインアクティビティのインスタンスからFragmentManageerを生成
		//現在のタブを再描画する
		//fa.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		//

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
		mViewPager.setCurrentItem(1);	//カレントタブ

	}
}
class BtnDeleteCancel implements DialogInterface.OnClickListener{
	//ToDo キャンセルボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//Do nothing	
	}
}
