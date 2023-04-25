package com.example.meta.repository;

import com.example.meta.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query(value = "select * from member where id = :user_id", nativeQuery = true)
        //findById는 내가 임의로 만든 메서드
        String findById (@Param("user_id") String user_id);

    @Query(value = "select * from member where password = :user_pw", nativeQuery = true)
        //findByPw는 내가 임의로 만든 메서드
    String findByPw(@Param("user_pw") String user_pw);

    @Query(value = "delete * from member where memberId = :memberId", nativeQuery = true)
        //findByPw는 내가 임의로 만든 메서드
    String deleteById(@Param("memberId") String memberId);

}
