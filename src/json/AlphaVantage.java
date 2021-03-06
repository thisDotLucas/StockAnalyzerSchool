package json;

import com.sun.org.apache.bcel.internal.generic.JsrInstruction;
import org.json.JSONObject;
import prog3.AlertBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AlphaVantage {

    public JSONObject object;

    //gets json data och konverterar det till en string och sparar det i variabeln object
    public String getJson(String key, String interval, String timeSer, String symbol, String size /*, int index*/) throws Exception {

        try {
            String url;
            if (size.equals("&outputsize="))
                url = "https://www.alphavantage.co/query?function=" + timeSer + symbol + "&apikey=0OPBQ9QM2UDDW9TD";
            else
                url = "https://www.alphavantage.co/query?function=" + timeSer + symbol + interval + size + "&apikey=0OPBQ9QM2UDDW9TD";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            object = new JSONObject(response.toString());

            return getUpdate(object, key, interval);

        } catch (Exception e) {

            AlertBox.display("Alert", "Fill all choice boxes.");

        }

        return "";

    }


    //Returnerar den informationen som användaren vill ha från Json stringen
    public static String getUpdate(JSONObject object, String key, String interval) {

        JSONObject objects = null;
        String outp = "Showing " + key + "\n";
        interval = interval.replace("&interval=", "");

        if (interval.equals("1min") || interval.equals("5min") || interval.equals("15min") || interval.equals("30min") || interval.equals("60min"))
            objects = object.getJSONObject("Time Series (" + interval + ")");
        else
            objects = object.getJSONObject(interval);

        Iterator<String> it = objects.keys();
        ArrayList<String> dates = new ArrayList<>();

        while (it.hasNext()) {
            dates.add(it.next());
        }

        Collections.sort(dates);

        for (String date : dates) {

            if (objects.get(date) instanceof JSONObject) {

                JSONObject values = objects.getJSONObject(date);
                outp += "Date: " + date + ": " + values.getString(key) + "\n";

            }

        }

        return outp;

    }


    private static String sortData(String x) {



        return "";
    }
}


