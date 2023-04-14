package io.github.xezzon.tao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author xezzon
 */
@SpringBootTest
@ActiveProfiles("test")
class JpaWrapperTest {

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

