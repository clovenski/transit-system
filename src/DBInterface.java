import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import org.bson.Document;

class DBInterface {
    /*
     * class to be used by Engine to interact with the mongodb database,
     * construct methods here that will interact with the mongodb and return necessary outputs
     */

    private MongoDatabase db;
    private HashMap<String, MongoCollection<Document>> collectionMap;

    public DBInterface() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        db = new MongoClient("localhost", 27017).getDatabase("transitsystemdb");
        collectionMap = new HashMap<String, MongoCollection<Document>>();
        collectionMap.put("Trip", db.getCollection("Trip"));
        collectionMap.put("TripOffering", db.getCollection("TripOffering"));
        collectionMap.put("Bus", db.getCollection("Bus"));
        collectionMap.put("Driver", db.getCollection("Driver"));
        collectionMap.put("Stop", db.getCollection("Stop"));
        collectionMap.put("ActualTripStopInfo", db.getCollection("ActualTripStopInfo"));
        collectionMap.put("TripStopInfo", db.getCollection("TripStopInfo"));
    }

    public boolean addDriver(String name, String phoneNumber) {
        Document driver = new Document()
            .append("_id", new Document().append("DriverName", name))
            .append("DriverTelephoneNumber", phoneNumber);
        try {
            collectionMap.get("Driver").insertOne(driver);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addBus(int id, String model, int year) {
        Document bus = new Document()
            .append("_id", new Document().append("BusID", id))
            .append("Model", model)
            .append("Year", year);
        try {
            collectionMap.get("Bus").insertOne(bus);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addTrip(int tripNum, String startLocName, String destinationName) {
        Document trip = new Document()
            .append("_id", new Document().append("TripNumber", tripNum))
            .append("StartLocationName", startLocName)
            .append("DestinationName", destinationName);
        try {
            collectionMap.get("Trip").insertOne(trip);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}