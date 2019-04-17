import com.mongodb.client.*;
import com.mongodb.MongoClient;
import org.bson.Document;
import java.util.Iterator;

public class Lab4 {
    public static void main( String args[] ) {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        // Creating Credentials
        // MongoCredential credential;
        // credential = MongoCredential.createCredential("sampleUser", "myDb",
        //     "password".toCharArray());
        System.out.println("Connected to the database successfully");
        // Accessing the database
        MongoDatabase database = mongo.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("testCollection");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}