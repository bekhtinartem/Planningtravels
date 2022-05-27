package com.example.planningtravels;

import java.util.List;
class Weather {
    public float now;
    public String now_dt;
    public Info info;
    public Fact fact;
    public List<Forecasts> forecasts;
}

class Info{
    public double lat;
    public double lon;
    public Tzinfo tzinfo;
    public float def_pressure_mm;
    public float def_pressure_pa;
    public String url;
}

class Tzinfo {
    public float offset;
    public String name;
    public String abbr;
    public boolean dst;
}

class Fact{
    public float temp;
    public float feels_like;
    public float temp_water;
    public String icon;
    public String condition;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public String daytime;
    public boolean polar;
    public String season;
    public float obs_time;
    public boolean is_thunder;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
    public String phenom_icon;
    public String phenom_condition;
}

class Forecasts{
    public String date;
    public  float date_ts;
    public float week;
    public String sunrise;
    public String sunset;
    public float moon_code;
    public String moon_text;
    public Parts parts;
    public Night night;
    public DayShort dayShort;
    public float temp;
    public List<hours> hours;
}

class Parts{
    public Night night;
    public Morning morning;
    public Day day;
    public Evening evening;
    public DayShort dayShort;
    public NightShort nightShort;
}

class Night{
    public float temp_min;
    public float temp_max;
    public float temp_avg;
    public float feels_like;
    public String icon;
    public String condition;
    public String daytime;
    public boolean polar;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_mm;
    public float prec_period;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
}

class Morning{
    public float temp_min;
    public float temp_max;
    public float temp_avg;
    public float feels_like;
    public String icon;
    public String condition;
    public String daytime;
    public boolean polar;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_mm;
    public float prec_period;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
}

class Day{
    public float temp_min;
    public float temp_max;
    public float temp_avg;
    public float feels_like;
    public String icon;
    public String condition;
    public String daytime;
    public boolean polar;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_mm;
    public float prec_period;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
}

class Evening{
    public float temp_min;
    public float temp_max;
    public float temp_avg;
    public float feels_like;
    public String icon;
    public String condition;
    public String daytime;
    public boolean polar;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_mm;
    public float prec_period;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
}

class DayShort{
    public float temp;
    public float temp_min;
    public float feels_like;
    public String icon;
    public String condition;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_type;
    public float prec_strength;
    public float cloudness;
}

class NightShort{
    public float temp;
    public float feels_like;
    public String icon;
    public String condition;
    public float wind_speed;
    public float wind_gust;
    public String wind_dir;
    public float pressure_mm;
    public float pressure_pa;
    public float humidity;
    public float prec_type;
    public float prec_strength;
}

