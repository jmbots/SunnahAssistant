package com.thesunnahrevival.sunnahassistant.data.local;

import android.net.Uri;

import com.thesunnahrevival.sunnahassistant.data.model.AppSettings;
import com.thesunnahrevival.sunnahassistant.data.model.HijriDateData.Hijri;
import com.thesunnahrevival.sunnahassistant.data.model.Reminder;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ReminderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("SELECT * FROM reminders_table WHERE timeInSeconds >= :offsetFromMidnight AND ((day == :day AND month == :month AND year == :year) OR (day == :day AND month == 12 AND year == 0) OR day == 0 OR customScheduleDays LIKE '%' || :nameOfTheDay || '%')  ORDER BY isEnabled DESC, timeInSeconds")
    LiveData<List<Reminder>> getUpcomingReminders(long offsetFromMidnight, String nameOfTheDay, int day, int month, int year);

    @Query("SELECT * FROM reminders_table WHERE timeInSeconds <= :offsetFromMidnight AND ((day == :day AND month == :month AND year == :year) OR (day == :day AND month == 12 AND year == 0) OR day == 0 OR customScheduleDays LIKE '%' || :nameOfTheDay || '%')  ORDER BY isEnabled DESC, timeInSeconds")
    LiveData<List<Reminder>> getPastReminders(long offsetFromMidnight, String nameOfTheDay, int day, int month, int year);

    @Query("SELECT * FROM reminders_table WHERE ((day == :day AND month == :month AND year == :year) OR (day == :day AND month == 12 AND year == 0) OR day == 0 OR customScheduleDays LIKE '%' || :nameOfTheDay || '%')  ORDER BY isEnabled DESC, timeInSeconds")
    LiveData<List<Reminder>> getRemindersOnDay(String nameOfTheDay, int day, int month, int year);

    @Query("SELECT * FROM reminders_table WHERE (category == 'Prayer' AND day == :day) ORDER BY timeInSeconds")
    LiveData<List<Reminder>> getPrayerTimes(int day);

    @Query("SELECT * FROM reminders_table WHERE frequency == 'Weekly'")
    LiveData<List<Reminder>> getWeeklyReminders();

    @Query("SELECT * FROM reminders_table WHERE frequency == 'Monthly'")
    LiveData<List<Reminder>> getMonthlyReminder();

    @Query("SELECT * FROM reminders_table WHERE frequency == 'One Time'")
    LiveData<List<Reminder>> getOneTimeReminders();

    @Query("UPDATE reminders_table SET isEnabled =:isEnabled WHERE id ==:id")
    void setEnabled(int id, boolean isEnabled);

    @Query("UPDATE reminders_table SET isEnabled =:isEnabled WHERE reminderName ==:prayerName")
    void setPrayerTimeEnabled(String prayerName, boolean isEnabled);

    @Query("UPDATE reminders_table SET `offset` =:offsetValue, reminderName =:newPrayerName, reminderInfo =:reminderInfo WHERE reminderName == :prayerName")
    void updatePrayerTimeDetails(String prayerName, String newPrayerName, String reminderInfo, int offsetValue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRemindersList(List<Reminder> remindersList);

    @Query("DELETE FROM reminders_table WHERE category == 'Prayer' ")
    void deleteAllPrayerTimes();

    @Query("SELECT * FROM reminders_table WHERE timeInSeconds > :offsetFromMidnight AND ((day == :day AND month == :month AND year == :year) OR (day == :day AND month == 12 AND year == 0) OR day == 0 OR customScheduleDays LIKE '%' || :nameOfTheDay || '%')  ORDER BY isEnabled DESC, timeInSeconds")
    Reminder getNextScheduledReminder(long offsetFromMidnight, String nameOfTheDay, int day, int month, int year);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addHijriDate(List<Hijri> hijriDate);

    @Query("SELECT * FROM hijri_calendar WHERE id = :id")
    LiveData<Hijri> getHijriDate(int id);

    @Query("DELETE FROM hijri_calendar")
    void deleteAllHijriData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(AppSettings settings);

    @Query("SELECT * FROM app_settings")
    LiveData<AppSettings> getAppSettings();

    @Query("SELECT * FROM app_settings")
    AppSettings getAppSettingsValue();

    @Update
    void updateAppSettings(AppSettings appSettings);


    @Query("SELECT showNextReminderNotification FROM app_settings")
    boolean getIsForegroundEnabled();

    @Query("UPDATE reminders_table SET category =:newCategory WHERE category == :deletedCategory")
    void updateCategory(String deletedCategory, String newCategory);

    @Query("UPDATE app_settings SET notificationToneUri =:notificationToneUri, isVibrate =:isVibrate, priority =:priority" )
    void updateNotificationSettings(Uri notificationToneUri, boolean isVibrate, int priority);

}
