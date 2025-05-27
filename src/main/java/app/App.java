package app;

import app.domain.Todo;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

public class App {
    public static void main(String[] args) {
        // POJO 타입 컬렉션 획득
        MongoCollection<Todo> collection = Database.getCollection("todo", Todo.class);
    }

    /**
     * POJO를 이용한 데이터 삽입
     *
     * @param collection
     */
    public static void insertData(MongoCollection<Todo> collection) {
        // 단일 문서 삽입
        Todo newtodo = new Todo(null, "POJO", "POJO 테스트 확인", false);
        collection.insertOne(newtodo);

        // 다중 문서 삽입
        List<Todo> newTodos = Arrays.asList(
                new Todo(null, "POJO2", "POJO2 테스트 확인", false),
                new Todo(null, "POJO3", "POJO3 테스트 확인", true),
                new Todo(null, "POJO4", "POJO4 테스트 확인", false)
        );
        collection.insertMany(newTodos);
    }

    /**
     * POJO를 이용한 데이터 조회
     *
     * @param collection
     */
    public static void selectData(MongoCollection<Todo> collection) {
        // 전체 문서 조회
        List<Todo> todos = new ArrayList<>();
        collection.find().into(todos);  // 결과를 List에 저장

        for (Todo todo : todos) {
            System.out.println(todo);
        }

        // 특정 문서 조회 (ID 기준)
        String id = "666a6296f4fe57189cd03eea";
        Bson query = eq("_id", new ObjectId(id));  // ObjectId로 변환 필요
        Todo todo = collection.find(query).first();
        System.out.println("==> findByIdResult : " + todo);
    }
}
