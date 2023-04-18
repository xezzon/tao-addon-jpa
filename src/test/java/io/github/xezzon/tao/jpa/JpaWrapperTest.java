package io.github.xezzon.tao.jpa;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.github.xezzon.tao.retrieval.CommonQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author xezzon
 */
@SpringBootTest
@ActiveProfiles("test")
class JpaWrapperTest {

  @Autowired
  private UserDAO userDAO;

  @Test
  void update() {
    // 普通字段正常更新

    // 值为 NULL 的字段不更新

    // updatable 属性为 false 的属性不更新
  }

  /**
   * 正常分页、排序、筛选
   */
  @Test
  void query() {
    String condition = "(name LLIKE 'J' OR (age GT 18)) AND (gender IN 'MALE' OR deleteTime NULL true)";
    List<User> excepts = UserDataset.getDataset().parallelStream()
        .filter(user -> user.getName().startsWith("J") || user.getAge() > 18)
        .filter(user -> Objects.equals(GenderEnum.MALE, user.getGender())
            || user.getDeleteTime() == null)
        .sorted(Comparator.comparing(User::getCredit))
        .toList();
    int pageSize = 15;
    int pageNum = 1;

    CommonQuery commonQuery = new CommonQuery();
    commonQuery.setFilter(condition);
    commonQuery.setSort(Collections.singletonList("credit:ASC"));
    commonQuery.setPageSize(pageSize);
    commonQuery.setPageNum(pageNum);
    Page<User> users = userDAO.query(commonQuery);
    Assertions.assertEquals(excepts.size(), users.getTotalElements());
    Assertions.assertIterableEquals(
        excepts.parallelStream().limit(pageSize).toList(),
        users.getContent()
    );
  }

  /**
   * 无条件查询
   */
  @Test
  void query_unpaged() {
    Page<User> users = userDAO.query(new CommonQuery());
    Assertions.assertEquals(UserDataset.getDataset().size(), users.getTotalElements());
    Assertions.assertEquals(UserDataset.getDataset().size(), users.getContent().size());
  }

  /**
   * 字段找不到
   */
  @Test
  void query_unknown_field() {
    CommonQuery commonQuery = new CommonQuery();
    commonQuery.setFilter("field EQ 'value'");
    Assertions.assertThrowsExactly(
        UnsupportedOperationException.class,
        () -> userDAO.query(commonQuery)
    );
  }

  @Test
  void query_string() {
    CommonQuery eqQuery = new CommonQuery();
    String exceptName = UserDataset.getDataset().parallelStream()
        .map(User::getName)
        .findAny()
        .get();
    eqQuery.setFilter(String.format("name EQ '%s'", exceptName));
    Page<User> eqPage = userDAO.query(eqQuery);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> Objects.equals(user.getName(), exceptName))
            .count(),
        eqPage.getTotalElements()
    );

    CommonQuery neQuery = new CommonQuery();
    neQuery.setFilter(String.format("name NE '%s'", exceptName));
    Page<User> nePage = userDAO.query(neQuery);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> !Objects.equals(user.getName(), exceptName))
            .count(),
        nePage.getTotalElements()
    );
    Assertions.assertEquals(
        UserDataset.getDataset().size(),
        eqPage.getTotalElements() + nePage.getTotalElements()
    );

    CommonQuery llikeQuery = new CommonQuery();
    llikeQuery.setFilter(String.format("name LLIKE '%s'", exceptName.substring(0, 3)));
    Page<User> llikePage = userDAO.query(llikeQuery);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> user.getName().startsWith(exceptName.substring(0, 3)))
            .count(),
        llikePage.getTotalElements()
    );

    Set<String> exceptId = UserDataset.getDataset().parallelStream()
        .limit(10)
        .map(User::getId)
        .collect(Collectors.toSet());
    CommonQuery inQuery = new CommonQuery();
    inQuery.setFilter(String.format("id IN '%s'", StrUtil.join(",", exceptId)));
    Page<User> inPage = userDAO.query(inQuery);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> exceptId.contains(user.getId()))
            .count(),
        inPage.getTotalElements()
    );

    CommonQuery outQuery = new CommonQuery();
    outQuery.setFilter(String.format("id OUT '%s'", StrUtil.join(",", exceptId)));
    Page<User> outPage = userDAO.query(outQuery);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> !exceptId.contains(user.getId()))
            .count(),
        outPage.getTotalElements()
    );
    Assertions.assertEquals(
        UserDataset.getDataset().size(),
        inPage.getTotalElements() + outPage.getTotalElements()
    );

    CommonQuery emptyInQuery1 = new CommonQuery();
    emptyInQuery1.setFilter("name IN ''");
    Page<User> emptyInPage1 = userDAO.query(emptyInQuery1);
    Assertions.assertEquals(
        0,
        emptyInPage1.getTotalElements()
    );

    CommonQuery emptyInQuery2 = new CommonQuery();
    emptyInQuery2.setFilter(String.format("name IN ',%s, ,'", exceptName));
    Page<User> emptyInPage2 = userDAO.query(emptyInQuery2);
    Assertions.assertEquals(
        UserDataset.getDataset().parallelStream()
            .filter(user -> Objects.equals(user.getName(), exceptName))
            .count(),
        emptyInPage2.getTotalElements()
    );
  }

  @Test
  void query_enum() {

  }

  @Test
  void query_number() {

  }

  @Test
  void query_datetime() {

  }

  @Test
  void query_date() {

  }

  @Test
  void query_time() {

  }

  @Test
  void query_boolean() {

  }
}

@Getter
@Setter
@ToString
@Entity
@Table(name = "t_user")
class User {

  @Id
  @Column
  private String id;
  @Column
  private String name;
  @Column
  private Integer age;
  @Column(scale = 9)
  private BigDecimal credit;
  @Column
  private GenderEnum gender;
  @Column
  private LocalDateTime deleteTime;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

enum GenderEnum {
  MALE,
  FEMALE,
  ;
}

@Component
class UserDataset extends AbstractDataset<User> {

  private static final List<User> DATASET = new ArrayList<>();

  static {
    for (int i = 0; i < 1000; i++) {
      User user = new User();
      user.setId(IdUtil.getSnowflakeNextIdStr());
      user.setName(RandomUtil.randomString(6));
      user.setAge(RandomUtil.randomInt(6, 60));
      user.setCredit(RandomUtil.randomBigDecimal(new BigDecimal("100.000")));
      user.setGender(RandomUtil.randomEle(GenderEnum.values()));
      user.setDeleteTime(RandomUtil.randomBoolean() ?
          LocalDateTime.of(
              RandomUtil.randomInt(2000, 2999),
              RandomUtil.randomInt(1, 11),
              RandomUtil.randomInt(1, 27),
              RandomUtil.randomInt(1, 11),
              RandomUtil.randomInt(1, 59),
              RandomUtil.randomInt(1, 59)
          ) : null
      );
      DATASET.add(user);
    }
  }

  public static List<User> getDataset() {
    return new ArrayList<>(DATASET);
  }

  private UserDataset(UserRepository repository) {
    super(DATASET, repository);
  }
}

@Repository
interface UserRepository extends JpaRepository<User, String>, QuerydslPredicateExecutor<User> {

}

@Repository
class UserDAO extends BaseJpaWrapper<User, QUser, UserRepository> {


  protected UserDAO(UserRepository dao) {
    super(dao);
  }

  @Override
  protected QUser getQuery() {
    return QUser.user;
  }

  @Override
  protected Class<User> getBeanClass() {
    return User.class;
  }
}
