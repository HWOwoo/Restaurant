# Restaurant
https://nelapham.tistory.com/51

네이버의 지역검색 API를 활용하여, 키워드를 검색해서 맛집을 검색할 수 있는 웹 페이지를 만들어보려고 한다. 검색된 가게를 저장하여, 위시리스트( 즐겨찾기 )를 추가하거나 방문횟수를 추가할 수 있도록 설계하여 내가 가 봤던 사이트들을 기록 할 수 있는 사이트를 만드는 프로젝트를 진행하려한다.

 

화면설계

 


키워드를 검색하여, 검색 결과가 리스트업 되고, 그 옆에 그 가게에 등록된 설명이 출력된다. 추가 버튼을 클릭시, 자신의 위시리스트에 추가되며, 자신의 위시리스트에 등록된 가게에는 방문횟수를 추가할 수 있는 버튼이 표시되어 몇 번 방문 했는지 기록할 수 있다.

 

전과 다르게 이클립스가 아닌 inteliJ로 프로젝트를 진행해보려합니다. spring을 이용할 것이기 때문에, spring initializr에서 추가 Dependencies를 이용해 Lombok과 Spring Web, Thymeleaf을 추가하고 프로젝트를 생성해줬습니다. 

- Thymeleaf 같은 경우는 템플릿을 관리해주는 디펜던시 입니다.

 


 

하단의 generate 버튼을 눌러 프로젝트 파일을 생성 후 inteliJ에서 열어줍니다.

 


java 셋팅이 되어있지 않다면 상단의 실행버튼이 보이지않습니다. 처음으로 Gradle Project를 불러들였다면 다운로드의 시간이 걸릴 수 있으니 하단 프로세서를 참고하여 기다린 후에 프로젝트를 진행해주시면 되겠습니다. 

 


우측의 실행버튼
 


정상적으로 실행된모습을 확인 후에 중지한 후에 작업을 진행하겠습니다.


 

처음으로 생성할 것은 db 패키지 폴더입니다. 새로운 interface를 생성하고 ( MemoryDbRepositoryIfs ) 제네릭을 이용하여 타입을 지정합니다. 

 

public interface MemoryDbRepositoryIfs<T> {

    Optional<T> findById(int index);
    T save(T entity);    
    void deleteById(int index);
    List<T> listAll();
    
}
Optional 을 이용하여 해당 타입의 대해서 한가지 옵션을 가지고 값을 리턴해주는 findById 메소드를 작성해줍니다. int 타입의 index를 받아서 해당 데이터를 리턴하는 역할을 해줄겁니다. T save는 저장을 담당하고 deleteById는 삭제, listAll은 전체를 리턴해주는 역할을 하는 메소드까지 총 네가지를 구현하도록 합니다.

 

interface를 구현했다면 추상클래스를 구현해줄 차례입니다.

 


 

public class MemoryDbRepositoryAbstract<T> implements MemoryDbRepositoryIfs<T> {

    @Override
    public Optional<T> findById(int index) {
        return Optional.empty();
    }

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public void deleteById(int index) {

    }

    @Override
    public List<T> listAll() {
        return null;
    }
}
만들었던 interface를 implements하여 상단의 implements code를 이용하여 인터페이스 내에 있던 메소드들을 모두 불러들인 후에 세부사항을 제작하도록 합니다.

 


 

public class MemoryDbRepositoryAbstract<T> implements MemoryDbRepositoryIfs<T> {

    private final List<T> db = new ArrayList<>();
    private int index = 0;

    @Override
    public Optional<T> findById(int index) {
        return Optional.empty();
    }

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public void deleteById(int index) {

    }

    @Override
    public List<T> listAll() {
        return null;
    }
}
데이터를 저장할 arraylist에 데이터를 넣는 방식으로 진행하려하고 index는 고유 id를 만들어준 것입니다. 해당 entity를 활용하기 위해서 클래스 한가지를 더 추가해 줍니다.

 


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoryDbEntity {

    private int index;

}
모든 데이터베이스를 가진 클래스들은 이 MemoryDbEntity를 상속해서 사용할 겁니다. 제네릭 타입에 와일드카드를 사용하여 이 MemoryDbEntity 제네릭 타입을 상속받아 사용할 것 입니다. stream을 통해 filter를 사용하여 찾도록하려합니다. getindex의 값이 index의 값과 동일한것에 findFirst를 리턴하여 활용합니다.

 

