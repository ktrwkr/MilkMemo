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
import android.content.DialogInterface;
import android.util.Log;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

/**
 *	ダイアログフラグメント
 */
class AddBtnDialogFragment extends DialogFragment {
	//アクティビティ
	protected Activity ac;

	AddBtnDialogFragment(final Activity ac){
		this.ac = ac;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//アラートダイアログタイトル・本文セット
		builder.setTitle(ac.getText(R.string.alrt_title));
		builder.setMessage(ac.getText(R.string.alrt_message_add_btn));
		//ボタンイベント付与
		BtnAddButtonOk btnAddOk = new BtnAddButtonOk();
		BtnAddButtonCancel btnCancel = new BtnAddButtonCancel();

		builder.setPositiveButton("はい", btnAddOk);
		builder.setNegativeButton("いいえ", btnCancel);
		return builder.create();
	}
}
class BtnAddButtonOk implements DialogInterface.OnClickListener{
	//ToDo はいボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//ToDo Add Event
	}
}
class BtnAddButtonCancel implements DialogInterface.OnClickListener{
	//ToDo キャンセルボタン押下時イベント
	@Override
	public void onClick(DialogInterface dialog, int which){
		//Do nothing	
	}
}
