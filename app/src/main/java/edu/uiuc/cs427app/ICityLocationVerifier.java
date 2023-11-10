package edu.uiuc.cs427app;

/**
 * The ICityLocationVerifier interface defines a method for verifying the geographical location of
 * a city. Implementations of this interface should provide a mechanism to validate the details of a
 * given city object, including if the latitude and longitude of the City object is successfully
 * obtained from external city location server.
 *
 * @author Sherry Li
 * @version 11/2/2023
 */
public interface ICityLocationVerifier {
    /**
     * Verifies if the geographical location, including the latitude and the longitude of a given
     * city, is successful obtained from external server.
     * Implementations of this method should handle any exception that arises from the verification
     * process, for example, "API failed" or "Such city does not exist."
     *
     * @param city The city object to be verified. It should contain initial geographical
     *             information like city name, state or region, country name.
     * @return The verified city object.
     * @throws Exception If the verification process fails, or if the city does not exist.
     */
    public City VerifyCityLocation(City city) throws Exception;
}
