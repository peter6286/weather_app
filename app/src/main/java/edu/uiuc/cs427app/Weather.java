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

    /**
     * Retrieves the current temperature.
     * @return The temperature in fahrenheit degrees.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature to the specified value.
     * @param temperature The temperature in degrees to set.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the description of the current weather conditions.
     * @return A string describing the weather.
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Sets the weather description to the specified text.
     * @param weatherDescription The descriptive text of the current weather conditions.
     */
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    /**
     * Retrieves the current humidity level.
     * @return The humidity level as a percentage.
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity level to the specified value.
     * @param humidity The humidity level as a percentage to set.
     */
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    /**
     * Retrieves the current wind speed.
     * @return The wind speed.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Sets the wind speed to the specified value.
     * @param windSpeed The wind speed to set.
     */
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Retrieves the current wind direction.
     * @return A string representing the direction of the wind.
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * Sets the wind direction to the specified value.
     * @param windDirection The direction from which the wind is blowing.
     */
    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
}