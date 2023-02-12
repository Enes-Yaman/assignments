import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


public class PropertyJsonReader {
    public static Map<Integer,Gamemap> properties = new HashMap<>();

    public PropertyJsonReader(){
        JSONParser processor = new JSONParser();
        try (Reader file = new FileReader("property.json")){
            JSONObject jsonfile = (JSONObject) processor.parse(file);
            JSONArray Land = (JSONArray) jsonfile.get("1");
            for(Object i:Land){
                properties.put(Integer.parseInt((String) ((JSONObject) i).get("id")), new Land((String)((JSONObject)i).get("name"),Integer.parseInt((String)((JSONObject)i).get("cost"))));}
            JSONArray RailRoad = (JSONArray) jsonfile.get("2");
            for(Object i:RailRoad){
                properties.put(Integer.parseInt((String) ((JSONObject) i).get("id")), new RailRoad((String)((JSONObject)i).get("name"),Integer.parseInt((String)((JSONObject)i).get("cost"))));}
            JSONArray Company = (JSONArray) jsonfile.get("3");
            for(Object i:Company){
                properties.put(Integer.parseInt((String) ((JSONObject) i).get("id")), new Company((String)((JSONObject)i).get("name"),Integer.parseInt((String)((JSONObject)i).get("cost"))));}
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }
        properties.put(1,new NotBuyablePlaces("Go",0));
        properties.put(3,new NotBuyablePlaces("Community Chest",1));
        properties.put(5,new NotBuyablePlaces("Income Tax",2));
        properties.put(8,new NotBuyablePlaces("Chance",3));
        properties.put(11,new NotBuyablePlaces("Jail",4));
        properties.put(18,new NotBuyablePlaces("Community Chest",1));
        properties.put(21,new NotBuyablePlaces("Free Parking",0));
        properties.put(23,new NotBuyablePlaces("Chance",3));
        properties.put(31,new NotBuyablePlaces("Go to Jail",4));
        properties.put(34,new NotBuyablePlaces("Community Chest",1));
        properties.put(37,new NotBuyablePlaces("Chance",3));
        properties.put(39,new NotBuyablePlaces("Super Tax",2));
    }
}