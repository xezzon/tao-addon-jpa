package io.github.xezzon.tao.jpa.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * @author xezzon
 */
@Configuration
public class JpaConfiguration {

  @Autowired
  public JpaConfiguration() {
  }

  @Bean
  public JPAQueryFactory queryFactory(EntityManager em) {
    return new JPAQueryFactory(em);
  }
}
