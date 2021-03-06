package com.ktrjack.milkmemo;

import java.util.*;
import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
//import com.ktrjack.milkmemo.*;

 public class DateTimeDialogFragment extends DialogFragment {
	DateTimePickerDialog.OnDateTimeSetListener onDateTimeSet;
	private int year, month, day, hourOfDay, minute; 
	private Date date;

	public DateTimeDialogFragment(){
	}
	/**
	 * コールバック設定メソッド
	 */
	public void setCallBack(final DateTimePickerDialog.OnDateTimeSetListener onDateTimeSet){
		this.onDateTimeSet = onDateTimeSet;
	}
	
	/**
	 * 各日時引数設定
	 */
	@Override
	public void setArguments(Bundle args){
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
		hourOfDay = args.getInt("hourOfDay");
		minute = args.getInt("minute");
		//Date型のコンストラクタが非推奨だったので修正
		Calendar calendar = Calendar.getInstance();
		calendar.set( year, month, day, hourOfDay, minute);
		date = calendar.getTime();
		//date = new Date( year, month, day, hourOfDay, minute);
	}

	/**
	 * DateTimePickerDialogインスタンスを返すファクトリメソッド
	 */
	public Dialog onCreateDialog(Bundle savedInstanceState){
		return new DateTimePickerDialog(getActivity(), onDateTimeSet, date, true); 

	}

 }
