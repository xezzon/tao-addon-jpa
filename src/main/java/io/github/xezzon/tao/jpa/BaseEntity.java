package io.github.xezzon.tao.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 数据库通用实体类生成基础方法
 * @author xezzon
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity implements Serializable, LogicDelete {

  @java.io.Serial
  private static final long serialVersionUID = 4129917285621615159L;
  /**
   * 主键
   */
  @Id
  @Column(nullable = false, updatable = false, length = 64)
  protected String id;
  /**
   * 记录创建时间
   */
  @Column(nullable = false, updatable = false)
  @CreatedDate
  protected LocalDateTime createTime;
  /**
   * 记录最后更新时间
   */
  @Column(nullable = false)
  @LastModifiedDate
  protected LocalDateTime updateTime;
  /**
   * 逻辑删除标记 删除时间不为空且大于当前时间则认为已删除<br/>
   * 注意 时间精度至少要到毫秒级
   */
  @Column()
  protected LocalDateTime deleteTime;

  public String getId() {
    return id;
  }

  public BaseEntity setId(String id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public BaseEntity setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
    return this;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public BaseEntity setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  @Override
  public LocalDateTime getDeleteTime() {
    return deleteTime;
  }

  public BaseEntity setDeleteTime(LocalDateTime deleteTime) {
    this.deleteTime = deleteTime;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseEntity that = (BaseEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
