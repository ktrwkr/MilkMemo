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
public class MilkMemoDbBeans extends SQLiteOpenHelper {
	
  /** クラス内定数定義 **/
	private static final String DB_NAME = "milkmemo.sqlite";
	private static final String TABLE = "tb_milkmemo";
	private static final int DB_VERSION = 1;
	private static final String CREATE_TABLE = "create table " + TABLE + "("
		+"_id integer primary key autoincrement, "
		+"event TEXT not null, "
		+"datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP);";
	/**
	*	インスタンスメンバ定義
	*/
	private SQLiteDatabase db;
	private ArrayList<ArrayList> allrowdata = new ArrayList<ArrayList>();
	/**
	 * @param context
	 * コンストラクタ
	 */
	
    public MilkMemoDbBeans(Context context){
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
	public MilkMemoDbBeans(Context context,	String name, 
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
	public MilkMemoDbBeans(Context context, String name,
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
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);

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
	 * @param db
	 * @param event
	 * 
	 * データ入力クラス
	 */
    public void insertTable(String event) {
    	

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String datetime = sdf.format(date); 
        ContentValues cv=new ContentValues();

        
        cv.put("event", event);
        cv.put("datetime", datetime);
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
    public void deleteData(String dbid) {

        try{  

        	long rtid=db.delete(TABLE , "_id = " + dbid , null);
        	if(rtid<0){
		        	Log.e("addevent", "データ削除に失敗");
        	}

        }catch (SQLException e) {  
            	Log.e("ERROR DELETE", e.toString());
        }  


    }
    /*
     * 
     *　配列で表示行をセレクト 改
	 * 返り値　String[][]
     */
    public String[][] arSlctTableData() {
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
        StringBuilder sql = new StringBuilder();  
        sql.append(" SELECT");
        sql.append(" _id");
        sql.append(" ,event");
        sql.append(" ,datetime");    
        sql.append(" FROM "+ TABLE +" order by _id desc;"); 
        String strList = new String();
        String strId;
      	String strEvent;
        String strDatetime;
        String[][] str = new String[2][cnt];
     // バインド先のレイアウト要素のリソースID
        //int[] to = {R.id.item1, R.id.item2};	
        
        int i = 0;
        
        try{ 
        	//SQL実行、結果をカーソルに保存
            Cursor cursor = db.rawQuery(sql.toString(), null);
        	//結果のカラム数を取得
        	int colcnt =  cursor.getColumnCount();
 
            while (cursor.moveToNext()){
            	// 各カラム値取得
            	strId = String.valueOf(cursor.getInt(0)); 
            	strEvent = cursor.getString(1);
            	strDatetime = cursor.getString(2);
          	//1次元目にID、2次元目にイベントと日付日時を設定する	
            	str[0][i] = strId;
            	str[1][i] = strEvent + " " + strDatetime;
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
