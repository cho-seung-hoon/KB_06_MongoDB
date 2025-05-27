package app.domain;

import org.bson.types.ObjectId;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data                    // Getter/Setter/toString 자동 생성
@AllArgsConstructor      // 모든 필드를 매개변수로 하는 생성자
@NoArgsConstructor       // 기본 생성자 (MongoDB 매핑 필수)
public class Todo {
    private ObjectId id;     // MongoDB _id 필드
    private String title;    // 할일 제목
    private String desc;     // 할일 설명
    private boolean done;    // 완료 여부
}
