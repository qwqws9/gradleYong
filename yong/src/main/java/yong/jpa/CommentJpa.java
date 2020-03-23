package yong.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.entity.CommentEntity;

public interface CommentJpa extends JpaRepository<CommentEntity, Long> {

}
