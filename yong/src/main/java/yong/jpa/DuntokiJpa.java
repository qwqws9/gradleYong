package yong.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.entity.BoardEntity;
import yong.entity.DuntokiEntity;

public interface DuntokiJpa extends JpaRepository<DuntokiEntity, Long> {

}
