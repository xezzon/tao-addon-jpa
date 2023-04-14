package io.github.xezzon.tao.jpa;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * @author xezzon
 */
public abstract class AbstractDataset<T> {

  protected final transient Collection<T> dataset;
  private final transient JpaRepository<T, ?> repository;

  protected AbstractDataset(
      @NotNull Collection<T> dataset,
      @NotNull JpaRepository<T, ?> repository
  ) {
    this.dataset = dataset;
    this.repository = repository;
  }

  @PostConstruct
  public void init() {
    if (dataset.isEmpty()) {
      return;
    }
    repository.saveAll(dataset);
  }
}
