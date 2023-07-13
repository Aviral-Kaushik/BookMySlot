package com.aviral.bookmyslot;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.aviral.bookmyslot.Adapter.TimeSlotAdapter;
import com.aviral.bookmyslot.Models.TimeModel;
import com.aviral.bookmyslot.Models.TimeSlot;
import com.aviral.bookmyslot.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AviralJSON";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {

            InputStream inputStream = getAssets().open("time.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
            String currentDay = days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];

            splitTimeSlots(json, currentDay);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: Exception while getting json file: " + e.getMessage());
        }

        binding.datePicker.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            try {

                                InputStream inputStream = getAssets().open("time.json");
                                int size = inputStream.available();
                                byte[] buffer = new byte[size];
                                inputStream.read(buffer);
                                inputStream.close();
                                String json = new String(buffer, StandardCharsets.UTF_8);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                                String dayString = simpleDateFormat.format(date);

                                binding.selectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                splitTimeSlots(json, dayString);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onCreate: Exception while getting json file: " + e.getMessage());
                            }

                        }
                    },
                    year, month, day);

            datePickerDialog.show();
        });

    }

    private void splitTimeSlots(String json, String currentDay) {

        ArrayList<TimeModel> timeModels = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(json);

            JSONObject teacherObject = jsonObject.getJSONObject("teacher");

            binding.username.setText(teacherObject.getString("name"));

            binding.aboutMe.setText(teacherObject.getString("description"));

            binding.ratings.setText(jsonObject.getString("rating"));

            Glide.with(this)
                    .load(teacherObject.getString("image"))
                    .into(binding.profilePicture);

            JSONObject object = jsonObject.getJSONObject("timeslot");

            JSONArray jsonArray = object.getJSONArray(currentDay);

            for (int i = 0; i < jsonArray.length(); i++) {

                String[] time = jsonArray.getString(i).replace(" ", "").split("-");

                ArrayList<TimeSlot> timeSlots = new ArrayList<>();

                timeSlots.add(new TimeSlot(Integer.parseInt(time[0]),
                        Integer.parseInt(time[0]), 15));

                timeSlots.add(new TimeSlot(Integer.parseInt(time[0]),
                        timeSlots.get(0).getInterval() + 5,
                        timeSlots.get(0).getInterval() + 5 + 15));

                timeSlots.add(new TimeSlot(Integer.parseInt(time[0]),
                        timeSlots.get(1).getInterval() + 5,
                        timeSlots.get(1).getInterval() + 5 + 15));


                JSONObject objectBooking = jsonObject.getJSONObject("bookedTimings");

//                JSONArray bookedTimingsArray = objectBooking.getJSONArray("Thursday");
                JSONArray bookedTimingsArray = objectBooking.getJSONArray(currentDay);

                boolean isCurrentSlotBooked = false;

                if (bookedTimingsArray.length() != 0) {

                    for(int j = 0; j < bookedTimingsArray.length(); j++) {

                        String[] bookedTimings =
                                bookedTimingsArray.getString(j).replace(" ", "").split("-");

                        String[] bookedStartTimeArray = bookedTimings[0].split(":");
                        String[] bookedEndTimeArray = bookedTimings[1].split(":");

                        for (int z = 0; z < 3; z++) {

                            if (String.valueOf(timeSlots.get(z).getInitialTime())
                                    .equals(bookedStartTimeArray[0]) &&
                                    (timeSlots.get(z).getInterval() - 15)
                                            < Integer.parseInt(bookedEndTimeArray[1])) {

                                isCurrentSlotBooked = true;

                            }

                        }

                    }

                }

                timeModels.add(new TimeModel(
                        timeSlots.get(0).getInitialTime()
                                + ":" + (timeSlots.get(0).getInterval() - 15) + "-"
                                + timeSlots.get(0).getInitialTime()
                                + ":" + timeSlots.get(0).getInterval(),
                        isCurrentSlotBooked
                ));

//                Log.d(TAG, "splitTimeSlots: time: "
//                        + timeSlots.get(0).getInitialTime()
//                        + ":" + timeSlots.get(0).getInterval()
//                        + " & " +timeSlots.get(0).getInitialTime()
//                        + ":" +(timeSlots.get(0).getInterval() - 15));
//
//                Log.d(TAG, "splitTimeSlots: time: "
//                        + timeSlots.get(1).getInitialTime()
//                        + ":" + timeSlots.get(1).getInterval()
//                        + " & " +timeSlots.get(1).getInitialTime()
//                        + ":" +(timeSlots.get(1).getInterval() - 15));
//
//                Log.d(TAG, "splitTimeSlots: time: "
//                        + timeSlots.get(2).getInitialTime()
//                        + ":" + timeSlots.get(2).getInterval()
//                        + " & " +timeSlots.get(2).getInitialTime()
//                        + ":" +(timeSlots.get(2).getInterval() - 15));

//                Log.d(TAG, "splitTimeSlots: timeslots: " + timeSlots.get(0).getStartTime()
//                        + ":" + timeSlots.get(0).getTime() + "-"
//                        + timeSlots.get(0).getInterval());
//
//                Log.d(TAG, "splitTimeSlots: timeslots: "
//                        + timeSlots.get(0).getStartTime() + "-" + timeSlots.get(1).getTime() + "-"
//                        + timeSlots.get(1).getInterval());
//
//                Log.d(TAG, "splitTimeSlots: timeslots: "
//                        + timeSlots.get(0).getStartTime() + "-" + timeSlots.get(2).getTime() + "-"
//                        + timeSlots.get(2).getInterval());

            }

            setUpTimeAdapter(timeModels);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: Exception while splitting time: " + e.getMessage());
        }
    }

    private void setUpTimeAdapter(ArrayList<TimeModel> timeModels) {

        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(timeModels);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        binding.timeRecyclerView.setLayoutManager(gridLayoutManager);

        binding.timeRecyclerView.setAdapter(timeSlotAdapter);

    }
}