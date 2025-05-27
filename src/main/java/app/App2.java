package app;

import app.domain.Todo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class App2 {
    public static void main(String[] args) {

        MongoCollection<Todo> collection = Database.getCollection("todo", Todo.class);

        //1개 삽입
        //insertTodo(collection, "수업 시간에 집중하기", "딴짓 그만");
        
        //여러 행 삽입
        //inserManyTodo(collection, "샘플", 5);

        // 모두 조회
        //selectAllTodo(collection);


        //_id 일치하는 Todo 1개 출력
        //selectTodo(collection, "68354e7396c0db39c3a6b727");
        
        // _id 일치하는 Todo의 done 수정
        //updateTodo(collection, "68354e7396c0db39c3a6b72a", true);

        // 일괄 done 수정
        //updateAllTodo(collection, true);

        //_id 일치하는 문서 삭제
        deleteTodo(collection, "68354e7396c0db39c3a6b72a");

        // 모든 문서삭제
        deleteAllTodo(collection);

        Database.close();
    }


    //Todo 문서 한개 삽입
    private static void insertTodo(MongoCollection<Todo> collection, String title, String desc) {
        // Todo 객체 생성
        Todo todo = new Todo(null, title, desc, false);

        // Insert 후 결과 객체 반환 받기
        InsertOneResult result = collection.insertOne(todo);

        System.out.println("todo 추가 성공 ==> _id = " + result.getInsertedId());
    }

    
    //다중 Todo 삽입
    private static void insertManyTodo(MongoCollection<Todo> collection, String str, int count) {
        
        //str을 제목, 설명ㅇ로 같는 샘플 Todo 문서 count 만큼 삽입하기

        List<Todo> todoList = new ArrayList<>(); // 빈 List 생성
        for (int i = 0; i < count; i++) {
            Todo todo = new Todo(null, str + i, str + i + "설명", false);
            todoList.add(todo); //List에 추가
        }

        InsertManyResult result = collection.insertMany(todoList);

        System.out.println("샘플 데이터 추가 성공 : " + result.getInsertedIds());
    }

    // 모든 Todo 조회
    private static void selectAllTodo(MongoCollection<Todo> collection) {
        List<Todo> todoList = new ArrayList<>();
        collection.find().into(todoList);

        todoList.forEach(System.out::println);
    }


    private static void selectTodo(MongoCollection<Todo> collection, String id) {

        // 쿼리 셀렉터(조건) 생성
        Bson query = Filters.eq("_id", new ObjectId(id));

        Todo todo = collection.find(query).first();

        if(todo == null) {
            System.out.println("_id가 일치하는 문서가 없습니다.");
        }

        System.out.println("_id : " + todo.getId());
        System.out.println("Title : " + todo.getTitle());
        System.out.println("Desc : " + todo.getDesc());
        System.out.println("Class : " + todo.getClass());
    }

    // id가 일치하는 Todo 문서의 done 값을 수정
    public static void updateTodo(MongoCollection<Todo> collection, String id, boolean done) {

        // 쿼리 셀렉터(조건) 생성
        Bson query = Filters.eq("_id", new ObjectId(id));
        // 업데이트할 내용 (set, unset 등)
        Bson update = Updates.set("done", done);

        // 실행 후 결과 객체 반환 (updateOne(), UpdateResult )
        // -> 수정된 문서의 개수 얻을 수 있음
        UpdateResult result = collection.updateOne(query, update);

        // 개수 == 0
        // 실패 메시지 출력
        if(result.getModifiedCount() == 0) {
            System.out.println("일치하는 _id를 가지는 문서가 없거나 수정 사항 없음");
            return;
        }

        // 개수 > 0
        // 성공 메시지 출력+ selectTodo(collection, id) 호출
        System.out.println("===수정 성공=====");
        selectTodo(collection, id);
    }


    /*
    * 모든 Todo 문서의 done 값 변경하기
    * @param collection
    * @param done
    */
    private static void updateAllTodo(MongoCollection collection, boolean done) {

        // db.todo.updateMany({}, {$set : {done:true}});

        Bson query = Filters.empty(); // 빈 조건(쿼리 셀렉터) {} 생성
        Bson update = Updates.set("done", done);

        // 여러 문서 수정
        UpdateResult result = collection.updateMany(query, update);

        if(result.getModifiedCount() == 0) {
            System.out.println("변경 사항 없음");
            return;
        }
        System.out.println("=== 일괄 수정 성공 ===");
        selectAllTodo(collection);
    }

    private static void deleteTodo(MongoCollection<Todo> collection, String id) {
        Bson query = Filters.eq("_id", new ObjectId(id));
        DeleteResult result = collection.deleteOne(query);

        if (result.getDeletedCount() == 0) {
            System.out.println("일치하는 _id 없음");
            return;
        }

        System.out.println("=== 삭제 성공 ====");
    }

    private static void deleteAllTodo(MongoCollection<Todo> collection) {
        Bson query = Filters.empty(); //조건 없음
        //모든 문서 삭제
        DeleteResult result = collection.deleteMany(query);

        System.out.println("삭제된 문서의 개수 : " + result.getDeletedCount());
    }

}
