package com.ktrjack.milkmemo;

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
import java.util.*;
/**
 *  データヒストリーをリスト表示するフラグメントタブ
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
	String[][] arMilkHist;
		
	@Override
	public View onCreateView(	LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View inputTab = inflater.inflate(R.layout.frag_milk_unchi_hist, null);
		//ArrayList<StringBuffer> rowdata = new ArrayList<StringBuffer> ();;
		String[][] arMilkHist;

		StringBuffer tmp;
		//DBオブジェクト生成
		dbbeans = new MilkMemoDbBeans(inputTab.getContext());
		db = dbbeans.getWritableDatabase();
		
		//DBオブジェクトセット
		dbbeans.setDatabase(db);
		//配列型での取得
		//allrowdata = (ArrayList)dbbeans.arSlctTableView();
		arMilkHist = dbbeans.arSlctTableData();
		db.close();
		
		if( arMilkHist != null){
		/*if(allrowdata != null){
			for (int i=0; i<allrowdata.size(); i++){
				tmp = new StringBuffer();
				for ( int j=0; j<allrowdata.get(i).size(); j++){
					tmp.append(allrowdata.get(i).get(j));
					tmp.append(" ");

				}
				rowdata.add(tmp);
			}
		*/
	    		//現在いるタブビューのIDで指定してやる（ハマった）
	    		ListView milkList = (ListView)inputTab.findViewById(R.id.lvHist);
	    		//ArrayAdapterの第一引数は現在のタブのコンテキスト
	    		//ListAdapter la = (ListAdapter) new ArrayAdapter<String>(inputTab.getContext(), android.R.layout.simple_list_item_1, (List)rowdata);
			ListAdapter la = (ListAdapter) new ArrayAdapter<String>(inputTab.getContext(), android.R.layout.simple_list_item_1, arMilkHist[1]);
			//現在のタブビューにセット
			milkList.setAdapter(la);
			LongClick lc = new LongClick(arMilkHist);
			
			milkList.setOnItemLongClickListener(lc);
		}
		
		return inputTab;
	}
	

}
/**
 *  リスト要素のロングクリック時イベント
 *
 *	@param String[][] arMilkHist	DBから取得したid、event+datetimeの2次元配列
 */
class LongClick implements OnItemLongClickListener{
    //DBオブジェクト
	private SQLiteDatabase db;
	private MilkMemoDbBeans dbbeans;
	private String[][] arMilkHist;
	
	//コンストラクタ　引数の配列をインスタントメンバーにセットする
	public LongClick(final String[][] arMilkHist){
		this.arMilkHist = arMilkHist;
	}
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
		String datetime = arItem[1] + arItem[2];
		//テーブルIDの取得
	//	String dbid = arItem[0];
		String dbid = arMilkHist[0][(int)id];
	
		System.out.println(dbid);
		//データ削除
		dbbeans = new MilkMemoDbBeans(listView.getContext());
		db = dbbeans.getWritableDatabase();
		//DBオブジェクトセット
		dbbeans.setDatabase(db);
		 //　データ削除関数呼び出し
		dbbeans.deleteData(dbid); 
		db.close();
		Toast.makeText(milkList.getContext(), "Oh! ID:" + id + " - DateTime:" + datetime + " DELETED!!!", Toast.LENGTH_LONG).show();
//		Toast.makeText(milkList.getContext(), "Oh! No " + id + " DELETED!!!", Toast.LENGTH_LONG).show();
		//現在のタブを再描画

		//Fragmentオブジェクト設定
		Fragment fragment = new TabHistory();
		//バンドルクラス生成
		Bundle args = new Bundle();
		//タブ位置を保存
		args.putInt(TabHistory.ARG_SECTION_NUMBER, 2);
		//args.putInt(MilkMemo.DummySectionFragment.ARG_SECTION_NUMBER, 2);
		//フラグメントにバンドルをセット
		fragment.setArguments(args);

		//メインアクティビティのインスタンスを取得してからFragmentManageer生成
		//MilkMemo.getMainAct().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		//これで動くか？
		milkList.getContext().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

		return false; 
	}
}
