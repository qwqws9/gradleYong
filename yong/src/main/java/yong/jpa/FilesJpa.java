package yong.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import yong.entity.FileEntity;

public interface FilesJpa extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByBoardSeq(Long boardSeq);
    
    List<FileEntity> findByBoardSeqAndDelYnOrderByFileSeq(Long boardSeq, String delYn);
    
}
