package yong.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import yong.entity.UserEntity;

public interface UserJpa extends JpaRepository<UserEntity, Long> {
    
    UserEntity findByUserId(String userId);
    

}
