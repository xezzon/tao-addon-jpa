package io.github.xezzon.tao.jpa;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

  }

  /**
   * 无条件查询
   */
  @Test
  void query_unpaged() {

  }

  /**
   * 字段找不到
   */
  @Test
  void query_unknown_field() {

  }

  @Test
  void query_string() {

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

  @Test
  void query_in_empty() {

  }
}

@Getter
@Setter
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
  @Column
  private BigDecimal credit;
  @Column
  private GenderEnum gender;
  @Column
  private LocalDateTime deleteTime;
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
      user.setCredit(RandomUtil.randomBigDecimal());
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
