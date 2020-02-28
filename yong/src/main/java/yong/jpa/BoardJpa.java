package yong.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.entity.BoardEntity;

public interface BoardJpa extends JpaRepository<BoardEntity, Long> {

}
