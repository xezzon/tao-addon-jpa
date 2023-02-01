package indi.xezzon.tao.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import indi.xezzon.tao.retrieval.CommonQuery;
import java.util.Optional;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author xezzon
 */
public class JpaUtil {

  public static final BooleanExpression TRUE_EXPRESSION = Expressions.ONE.eq(1);

  public static <T extends EntityPathBase<RT>, RT> BooleanExpression getQueryClause(
      CommonQuery commonQuery,
      T dataObj,
      Class<RT> clazz
  ) {
    ParseTree parseTree = commonQuery.parseFilter();
    if (parseTree == null) {
      return TRUE_EXPRESSION;
    }
    // 筛选
    CommonQueryFilterJpaVisitor<T, RT> visitor = new CommonQueryFilterJpaVisitor<>(dataObj, clazz);
    return Optional.ofNullable(visitor.visit(parseTree))
        .orElse(TRUE_EXPRESSION);
  }

  public static Pageable getPageable(CommonQuery commonQuery) {
    Pageable pageable = commonQuery.getPageSize() > 0
        ? Pageable.ofSize(commonQuery.getPageSize()).withPage(commonQuery.getPageNum() - 1)
        : Pageable.unpaged();
    commonQuery.parseSort()
        .forEach(sorter -> pageable.getSort()
            .and(Sort.by(
                switch (sorter.getDirection()) {
                  case ASC -> Direction.ASC;
                  case DESC -> Direction.DESC;
                },
                sorter.getField()
            ))
        );
    return pageable;
  }
}

