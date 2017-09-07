package cl.jsalgado.reigndesign.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import cl.jsalgado.reigndesign.R;

/**
 * Created by joels on 05-09-2017.
 *
 */

public class Util {

    private static String FILE_NAME = "reign_file.json";
    public static String URL_DATA = "https://hn.algolia.com/api/v1/search_by_date?query=android?page=";

    public static String getTime(String date){
        DateTime now = new DateTime(DateTimeZone.UTC);
        DateTime hitTime = new DateTime(date).withZone(DateTimeZone.UTC);

        int months = Months.monthsBetween(hitTime,now).getMonths();
        int days = Days.daysBetween(hitTime,now).getDays();
        int hours = Hours.hoursBetween(hitTime,now).getHours();
        int min = Minutes.minutesBetween(hitTime,now).getMinutes();

        if(months > 0){
            return months + "m";
        }else if(days > 0){
            return days + "d";
        } else if(hours > 0){
            return hours + "h";
        } else if (min > 0) {
            return min + "m";
        }else {
            return "now";
        }
    }

    public static void saveData(Context context, String data){
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getData(Context context){
        String data = null;
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    context.openFileInput(FILE_NAME)));
            data = fin.readLine();
            fin.close();
            Toast.makeText(context, context.getString(R.string.load_ok_msg), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            if(ex instanceof FileNotFoundException){
                Toast.makeText(context, context.getString(R.string.file_not_found), Toast.LENGTH_SHORT).show();
            }else {
                Log.e(context.getString(R.string.file), context.getString(R.string.file_read_error));
            }
        }
        return data;
    }

    public static boolean isConnected(Context context){
        boolean isConnected = false;
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null){
            isConnected = activeNetwork.isConnectedOrConnecting();
        }
        return  isConnected;
    }

}