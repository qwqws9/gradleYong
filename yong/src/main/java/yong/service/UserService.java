package yong.service;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.common.HashUtil;
import yong.common.Result;
import yong.dto.UserDto;
import yong.entity.UserEntity;
import yong.exception.BadRequestException;
import yong.jpa.UserJpa;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserJpa userJpa;
    
    /**
     * 
     * 유저 로그인
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     * @return
     */
    public Result login(UserDto user) {
        log.debug(user.getUserPwd());
        log.debug(user.getUserId());
        
        if (user == null || StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getUserPwd())) {
            return new Result(100, "아이디, 패스워드 오류");
        }
        
        // 비밀번호 암호화
        user.setUserPwd(HashUtil.sha512(user.getUserPwd(), user.getUserPwd()));
        
        UserEntity entity = this.userJpa.findByUserId(user.getUserId());
        
        if (entity != null && user.getUserPwd().equals(entity.getUserPwd())) {
            return new Result(entity.toDto(UserDto.class));
        }
        
        return new Result(2);
    }
    
    /**
     * 
     * 유저 등록
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     */
    public void userSave(UserDto user) {
        if (StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getUserPwd()) || StringUtils.isEmpty(user.getUserNick())) {
            throw new BadRequestException("폼 데이터 확인 후 재등록");
        }
        
        user.setUserStatCd("1");
        user.setUserRole("user");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        user.setRegDt(format.format(System.currentTimeMillis()));
        user.setUserPwd(HashUtil.sha512(user.getUserPwd(), user.getUserPwd()));
        this.userJpa.save(user.toEntity(UserEntity.class));
    }
    
    /**
     * 
     * 유저 수정
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     */
    public void userMod(UserDto user) {
        if (StringUtils.isEmpty(user.getUserId()) || StringUtils.isEmpty(user.getUserPwd()) || StringUtils.isEmpty(user.getUserNick())) {
            throw new BadRequestException("폼 데이터 확인 후 재등록");
        }
        
        UserEntity entity = this.userJpa.findById(user.getUserSeq()).orElse(null);
        entity.setUserNick(user.getUserNick());
        entity.setUserPwd(HashUtil.sha512(user.getUserPwd(), user.getUserPwd()));
        
    }
    
    /**
     * 
     * 유저 조회
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     * @return
     */
    public UserDto getUser(Long userSeq) {
        UserEntity entity = this.userJpa.findById(userSeq).orElse(null);
        if (entity == null) { throw new BadRequestException("존재하지 않는 회원입니다."); }
        
        UserDto user = entity.toDto(UserDto.class);
        user.setUserPwd("");
        
        return user;
    }
}
