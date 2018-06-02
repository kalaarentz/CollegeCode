import java.util.List;
import java.util.Map;

/**
 * Flight Path Interface
 *  
 * @author J. Hursey
 */
public interface FlightPath {

    /**
     * Load a file of city data. File contains entries of the form:
     * CITY_NAME  longitude  latitude
     * For example:
     *   Charlotte  -80.83  35.2
     *   San Nicolas de los Garza   -100.3  25.75
     *   Paris  2.34    48.86
     *   
     * An undirected, weighted graph should be generated with edges between
     * cities that can be reached with a direct flight. A direct flight
     * between two cities is possible if the two cities are no further apart
     * than 'maxDist'.
     * 
     * @param filename File containing city data
     * @param maxDist Maximum distance between cities for direct flights. Value greater than or equal to 1000.
     * @throws IllegalArgumentException if maxDist is less than 1000.
     */
    public void loadCities( String filename, double maxDist );
    
    /**
     * Same as the loadCities method above, except the maxDist is set to 2000 miles.
     * 
     * @param filename File containing city data
     */
    public void loadCities( String filename );
    
    /**
     * Test if the specified city is contained in the graph.
     * 
     * @param cityName name of the city
     * @return true if the city is in the graph, false otherwise
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public boolean contains( String cityName );

    /**
     * Test if the graph is fully connected.
     * An empty graph is treated as being connected.
     * 
     * @return true if every vertex is reachable from every other vertex in the graph, false otherwise
     */
    public boolean isConnected();
    
    /**
     * Access the true distance between the two cities.
     * This uses the Haversine Formula to calculate the distance.
     * The cities do not have to be connected in the graph.
     * This method will help you test your implementation of this formula.
     * 
     * @param citySource The source city
     * @param cityDestination The destination city
     * @return The true distance between the two cities
     * @throws IllegalArgumentException if either the source or destination cities are not contained in the graph.
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public double getTrueDistance( String citySource, String cityDestination );

    /**
     * Return the ordered path between the source and destination cities.
     * The list returned will contain the 'citySource' as the first element, and
     * 'cityDestination' as the last element. The city names that follow from the first element
     * will be the next hop in the graph towards the destination.
     * 
     * If there is no path from the source to the destination then this method will return
     * the empty list (not null).
     * 
     * It is possible that the source and destination cities are the same. In which case this 
     * method will return a list containing exactly one entry, that of the source city.
     * 
     * @param citySource The source city
     * @param cityDestination The destination city
     * @return A list of cities traversed from the source city to the destination city or an empty list if no such path.
     * @throws IllegalArgumentException if either the source or destination cities are not contained in the graph.
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public List<String> getPath( String citySource, String cityDestination );

    /**
     * Similar to the getPath method, this will calculate the shortest path between the two
     * cities. Instead of returning a list of those cities, it will return the total distance traveled
     * along the path between those cities.
     * 
     * If there is no path from the source to the destination then this method will return
     * the value Double.POSITIVE_INFINITY.
     * 
     * It is possible that the source and destination cities are the same. In which case this 
     * method will return the value of 0.
     * 
     * @param citySource The source city
     * @param cityDestination The destination city
     * @return The total distance traveled between the two cities or Double.POSITIVE_INFINITY if no such path
     * @throws IllegalArgumentException if either the source or destination cities are not contained in the graph.
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public double getPathDistance( String citySource, String cityDestination );
    
    /**
     * Given a source city calculate the shortest path to all other cities in the graph
     * returning a Map of those paths. It will not calculate the distance from the source
     * city to itself.
     * 
     * If there is no path from the source to a specific destination then an empty list is added
     * to the map (similar to what would have been returned from getPath).
     * 
     * The 'key' of the Map will be the destination city with the value being the shortest
     * path from the source city to the destination city (as would be returned from getPath).
     * 
     * @param citySource The source city
     * @return Map of paths to all other cities in the graph
     * @throws IllegalArgumentException if the source city is not contained in the graph.
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public Map<String, List<String>> getAllPaths( String citySource );
    
    /**
     * Find a route that tours all of the 'visiting' cities starting and ending at the 'origin' city.
     * The 'visiting' cities can be visited in any order.
     * 
     * For the assignment this can be any such route. You may pursue a bonus variation that add additional
     * constraints on the type of route returned.
     * 
     * The list returned will start and end with the 'origin' city. The path extending from the origin
     * city will be the path between each pair of cities seen in the visiting array.
     * 
     * If one or more cities in the 'visiting' array cannot be reached by some path from the origin then 
     * this method will return the empty list (not null).
     * 
     * 
     * Bonus: (two versions with increasing complexity)
     *  v1: Find the shortest such route
     *  v2: Find the shortest such route that does not visit any of the 'visiting' cities more than once
     *      including passing through the airport on the way to another location.
     * ** Make sure you clearly identify which bonus version you implemented, if any **
     *  
     * @param cityOrigin Origin city
     * @param visting List of cities to visit
     * @return A full path visiting all of the reachable cities starting and ending with the origin.
     * @throws IllegalArgumentException if any of the cities are not contained in the graph.
     * @throws IllegalStateException if loadCities was not called before this method, or loadCities did not successfully load the file.
     */
    public List<String> getTour( String cityOrigin, List<String> visting );

}
