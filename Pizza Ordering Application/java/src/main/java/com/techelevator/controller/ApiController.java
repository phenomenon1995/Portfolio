package com.techelevator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.transform.OutputKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
@Service
public class ApiController {
    private final String MAPBOX_TOKEN = "pk.eyJ1IjoiZGF3aWxsaWFtczA3MjciLCJhIjoiY2x6bXUyOHdzMGkxeDJtcHVvZGZrb2RkcyJ9.YuUmVJT4QerDA9k-Hz3t_A";
    private final String GEOCODE_TOKEN = "66b647062c5e6532299318zax27ff3d";
    private final int MAX_MILES = 15;
    private final int MAX_DURATION_SECONDS = 1800;
    RestTemplate http = new RestTemplate();
    /**
     *    RETURN STRUCTURED AS
     *         [
     *             {
     *                 "latitude": "value",
     *                     "longitude": "value"
     *             },
     *             {
     *                 "latitude": "value",
     *                     "longitude": "value"
     *             },
     *         ]
     * @param addresses: the addresses
     * @return the converted coordinates
     * */

    public List<Map<String, String>> convertAddressToCoordinates(List<Map<String, String>> addresses){
        List<Map<String,String>> convertedCoordinates = new ArrayList<>();
        System.out.println("Created New list to hold cnverted coordinates");

        Map<String, Object> mapBoxObject = new HashMap<>();
        for (Map<String, String> address : addresses){
            String[] streetAddressComponents = address.get("street_address").split(" ");
            String[] cityComponents = address.get("city").split(" ");
            String joinedAddress = "";
            String addressLine1 = "";
            String city = "";
            String stateAbbreviation = "";
            String zipcode = "";
            for(String component : streetAddressComponents){
                addressLine1 += component + "%20";
            }
            for(String component: cityComponents){
                city += component + "%20";
            }
            stateAbbreviation += address.get("state_abbreviation");
            zipcode += address.get("zip_code");

            String convertAddressUrl = String.format("https://api.mapbox.com/search/geocode/v6/forward?address_line1=%1$s&place=%2$s&region=%3$s&postcode=%4$s&limit=%5$s&access_token=%6$s",
                    addressLine1,city,stateAbbreviation,zipcode,1,MAPBOX_TOKEN);

            System.out.println("Convert Address URL: "+convertAddressUrl);
            mapBoxObject = http.getForEntity(
                    convertAddressUrl,
                    Map.class).getBody();

            Map<String,String> thisCoordinates = new HashMap<>();
            List<Map<String, Object>> features = (List<Map<String, Object>>)mapBoxObject.get("features");
            Map<String, Map<String, Object>> geometry = (Map<String, Map<String, Object>>)features.get(0).get("geometry");
            List<Double> coordinates = (List<Double>)geometry.get("coordinates");
            thisCoordinates.put("longitude", coordinates.get(0).toString()) ;
            thisCoordinates.put("latitude", coordinates.get(1).toString());
            convertedCoordinates.add(thisCoordinates);
        }
        System.out.println(convertedCoordinates);
        return convertedCoordinates;
    }

    @RequestMapping(path = "/validate", method=RequestMethod.POST)
    public ResponseEntity<Boolean> validateAddress(@RequestBody List<Map<String, String>> addresses){
    /* EXAMPLE FORMAT FOR REQUEST BODY
        [
                {
                    "street_address": "1349 SOM Center Road",
                        "city": "Mayfield Heights",
                        "state_abbreviation":"OH",
                        "zip_code":"44124"
                },1349 SOM Center Road
                {
                    "street_address": "5867 Mayfield Road",
                        "city": "Mayfield Heights",
                        "state_abbreviation":"OH",
                        "zip_code":"44124"
                }
        ]
         */

        Map<String, Object> mapBoxObject = null;
        System.out.println("About to convert addresses to coordinates");
        List<Map<String,String>> convertedCoordinates = convertAddressToCoordinates(addresses);
        System.out.println("Converted Address to coordinates");
        Map<String, String> from = convertedCoordinates.get(0);
        Map<String, String> to = convertedCoordinates.get(1);
//        System.out.println(String.format("%s,%s;%s,%s",
//                from.get("longitude"), from.get("latitude"), to.get("longitude"), to.get("latitude")));

        /*Durations as an array of arrays that represent the matrix in row-major order. durations[i][j] gives the
         travel time from the ith source to the jth destination. All values are in seconds. The duration between the
          same coordinate is always 0. Finding no duration, the result will be null.
        * */
        String checkDurationUrl = String.format(
                "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/%1$s,%2$s;%3$s,%4$s?access_token=%5$s",
                from.get("longitude"), from.get("latitude"), to.get("longitude"), to.get("latitude"),
               MAPBOX_TOKEN);

        Double duration = null; //gets duration from destination at index 0, to index 1
        boolean inRange;
        try {
            System.out.println("Check Duration Url: "+checkDurationUrl);
            mapBoxObject = http.getForObject(
                    checkDurationUrl,
                    Map.class);
            System.out.println(mapBoxObject);
            List<List<Double>> durations = (List<List<Double>>)mapBoxObject.get("durations");
            duration = durations.get(0).get(1);
            System.out.println(duration);
            inRange = duration < MAX_DURATION_SECONDS;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID ADDRESSES");
        }

        return new ResponseEntity<>( inRange? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ResponseEntity<Object> test (){
        String  url = "https://api.mapbox.com/search/geocode/v6/forward?q=6503%20Marsol%20Road%20OH%2044124&limit=1&access_token=pk.eyJ1IjoiZGF3aWxsaWFtczA3MjciLCJhIjoiY2x6bXUyOHdzMGkxeDJtcHVvZGZrb2RkcyJ9.YuUmVJT4QerDA9k-Hz3t_A";

        Object object =  http.getForEntity(
                url,
                Object.class).getBody();
        return new ResponseEntity<Object>(object,HttpStatus.OK);
    }
}
