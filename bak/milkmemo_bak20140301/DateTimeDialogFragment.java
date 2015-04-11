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

	public void setCallBack(final DateTimePickerDialog.OnDateTimeSetListener onDateTimeSet){
		this.onDateTimeSet = onDateTimeSet;
	}
	
	@Override
	public void setArguments(Bundle args){
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
		hourOfDay = args.getInt("hourOfDay");
		minute = args.getInt("minute");
		date = new Date( year, month, day, hourOfDay, minute);

	}


	public Dialog onCreateDialog(Bundle savedInstanceState){
		return new DateTimePickerDialog(getActivity(), onDateTimeSet, date, true); 

	}

 }
