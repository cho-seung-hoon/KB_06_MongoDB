package sec04;

import app.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;

public class UpdateOneTest {
    public static void main(String[] args) {
        MongoCollection<Document> collection = Database.getCollection("todo");

        String id = "68353e6e77be559b728e5c24";
        Bson query = eq("_id", new ObjectId(id));

        // Updates.combine() : 업데이트할 내용을 묶는 메서드
        // Updates.set("name", "modify name") : name 필드 값을 "modify name"으로 설정
        // Updates.currentTimestamp("LastUpdated") : lastUpdated 필드 값으로 현재 시간 (currentTimestatmp) 설정
        Bson updates = Updates.combine(
                Updates.set("name", "modify name"),
                Updates.currentTimestamp("lastUpdated")
        );

        UpdateResult result = collection.updateOne(query, updates);
        System.out.println("==> UpdateResult : " + result.getModifiedCount());

        Database.close();
    }
}
