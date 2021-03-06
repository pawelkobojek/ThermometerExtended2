package pl.narfsoftware.thermometer.utils;

import pl.narfsoftware.thermometer.R;
import pl.narfsoftware.thermometer.ThermometerApp;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Preferences implements OnSharedPreferenceChangeListener {

	Context context;
	SharedPreferences preferences;

	// TODO getters and setters...
	public String timeFormat = DEFAULT_TIME_FORMAT;
	public String dateFormat = DEFAULT_DATE_FORMAT;
	static final String DEFAULT_TIME_FORMAT = "KK:mm a";
	static final String DEFAULT_DATE_FORMAT = "EEEE, dd MMMM";

	public String temperatureUnit;
	public int temperatureUnitCode;
	public static final int CELSIUS = 0;
	public static final int FAHRENHEIT = 1;
	public static final int KELVIN = 2;

	public int backgroundColor;
	static final int BACKGROUND_DEFAULT_COLOR = Color.parseColor("#FFF0F8FF");
	private final String BACKGROUND_DEFAULT_COLOR_STRING = "#FFF0F8FF";

	public String fontTypeface;
	static final String DEFAULT_FONT_TYPEFACE = "Roboto.ttf";
	public Typeface typeface;

	public String theme;
	static final String DEFAULT_THEME = "";
	public Drawable themeDrawable;

	public boolean[] showAmbientCondition = new boolean[ThermometerApp.AMBIENT_CONDITIONS_COUNT];

	public Preferences(Context context) {
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.registerOnSharedPreferenceChangeListener(this);
		setAmbientConditionsToShow();
		setCustomizationPreferences();
	}

	private void setAmbientConditionsToShow() {
		showAmbientCondition[ThermometerApp.TEMPERATURE_INDEX] = preferences
				.getBoolean(
						context.getResources().getString(
								R.string.ambient_temp_key), true);

		showAmbientCondition[ThermometerApp.RELATIVE_HUMIDITY_INDEX] = preferences
				.getBoolean(
						context.getResources().getString(
								R.string.relative_humidity_key), true);
		showAmbientCondition[ThermometerApp.ABSOLUTE_HUMIDITY_INDEX] = preferences
				.getBoolean(
						context.getResources().getString(
								R.string.absolute_humidity_key), false);
		showAmbientCondition[ThermometerApp.PRESSURE_INDEX] = preferences
				.getBoolean(
						context.getResources().getString(R.string.pressure_key),
						true);
		showAmbientCondition[ThermometerApp.DEW_POINT_INDEX] = preferences
				.getBoolean(
						context.getResources()
								.getString(R.string.dew_point_key), false);
		showAmbientCondition[ThermometerApp.LIGHT_INDEX] = preferences
				.getBoolean(context.getResources()
						.getString(R.string.light_key), false);
		showAmbientCondition[ThermometerApp.MAGNETIC_FIELD_INDEX] = preferences
				.getBoolean(
						context.getResources().getString(
								R.string.magnetic_field_key), false);
	}

	private void setCustomizationPreferences() {
		// set background color
		backgroundColor = (Color.parseColor(preferences.getString(context
				.getResources().getString(R.string.prefs_background_color_key),
				BACKGROUND_DEFAULT_COLOR_STRING)));

		// set temperature unit
		temperatureUnit = preferences
				.getString(
						context.getResources().getString(
								R.string.prefs_temp_unit_key),
						context.getResources().getStringArray(
								R.array.prefs_temp_unit_vals)[0]);
		if (temperatureUnit.equals(context.getResources().getStringArray(
				R.array.prefs_temp_unit_vals)[CELSIUS]))
			temperatureUnitCode = CELSIUS;

		else if (temperatureUnit.equals(context.getResources().getStringArray(
				R.array.prefs_temp_unit_vals)[FAHRENHEIT]))
			temperatureUnitCode = FAHRENHEIT;

		else
			temperatureUnitCode = KELVIN;

		// set date and time format
		dateFormat = preferences.getString(
				context.getResources()
						.getString(R.string.prefs_date_format_key),
				DEFAULT_DATE_FORMAT);
		timeFormat = preferences.getString(
				context.getResources()
						.getString(R.string.prefs_time_format_key),
				DEFAULT_TIME_FORMAT);

		// set font typeface
		fontTypeface = preferences.getString(
				context.getResources().getString(R.string.prefs_font_key),
				DEFAULT_FONT_TYPEFACE);
		typeface = Typeface.createFromAsset(context.getAssets(), fontTypeface);

		// set theme
		theme = preferences.getString(
				context.getResources().getString(R.string.prefs_theme_key),
				DEFAULT_THEME);
		if (!theme.equals("")) {
			try {
				themeDrawable = context.getResources().getDrawable(
						context.getResources().getIdentifier(
								theme,
								"drawable",
								context.getApplicationContext()
										.getPackageName()));
			} catch (OutOfMemoryError e) {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.out_of_memory_error_toast),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		preferences = sharedPreferences;
		// TODO Set only setting with this particular key (?)
		setAmbientConditionsToShow();
		setCustomizationPreferences();
	}

}
