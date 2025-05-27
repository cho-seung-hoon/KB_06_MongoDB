package sec03;

import app.Database;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Iterator;

public class FindTest {
    public static void main(String[] args) {
        MongoCollection<Document> collection = Database.getCollection("todo");

        //FindIterable =
        FindIterable<Document> doc = collection.find();

        //반복자 형태로 변환
        Iterator itr = doc.iterator();

        while (itr.hasNext()) { // 다음 행이 있으면 true
            //itr.next() : 다음 행(문서) 반환
            System.out.println("==> findResultRow : " + itr.next());
        }

        Database.close();
    }
}
