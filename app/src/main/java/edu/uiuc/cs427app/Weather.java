package edu.uiuc.cs427app;


/**
 * Represents weather conditions for a specific location.
 * This class holds data about the temperature, weather (sunny, partly sunny, cloudy, partly cloudy, foggy, etc.),
 * humidity, and wind conditions.
 *
 * @author Yafeng Liu
 * @version 10/31/2023
 */
public class Weather {
    private double temperature;
    private String weatherDescription;
    private double humidity;
    private double windSpeed;
    private String windDirection;

    /**
     * Creates a new Weather instance.
     *
     * @param temperature     The current temperature.
     * @param weatherDescription A description of the weather condition.
     * @param humidity        The current humidity level as a percentage.
     * @param windSpeed       The current wind speed.
     * @param windDirection   The direction from which the wind is coming.
     */
    public Weather(double temperature, String weatherDescription, double humidity, double windSpeed, String windDirection) {
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    // Getters and setters for each instance
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
}