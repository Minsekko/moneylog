package org.codenova.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codenova.moneylog.entity.User;

@Mapper
public interface UserRepository {
    public int save(User user);
    public User findByEmail(@Param("email") String email); //@Param("email") 생략가능
    public User findByProviderAndProviderId(@Param("provider") String provider,
                                            @Param("providerId") String providerId);  //MAP로 처리 할 필요 없다
}
