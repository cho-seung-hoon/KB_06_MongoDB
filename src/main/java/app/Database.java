package app;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/* POJO 관련 import */
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class Database {

    /**
     * MongoDB 클라이언트 인스턴스 (싱글톤)
     * 애플리케이션 전체에서 공유하는 단일 연결 객체
     */
    private static MongoClient client;

    /**
     * MongoDB 데이터베이스 인스턴스 (싱글톤)
     * todo_db 데이터베이스에 대한 접근 객체
     */
    private static MongoDatabase database;

    /**
     * 정적 초기화 블록
     * - 클래스가 로드될 때 한 번만 실행됨
     * - MongoDB 서버 연결 및 데이터베이스 객체 초기화
     * - ConnectionString을 사용한 명시적 연결 설정
     */
    static {


        // MongoDB 연결 문자열 생성 (로컬 서버, IP 주소 명시)
        ConnectionString connectionString = new ConnectionString("mongodb://127.0.0.1:27017");
        // MongoClient 인스턴스 생성
        client = MongoClients.create(connectionString);

        // todo_db 데이터베이스 객체 획득
        // database = client.getDatabase("todo_db");

        /* ================ POJO ================ */
        // POJO 코덱 프로바이더 생성 - 자동 매핑 활성화
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .automatic(true).build();

        // 코덱 레지스트리 생성 - 기본 + POJO 코덱 결합
        CodecRegistry pojoCodecRegistry = fromRegistries(
                getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider)
        );
        // 데이터베이스에 POJO 코덱 레지스트리 적용
        database = client.getDatabase("todo_db").withCodecRegistry(pojoCodecRegistry);
        /* ================ POJO ================ */

    }

    /**
     * MongoDB 연결을 종료하는 메서드
     * - 애플리케이션 종료 시 호출하여 리소스 정리
     * - 클라이언트 연결 해제
     */
    public static void close() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * MongoDB 데이터베이스 인스턴스를 반환하는 메서드
     * - 다른 클래스에서 데이터베이스 객체에 직접 접근할 때 사용
     *
     * @return MongoDatabase todo_db 데이터베이스 객체
     */
    public static MongoDatabase getDatabase() {
        return database;
    }

    /**
     * 지정된 이름의 컬렉션을 반환하는 편의 메서드
     * - 컬렉션 접근을 간소화하는 유틸리티 메서드
     * - Document 타입의 컬렉션 반환
     *
     * @param colName 접근할 컬렉션 이름
     * @return MongoCollection<Document> 지정된 컬렉션 객체
     */
    public static MongoCollection<Document> getCollection(String colName) {
        MongoCollection<Document> collection = database.getCollection(colName);
        return collection;
    }


    /* ================ POJO ================ */
    // POJO 타입 컬렉션 반환 - 제네릭 타입 활용
    public static <T> MongoCollection<T> getCollection(String colName, Class<T> clazz) {
        MongoCollection<T> collection = database.getCollection(colName, clazz);
        return collection;
    }
    /* ================ POJO ================ */
}
