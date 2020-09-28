package yong.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.entity.BoardEntity;
import yong.entity.DuntokiEntity;
import yong.entity.FileEntity;

public interface DuntokiJpa extends JpaRepository<DuntokiEntity, Long> {

    List<DuntokiEntity> findByMainId(String mainId);
    
    List<DuntokiEntity> findByMainIdAndSeqno(String mainId, Long seqno);
}
