package com.ktrjack.milkmemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ktrjack.milkmemo.TabMilkButton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;
import java.util.*;
//import android.widget.TextView;
import android.widget.Toast;

/**
 * @author kotaroiwakura
 * データベース定義クラス
 */
public class ButtonDbBeans extends SQLiteOpenHelper {
	
  /** クラス内定数定義 **/
	private static final String DB_NAME = "buttons.sqlite";
	private static final String TABLE = "tb_mst_button";
	private static final int DB_VERSION = 1;
	private static final String CREATE_TABLE = "create table " + TABLE + "("
		+"_id integer primary key autoincrement, "
		+"button TEXT not null); ";
	/**
	*	インスタンスメンバ定義
	*/
	private SQLiteDatabase db;
	private ArrayList<ArrayList> allrowdata = new ArrayList<ArrayList>();
	/**
	 * @param context
	 * コンストラクタ
	 */
	public ButtonDbBeans(Context context){
        	super(context, DB_NAME, null, DB_VERSION);
	}
    
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * 
	 * 
	 */
	public ButtonDbBeans(Context context,	String name, 
							CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public ButtonDbBeans(Context context, String name,
			CursorFactory factory,
			int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 * DB生成時に必ず呼び出しされる
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 初回生成時、CREATE TABLE文発行
		db.execSQL(CREATE_TABLE);
		//初期データ投入
		ContentValues cv = new ContentValues();
        	cv.put("button", "ミルク");
		long id=db.insert(TABLE, null, cv);
		if(id<0){
			Log.e("addevent", "ミルク追加に失敗");
		} 
		cv.put("button", "うんち");
		id=db.insert(TABLE, null, cv);
		if(id<0){
			Log.e("addevent", "うんち追加に失敗");
		} 
		cv.put("button", "バケツ水交換");
		id=db.insert(TABLE, null, cv);
		if(id<0){
			Log.e("addevent", "バケツ水追加に失敗");
		} 
		cv.put("button", "お米");
		id=db.insert(TABLE, null, cv);
		if(id<0){
			Log.e("addevent", "お米追加に失敗");
		} 


	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @param db
	 * 
	 * データベースオブジェクト作成セッターメソッド
	 */	
	public void setDatabase(final SQLiteDatabase db) {
		this.db = db;
	}
	/**
	 * 
	 * @param event
	 * 
	 * データ入力クラス
	 */
	public void insertButtonMaster(String button) {
    	
		ContentValues cv = new ContentValues();
        	cv.put("button", button);
		long id=db.insert(TABLE, null, cv);
        	
		if(id<0){
			Log.e("addevent", "データ追加に失敗");
		} 
    	}
    

	/**
	 * 
	 * @param db
	 * @param no
	 * 
	 * データ削除クラス
	 */
	public void deleteButtonMaster(String dbid) {

		try{  
	
			long rtid=db.delete(TABLE , "_id = " + dbid , null);
			if(rtid<0){
				Log.e("addevent", "データ削除に失敗");
			}

		}catch (SQLException e) {  
			Log.e("ERROR DELETE", e.toString());
		}
	}

	/**
	* 
	*　配列で表示行をセレクト
	*
	* @return ArrayList<String>
	*/
	public ArrayList<String> selectButtonMaster() {
    	
		//列取得SQL作成  
		StringBuilder sql = new StringBuilder();  		//SQL用StringBuilder
		sql.append(" SELECT");
		sql.append(" _id");
		sql.append(" , button");  
 		sql.append(" FROM "+ TABLE +" order by _id;"); 
		
		//int Id;
		String strButton;								//結果取得用一時変数
		ArrayList<String> alButton = new ArrayList<String>();		//戻り値用ArrayList
        
		int i = 0;
        
		try{ 
			//SQL実行、結果をカーソルに保存
          		Cursor cursor = db.rawQuery(sql.toString(), null);
			//結果のカラム数を取得
			int colcnt =  cursor.getColumnCount();
 
			while (cursor.moveToNext()){
				// 各カラム値取得 
				//intId = cursor.getInt(0);
				strButton = cursor.getString(1);
				
				//ボタンの文字列をArrayListに追加していく
				alButton.add(strButton);
            			//ボタンの文字列をHashMapに格納していく
				//hmButton.put(intId, strButton);
				i++; 
        
			}

        
		}catch (SQLException e) {  
			Log.e("ERROR SELECT", e.toString());
        
		}finally{ 
        	
			//後処理DB　クローズ
			db.close(); 
		}
		return alButton;
	}
	/**
	* 
	*　２次元配列で表示行をセレクト
	*
	* @return String[][]
	*/
	public  String[][] arSelectButtonMaster() {
		int cnt = 0;
   		//列数確認
		StringBuilder sqlCnt = new StringBuilder(); 
		sqlCnt.append(" SELECT count(*) from "+ TABLE +";");
		try{  
			Cursor cursor = db.rawQuery(sqlCnt.toString(), null);   
            		//TextViewに表示  
 
			while (cursor.moveToNext()){  
				cnt = cursor.getInt(0);
			}  
		}catch (SQLException e) {  
			Log.e("ERROR COUNT", e.toString());
		}


		//列取得SQL作成  
		StringBuilder sql = new StringBuilder();  		//SQL用StringBuilder
		sql.append(" SELECT");
		sql.append(" _id");
		sql.append(" , button");  
 		sql.append(" FROM "+ TABLE +" order by _id;"); 
		
		int intId;
		String strButton;								//結果取得用一時変数
		//ArrayList<String> alButton = new ArrayList<String>();		//戻り値用ArrayList
	        String[][] str = new String[2][cnt];

		int i = 0;
        
		try{ 
			//SQL実行、結果をカーソルに保存
          		Cursor cursor = db.rawQuery(sql.toString(), null);
			//結果のカラム数を取得
			int colcnt =  cursor.getColumnCount();
 
			while (cursor.moveToNext()){
				// 各カラム値取得 
				intId = cursor.getInt(0);
				strButton = cursor.getString(1);
				
				//ボタンの文字列をArrayListに追加していく
				//alButton.add(strButton);
            			//ボタンの文字列をHashMapに格納していく
				//hmButton.put(intId, strButton);
				str[0][i] = String.valueOf(intId);
				str[1][i] = strButton;

				i++; 
        
			}

        
		}catch (SQLException e) {  
			Log.e("ERROR SELECT", e.toString());
        
		}finally{ 
        	
			//後処理DB　クローズ
			db.close(); 
		}
		return str;
	}
}
