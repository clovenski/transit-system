import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import org.bson.Document;

class DBInterface {
    /*
     * class to be used by Engine to interact with the mongodb database,
     * construct methods here that will interact with the mongodb and return necessary outputs
     */

    private static MongoDatabase db;
    private static HashMap<String, MongoCollection<Document>> collectionMap;

    static {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        db = new MongoClient("localhost", 27017).getDatabase("transitSystem");
        collectionMap = new HashMap<String, MongoCollection<Document>>();
        collectionMap.put("trips", db.getCollection("trips"));
        collectionMap.put("buses", db.getCollection("buses"));
        collectionMap.put("drivers", db.getCollection("drivers"));
        collectionMap.put("stops", db.getCollection("stops"));
        collectionMap.put("tripStopInfo", db.getCollection("tripStopInfo"));
    }

    public static boolean addDriver(String name, String phoneNumber) {
        Document driver = new Document()
            .append("_id", new Document().append("DriverName", name))
            .append("DriverTelephoneNumber", phoneNumber);
        try {
            collectionMap.get("drivers").insertOne(driver);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean addBus(int id, String model, int year) {
        Document bus = new Document()
            .append("_id", new Document().append("BusID", id))
            .append("Model", model)
            .append("Year", year);
        try {
            collectionMap.get("buses").insertOne(bus);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean addStop(int stopNum, String address) {
        Document stop = new Document()
            .append("_id", new Document().append("StopNumber", stopNum))
            .append("StopAddress", address);
        try {
            collectionMap.get("stops").insertOne(stop);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean addTrip(int tripNum, String startLocName, String destinationName) {
        Document trip = new Document()
            .append("_id", new Document().append("TripNumber", tripNum))
            .append("StartLocationName", startLocName)
            .append("DestinationName", destinationName);
        try {
            collectionMap.get("trips").insertOne(trip);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean addOffering(int tripNum, String date, String startTime, String arrivalTime, String driverName, int busID) {
        Document offering = new Document()
            .append("_id", new Document()
                .append("Date", date)
                .append("ScheduledStartTime", startTime))
            .append("ScheduledArrivalTime", arrivalTime)
            .append("DriverName", driverName)
            .append("BusID", busID);
        boolean alreadyExists = collectionMap.get("trips").find(
            and(eq("_id", new Document().append("TripNumber", tripNum)),
                elemMatch("offerings", new Document().append("_id", new Document().append("Date", date).append("ScheduledStartTime", startTime)))
            )
        ).first() != null;
        if (alreadyExists) {
            return false;
        } else {
            try {
                return collectionMap.get("trips").updateOne(
                    eq("_id", new Document().append("TripNumber", tripNum)),
                    new Document().append("$addToSet", new Document()
                        .append("offerings", offering))
                ).getModifiedCount() > 0;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static boolean tripsExist() {
        return collectionMap.get("trips").count() > 0;
    }

    public static boolean driversExist() {
        return collectionMap.get("drivers").count() > 0;
    }

    public static boolean busesExist() {
        return collectionMap.get("buses").count() > 0;
    }

    public static boolean containsTrip(int tripNum) {
        return collectionMap.get("trips").count(
            new Document().append("_id", new Document().append("TripNumber", tripNum))
        ) > 0;
    }

    public static boolean containsDriver(String name) {
        return collectionMap.get("drivers").count(
            new Document().append("_id", new Document().append("DriverName", name))
        ) > 0;
    }

    public static boolean containsBus(int id) {
        return collectionMap.get("buses").count(
            new Document().append("_id", new Document().append("BusID", id))
        ) > 0;
    }
}