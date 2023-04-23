package io.github.xezzon.tao.jpa;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
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
class LogicDeleteRepositoryTest {

  @Autowired
  private DictDAO dictDAO;

  @Test
  void logicDelete() {
    Dict dict = DictDataset.getDataset().parallelStream()
        .findAny().get();
    Assertions.assertDoesNotThrow(() -> dictDAO.get().logicDelete(dict.getId()));
    List<Dict> result = new ArrayList<>();
    dictDAO.get()
        .findAll(QDict.dict.deleteTime.isNotNull())
        .forEach(result::add);
    Assertions.assertEquals(
        1,
        result.size()
    );
    Assertions.assertEquals(
        dict,
        result.get(0)
    );
  }
}

@Getter
@Setter
@ToString
@Entity
@Table
class Dict extends BaseEntity {

  @Serial
  private static final long serialVersionUID = 5254551771226841499L;

  @Column
  private String id;
  @Column
  private String name;

  @Override
  public Dict setId(String id) {
    this.id = id;
    return this;
  }
}

@Component
class DictDataset extends AbstractDataset<Dict> {

  private static final List<Dict> DATASET = new ArrayList<>();

  static {
    for (int i = 0; i < 1000; i++) {
      Dict user = new Dict();
      user.setId(IdUtil.getSnowflakeNextIdStr());
      user.setName(RandomUtil.randomString(6));
      DATASET.add(user);
    }
  }

  public static List<Dict> getDataset() {
    return new ArrayList<>(DATASET);
  }

  protected DictDataset(@NotNull JpaRepository<Dict, ?> repository) {
    super(DATASET, repository);
  }
}

@Repository
interface DictRepository
    extends LogicDeleteRepository<Dict, String>, QuerydslPredicateExecutor<Dict> {

}

@Repository
class DictDAO extends BaseJpaWrapper<Dict, QDict, DictRepository> {

  protected DictDAO(DictRepository dao) {
    super(dao);
  }

  @Override
  protected QDict getQuery() {
    return QDict.dict;
  }

  @Override
  protected Class<Dict> getBeanClass() {
    return Dict.class;
  }
}
