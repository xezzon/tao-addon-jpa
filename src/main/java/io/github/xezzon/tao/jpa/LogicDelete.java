package io.github.xezzon.tao.jpa;

import java.time.LocalDateTime;

/**
 * @author xezzon
 */
public interface LogicDelete {

  LocalDateTime getDeleteTime();

  /**
   * 删除时间小于等于当前时间即为删除
   * @return 是否被删除
   */
  default boolean isDeleted() {
    if (this.getDeleteTime() == null) {
      return false;
    }
    return !this.getDeleteTime().isAfter(LocalDateTime.now());
  }
}
