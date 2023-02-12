import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class ListJsonReader{
    public ArrayList chance;
    public ArrayList community;
    public ListJsonReader(){
        JSONParser processor = new JSONParser();
        try (Reader file = new FileReader("list.json")){
            JSONObject jsonfile = (JSONObject) processor.parse(file);
            JSONArray chanceList = (JSONArray) jsonfile.get("chanceList");
            ArrayList<String> chance = new ArrayList<>();
            ArrayList<String> community = new ArrayList<>();
            for(Object i:chanceList){
                chance.add(((String)((JSONObject)i).get("item")));
            }
            JSONArray communityChestList = (JSONArray) jsonfile.get("communityChestList");
            for(Object i:communityChestList){
                community.add(((String)((JSONObject)i).get("item")));
            }
            this.chance = chance;
            this.community = community;
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public ArrayList getChance() {
        return chance;
    }

    public ArrayList getCommunity() {
        return community;
    }
}

