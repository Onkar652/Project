package utils;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper 
{

	Context con;

	public PreferenceHelper(Context con)
	{
		this.con = con;
	}


	public static SharedPreferences getSharedSettings(Context con)
	{
		return con.getSharedPreferences(SharedPreferenceConstant.SHARED_PREFERENCE_KEY,0);
	}

	public static void storeUsertype(Context ctx, String custname)
	{
		SharedPreferences.Editor prefEditor = getSharedSettings(ctx).edit();
		prefEditor.putString(SharedPreferenceConstant.USERTYPE, custname);
		prefEditor.commit();
	}

	public static String getUsertype(Context ctx)
	{
		SharedPreferences sp = getSharedSettings(ctx);
		return sp.getString(SharedPreferenceConstant.USERTYPE, null);
	}



}
