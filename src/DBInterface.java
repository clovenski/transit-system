import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;

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

    @SuppressWarnings("unchecked")
    public static List<String> getSchedule(String startLocName, String destName, String date) {
        ArrayList<String> schedule, condition;
        ArrayList<Bson> pipeline;
        ArrayList<Document> offerings;
        Bson stage1, stage2;
        AggregateIterable<Document> results;
        MongoCursor<Document> it;
        Document currentResult;
        String row;

        schedule = new ArrayList<String>();
        pipeline = new ArrayList<Bson>();
        condition = new ArrayList<String>();
        condition.add("$$offering._id.Date");
        condition.add(date);
        stage1 = new Document()
            .append("$match", new Document()
                .append("StartLocationName", startLocName)
                .append("DestinationName", destName)
                .append("offerings._id.Date", date)
            );
        stage2 = new Document()
            .append("$addFields", new Document()
                .append("offerings", new Document()
                    .append("$filter", new Document()
                        .append("input", "$offerings")
                        .append("as", "offering")
                        .append("cond", new Document()
                            .append("$eq", condition)
                        )
                    )
                )
            );
        pipeline.add(stage1);
        pipeline.add(stage2);
        results = collectionMap.get("trips").aggregate(pipeline);
        it = results.iterator();
        while (it.hasNext()) {
            currentResult = it.next();
            offerings = (ArrayList<Document>) currentResult.get("offerings");
            for (Document offering : offerings) {
                row = date + "\t" + startLocName + "\t" + destName + "\t";
                row += ((Document)offering.get("_id")).getString("ScheduledStartTime") + "\t";
                row += offering.getString("ScheduledArrivalTime") + "\t";
                row += offering.getString("DriverName") + "\t";
                row += offering.getInteger("BusID").toString();
                schedule.add(row);
            }
        }
        return schedule;
    }

    public static List<String> getTripInfo(int tripNum) {
        ArrayList<String> tripInfo = new ArrayList<String>();
        FindIterable<Document> results;
        MongoCursor<Document> it;
        Document currentResult;
        Document trip;
        String row;

        trip = new Document()
            .append("_id.TripNumber", tripNum);
        results = collectionMap.get("tripStopInfo").find(trip);
        it = results.iterator();
        while (it.hasNext()) {
            currentResult = it.next();
            row = ((Document)currentResult.get("_id")).getInteger("StopNumber").toString() + "\t";
            row += currentResult.getInteger("SequenceNumber").toString() + "\t";
            row += currentResult.getInteger("DrivingTime").toString();
            tripInfo.add(row);
        }
        return tripInfo;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getDriverSchedule(String driverName, String date) {
        ArrayList<String> schedule, condition1, condition2;
        ArrayList<Bson> pipeline, conditions;
        ArrayList<Document> offerings;
        Bson stage1, stage2;
        AggregateIterable<Document> results;
        MongoCursor<Document> it;
        Document currentResult;
        String row, tripNumStr, startLocStr, destinationStr;

        schedule = new ArrayList<String>();
        pipeline = new ArrayList<Bson>();
        conditions = new ArrayList<Bson>();
        condition1 = new ArrayList<String>();
        condition2 = new ArrayList<String>();
        condition1.add("$$offering._id.Date");
        condition1.add(date);
        condition2.add("$$offering.DriverName");
        condition2.add(driverName);
        conditions.add(new Document().append("$eq", condition1));
        conditions.add(new Document().append("$eq", condition2));
        stage1 = new Document()
            .append("$match", new Document()
                .append("offerings.DriverName", driverName)
                .append("offerings._id.Date", date)
            );
        stage2 = new Document()
            .append("$addFields", new Document()
                .append("offerings", new Document()
                    .append("$filter", new Document()
                        .append("input", "$offerings")
                        .append("as", "offering")
                        .append("cond", new Document()
                            .append("$and", conditions)
                        )
                    )
                )
            );
        pipeline.add(stage1);
        pipeline.add(stage2);
        results = collectionMap.get("trips").aggregate(pipeline);
        it = results.iterator();
        while (it.hasNext()) {
            currentResult = it.next();
            tripNumStr = ((Document)currentResult.get("_id")).getInteger("TripNumber").toString();
            startLocStr = currentResult.getString("StartLocationName");
            destinationStr = currentResult.getString("DestinationName");
            offerings = (ArrayList<Document>) currentResult.get("offerings");
            for (Document offering : offerings) {
                row = tripNumStr + "\t" + startLocStr + "\t" + destinationStr + "\t";
                row += ((Document)offering.get("_id")).getString("ScheduledStartTime") + "\t";
                row += offering.getString("ScheduledArrivalTime") + "\t";
                row += offering.getInteger("BusID").toString();
                schedule.add(row);
            }
        }
        return schedule;
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

    public static boolean addFullTripInfo(int tripNum,
                                          String date,
                                          String startTime,
                                          int stopNum,
                                          String arrivalTime,
                                          String realStartTime,
                                          String realArrivalTime,
                                          int numPassengersIn,
                                          int numPassengersOut) {
        // return true if trip info was updated properly OR added if doesn't exist
        // false otherwise
        
        Document tripInfo = new Document()
        	.append("_id", new Document()
                .append("TripNumber", tripNum)
                .append("Date", date))
            .append("ScheduledStartTime", startTime)
            .append("StopNumber", stopNum)
            .append("ScheduledArrivalTime", arrivalTime)
            .append("ActualStartTime", realStartTime)
            .append("ActualArrivalTime", realArrivalTime)
            .append("NumberOfPassengerIn", numPassengersIn)
            .append("NumberOfPassengerOut", numPassengersOut);
            	
            	
        return true; // dummy code
    }

    public static boolean addTripStopInfo(int tripNum, int stopNum, int seqNum, int drivingTime) {
        Document tripStop = new Document()
            .append("_id", new Document().append("TripNumber", tripNum).append("StopNumber", stopNum))
            .append("SequenceNumber", seqNum)
            .append("DrivingTime", drivingTime);
        try {
            collectionMap.get("tripStopInfo").insertOne(tripStop);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean deleteDriver(String name) {
        // delete driver from db according to given driver name
        // also need to set the field in any offerings in any trip that contains
        // this driver; ie. set that field to NULL 
        
        Document driver = new Document()
        	.append("_id", new Document().append("DriverName", name));
        
        try{
        	collectionMap.get("drivers").deleteOne(driver);
        }catch (Exception e){
        	return false;
        }
        
        /**
        boolean driverFound = collectionMap.get("trips").find(
            and(eq("_id"), elemMatch("offerings", new Document().append("_id", new Document().append("DriverName", name)))));
            
            if(driverFound){
            	try{
            		collectionMap.get("trips").updateOne(eq("_id", new Document().append("$addToSet", new Document().append("DriverName", null))));
            		}
                catch(Exception e){
                
            }
        }
        **/
            	
        return true; // dummy code
    }

    public static boolean deleteBus(int id) {
        // delete bus from db according to given bus ID
        // also need to set the field in any offerings in any trip that contains
        // this bus; ie. set that field to NULL
        
         Document bus = new Document()
        	.append("_id", new Document().append("BusID", id));
        
        try{
        	collectionMap.get("buses").deleteOne(bus);
        }catch (Exception e){
        	return false;
        }
        
        return true; // dummy code
    }

    public static boolean deleteStop(int stopNum) {
        // delete stop from db according to given stop number
        // CASCADE this delete operation onto the stop info field in any
        // offerings in any trip that contains this stop, also CASCADE
        // the delete onto any trip-stop info that contains this stop
        Document stop = new Document()
        	.append("_id", new Document().append("StopNumber", stopNum));
        
        try{
        	collectionMap.get("stops").deleteOne(stop);
        }catch (Exception e){
        	return false;
        }
        return true; // dummy code
    }

    public static boolean deleteTrip(int tripNum) {
        // delete trip from db according to given trip number
        // also CASCADE this delete operation to any trip-stop info that contains
        // this trip
        Document trip = new Document()
        	.append("_id", new Document().append("TripNumber", tripNum));
        
        try{
        	collectionMap.get("trips").deleteOne(trip);
        }catch (Exception e){
        	return false;
        }
        
        return true; // dummy code
    }

    public static boolean deleteOffering(int tripNum, String date, String startTime) {
        // delete offering from db with the specified trip number, date and start time
        // note that trip offerings are nested into a field of the trips,
        // so delete the appropriate offering from their "offerings" field
        Document offering = new Document()
            .append("_id", new Document()
            	.append("TripNumber", tripNum)
                .append("Date", date)
                .append("ScheduledStartTime", startTime));
        
        try{
        	collectionMap.get("trips").deleteOne(offering);
        }catch (Exception e){
        	return false;
        }
        
        return true; // dummy code
    }

    public static boolean updateDriver(int tripNum, String date, String startTime, String newDriverName) {
        // update the appropriate offering's "DriverName" field to newDriverName
        // return true if successfully updated, false otherwise
        return true; // dummy code
    }

    public static boolean updateBus(int tripNum, String date, String startTime, int newBusID) {
        // update the appropriate offering's "BusID" field to newBusID
        // return true if successfully updated, false otherwise
        return true; // dummy code
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

    public static boolean stopsExist() {
        return collectionMap.get("stops").count() > 0;
    }

    public static boolean containsTrip(int tripNum) {
        return collectionMap.get("trips").count(
            eq("_id.TripNumber", tripNum)
        ) > 0;
    }

    public static boolean containsDriver(String name) {
        return collectionMap.get("drivers").count(
            eq("_id.DriverName", name)
        ) > 0;
    }

    public static boolean containsBus(int id) {
        return collectionMap.get("buses").count(
            eq("_id.BusID", id)
        ) > 0;
    }

    public static boolean containsStop(int stopNum) {
        return collectionMap.get("stops").count(
            eq("_id.StopNumber", stopNum)
        ) > 0;
    }
}