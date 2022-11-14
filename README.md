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
 

 
https://nelapham.tistory.com/52
저번 글에서 작성해 놓았던 entity 데이터베이스가 잘 작동하는지 확인해주기 위해서 새로운 repository를 생성해줍니다.

 


public class WishListRepository {

}
repository 패키지를 생성하여 폴더를 나누어 준 후에, WishListRepository 클래스를 생성해줬습니다. 그 후에 extends를 사용하여 MemoryDbRepositoryAbstarct를 상속해줬습니다.  그 후 제네릭을 이용하여 wishlistentity를 타입으로 지정해주었습니다. 데이터베이스를 저장하는 곳이다라는 의미로 repository 어노테이션을 사용해주도록 합니다.

 

@Repository
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {

}
이 abstract 추상 클래스의  <T>를 지정해주었던 부분들이 모두 WishListEntity로 반영되어 작동되게 됩니다.

 

abstract public class MemoryDbRepositoryAbstract<T extends MemoryDbEntity> implements MemoryDbRepositoryIfs<T> {

    private final List<T> db = new ArrayList<>();
    private int index = 0;

    @Override		// <WishListEntity>
    public Optional<T> findById(int index) {
        return db.stream().filter(it -> it.getIndex() == index).findFirst();
    }

    @Override	// <WishListEntity>
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

    @Override
    public void deleteById(int index) {

        var optionalEntity = db.stream().filter(it -> it.getIndex() == index).findFirst();
        if(optionalEntity.isPresent()){
            db.remove(optionalEntity.get());
        }

    }

    @Override	//<WishListEntity>
    public List<T> listAll() {
        return db;
    }
}
 

제대로 작동하는지 Test를 만들어서 작동시켜봅시다. test에서는 이클립스에서 사용했던 어노테이션들을 그대로 가져와서 사용해주면 됩니다. spirngboottest와 각 클래스에 test를 부여했고, autowired 또한 평소와 같이 사용하여 자동으로 할당시켰습니다.

 

@SpringBootTest
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;
    
    @Test
    public void saveTest() {

    }
    
    @Test
    public void findByIdTest() {

    }

    @Test
    public void deleteTest() {

    }

    @Test
    public void listAllTest() {

    }
}
 

테스트 해야 할 네가지 부분을 모두 작성해 주었습니다. 각 엔티티에 알맞은 값을 세팅해주고 하나씩 작동해보도록 하겠습니다.

 

@Test
public void saveTest() {

    var wishList = new WishListEntity();
    wishList.setTitle("title");
    wishList.setCategory("category");
    wishList.setAddress("address");
    wishList.setReadAddress("readAddress");
    wishList.setHomePageLink("");
    wishList.setImageLink("");
    wishList.setVisit(false);
    wishList.setVisitCount(0);
    wishList.setLastVisitDate(null);

}
WishListEntity.set 정보들을 모두 셋팅해주었습니다. 이대로 실행해봐도 좋지만, 다른 메소드에서도 자주 사용될 것 같은 내용이기 때문에 메소드로 따로 설정해준 후에 사용할 때마다 불러들이겠습니다.

 

private WishListEntity create() {
    var wishList = new WishListEntity();
    wishList.setTitle("title");
    wishList.setCategory("category");
    wishList.setAddress("address");
    wishList.setReadAddress("readAddress");
    wishList.setHomePageLink("");
    wishList.setImageLink("");
    wishList.setVisit(false);
    wishList.setVisitCount(0);
    wishList.setLastVisitDate(null);

    return wishList;
}
@Test
public void saveTest() {
    var wishListEntity = create();
 

wishListEntity 메소드를 호출해서 생성해준 후에 wishListRepository에 세팅해 준 후에 Assertions를 사용하여 조건을 부착해줍니다. 기대값을 설정해주는 역할을 합니다. notnull과  equals를 사용했습니다.

 

@Test
public void saveTest() {
    var wishListEntity = create();
    var expected = wishListRepository.save(wishListEntity);

    Assertions.assertNotNull(expected);
    Assertions.assertEquals(1, expected.getIndex());

}
 

해당 테스트항목을 실행해 보고 오류가 발생한다면 수정하고 해결합니다.

 


정상으로 작동되는 것을 확인했다면 나머지 세가지의 메소드도 모두 작성 후 테스트를 반복합니다. 테스트를 하지 않고 실적용으로 바로 넘어간다면 오류를 빠르게 찾아내기 힘들기 때문에 작업능률이 떨어질 수 있습니다. 꼭꼭 테스트를하고 결과를 예상하고 확인하여 다음 작업으로 넘어가야합니다. 

 

@Test
public void findByIdTest() {
    var wishListEntity = create();
    wishListRepository.save(wishListEntity);

    var expected = wishListRepository.findById(1);

}
위에 똑같이 설정해 준후에 expected를 불러들이겠습니다. 그 후에 다시 Assertions를 사용할건데, Optional같은 경우 항상 값이 있기 때문에 Notnull이라는 조건은 그다지 쓸모가 없습니다. .isPresent() 를 사용하여 값이 있을 경우로 작업하겠습니다.

 

Assertions.assertEquals(true, expected.isPresent()); // 값이 있어야 한다.
Assertions.assertEquals(1, expected.get().getIndex());
1이라는 값을 기대값으로 설정하고, expected 값을 꺼냈을때의 index값이 1이어야 한다. 해당 메소드도 제대로 작동하는지 확인하고 넘어가도록 하겠습니다.

 


DeleteTest, listAllTest또한 같은 방식으로 진행하였습니다.

 

@Test
public void deleteTest() {
    var wishListEntity = create();
    wishListRepository.save(wishListEntity);

    wishListRepository.deleteById(1);

    int count = wishListRepository.listAll().size();
    Assertions.assertEquals(0, count);

}

@Test
public void listAllTest() {
    var wishListEntity1 = create();
    wishListRepository.save(wishListEntity1);

    var wishListEntity2 = create();
    wishListRepository.save(wishListEntity2);

    int count = wishListRepository.listAll().size();
    Assertions.assertEquals(2, count);


}
네 가지 모든 테스트 방식을 진행하였지만, 한가지 빼먹었던 부분이 데이터 db에 이미 정보가 있을 경우는 아직 테스트하지 못했습니다. 데이터를 업데이트하여 받아와야 하기 때문에 카운터가 늘어나면 안됩니다. 이 부분을 테스트하기 위해 saveTest 메소드를 복사하여 updateTest로 수정해서 작동시켜보도록 하겠습니다.

 

@Test
public void updateTest() {
    var wishListEntity = create();
    var expected = wishListRepository.save(wishListEntity);

    expected.setTitle("update Test");
    var saveEntity = wishListRepository.save(expected);

    Assertions.assertEquals("update Test", saveEntity.getTitle());
    Assertions.assertEquals(1, wishListRepository.listAll().size());
    
}
제목을 업데이트 됐단것으로 알 수 있게 바꾸었고, 해당 기대값들이 "update Test" 여야 하고, listAll로 호출하였을때 사이즈값이 그대로 1이어야 합니다 ( index++ )이 되면 업데이트가 아니라 기존 생성과 다를바 없기때문에 반드시 확인해야합니다.