@Override
public Optional<T> findById(int index) {
    return db.stream().filter(it -> it.getIndex() == index).findFirst();
}
이제 저장 역할을 하는 save 메소드를 작성해주려합니다. 저장같은 경우는 두가지 상황으로 갈리게 되는데 데이터베이스에 이미 정보가 있을 경우 그 곳에 저장하는 것과 정보가 없을때 처음 생성되는 상황으로 나뉘게됩니다. 판단할 수 있는 부분을 따로 작성해주어야 한다.

    @Override
    public T save(T entity) {

        var optionalEntity = db.stream().filter(it -> it.getIndex() == entity.getIndex()).findFirst();

        if(optionalEntity.isEmpty()) {
            // db에 데이터가 없던 경우


        } else {
            // db에 데이터가 있던 경우

        }


        return null;
    }
isEmpty를 이용하여 해당 db에 데이터가 이미 존재할 경우와 존재하지 않던 경우를 나누어서 코드를 작성하도록 하겠습니다.  데이터가 없던 경우, 즉 isEmpty에서는 index값을 증가시키고 index값을 entity에 저장하여 추가합니다. 그에 반해 db에 데이터가 있던 경우에는 기존에 있던 entity 정보를 불러들인 후에 새로운 entity에 세팅하여 기존에 있던 데이터를 지우고 새로 받은 entity를 불러들여 리턴하려합니다.

 

@Override
public T save(T entity) {

    var optionalEntity = db.stream().filter(it -> it.getIndex() == entity.getIndex()).findFirst();

    if(optionalEntity.isEmpty()) {
        // db에 데이터가 없던 경우
        index++;
        entity.setIndex(index);
        db.add(entity);

        return entity;

    } else {
        // db에 데이터가 있던 경우
        var preIndex = optionalEntity.get().getIndex();
        entity.setIndex(preIndex);

        deleteById(preIndex);
        db.add(entity);

        return entity;

    }

}
위에 내용을 바탕으로 코딩화 시켰습니다. setIndex를 이용하여 데이터를 불러들이고 add를 이용하여 추가해줍니다. 위 코드에 deleteById를 작성해 줄 시간입니다.

 

@Override
public void deleteById(int index) {

    var optionalEntity = db.stream().filter(it -> it.getIndex() == index).findFirst();
    if(optionalEntity.isPresent()){
        db.remove(optionalEntity.get());
    }

}
 

delete는 비교적 간단합니다. 찾은 후에 있다면 삭제합니다. 

 

@Override
public List<T> listAll() {
    return db;
}
모든 list를 불러들이는 역할은 더더욱 간단합니다. 모든 db 정보를 return시키면 데이터가 모두 넘어오겠네요. 모든 데이터베이스 추상화된 클래스를 완성시켰습니다. 데이터베이스를 사용하기 위해서 새로운 파일과 패키지를 만들어주도록 합니다.


@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishListEntity extends MemoryDbEntity {

    private String title;           // 음식명, 장소명
    private String category;        // 카테고리
    private String address;         // 주소
    private String readAddress;     // 도로명
    private String homePageLink;    // 홈페이지 주소
    private String imageLink;       // 음식,가게 이미지 주소
    private boolean isVisit;        // 방문 여부
    private int visitCount;         // 방문 횟수
    private LocalDateTime lastVisitDate; // 방문 마지막 일자

}
해당 클래스파일에 데이터베이스에 필요한 정보들을 작성해줍니다. 제목과 카테고리, 주소, 홈페이지 주소와 이미지 주소, 방문여부와 방문횟수, 그리고 마지막 방문일까지 저장할 수 있는 공간을 할당해줍니다.
