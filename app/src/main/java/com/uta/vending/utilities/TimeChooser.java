package com.uta.vending.utilities;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeChooser
	implements View.OnClickListener, TimePickerDialog.OnTimeSetListener
{
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	private TextView editText;
	private Context ctx;
	private Calendar myCalendar;
	private LocalDateTime time;
	private boolean hasTime;

	public TimeChooser(TextView editText, Context ctx)
	{
		this.editText = editText;
		this.ctx = ctx;
		this.editText.setOnClickListener(this);
		this.myCalendar = Calendar.getInstance();
		this.time = LocalDateTime.now()
			.withHour(0)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);
		this.hasTime = false;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		time = time
			.withHour(hourOfDay)
			.withMinute(minute);
		editText.setText(time.format(formatter));
		hasTime = true;
	}

	public LocalDateTime getTime()
	{
		return time;
	}

	public boolean isTimeSet()
	{
		return hasTime;
	}

	@Override
	public void onClick(View v)
	{
		int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = myCalendar.get(Calendar.MINUTE);
		new TimePickerDialog(ctx, this, hour, minute, true).show();
	}
}
