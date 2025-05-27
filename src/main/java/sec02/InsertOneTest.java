package sec02;

import app.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

public class InsertOneTest {
    public static void main(String[] args) {

        //Database 유틸리티 클래스를 이용해서 todo 컬렉션(테이블) 연결 객체 얻어오기
        MongoCollection<Document> collection = Database.getCollection("todo");

        //문서(행)을 정의하는 객체 생성(BSON)
        Document document = new Document();

        //문서 객체에 필드(열) 추가
        document.append("title", "MongoDB");
        document.append("desc", "MongoDB 공부하기");
        document.append("done", false);

        //컬렉션에 문서 1개 삽입 후 결과 객체(InsertOneResult) 반환 받기
        InsertOneResult result = collection.insertOne(document);

        result.getInsertedId();

        //생성된 문서의 _id 필드값 얻어오기 (ObjectId, 문자열 x)
        System.out.println("==> InsertOneResult : " + result.getInsertedId());

        // 데이터베이스 연결 종료 및 리소스 정리
        Database.close();
    }
}
