package foundation.webservice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 * @author 沈志鹏
 *
 */
public class GeocodeService {

	public static JSONObject geocodeAddr(double lat, double lng) {

		String urlString = "http://maps.google.com/maps/api/geocode/json?latlng="
				+ lat + "," + lng + "&sensor=true&language=zh-CN";
		StringBuilder sTotalString = new StringBuilder();
		try {

			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			InputStream urlStream = httpConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlStream));

			String sCurrentLine = "";
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
				System.out.println();
			}
			//System.out.println(sTotalString);
			bufferedReader.close();
			httpConnection.disconnect(); // 关闭http连接

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(sTotalString.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
	/**
	 * 
	 * @param lat
	 * @param lng
	 * @param i 0街道，1区，2市，3国家
	 * @return
	 */
	public static String getAddressByLatLng(double lat, double lng,
			int i) {
		String address = null;
		JSONObject jsonObject = geocodeAddr(lat, lng);
		try {
			JSONArray placemarks = jsonObject.getJSONArray("results");
			JSONObject place = placemarks.getJSONObject(0);
			JSONArray detail = place.getJSONArray("address_components");
			JSONObject add = detail.getJSONObject(i);
			address = add.getString("long_name");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return address;
	}

}
