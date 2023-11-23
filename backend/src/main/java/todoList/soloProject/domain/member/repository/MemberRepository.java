package todoList.soloProject.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoList.soloProject.domain.member.entity.Member;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByEmailAndPassword(String email, String password);

	Optional<Member> findMemberByEmail (String email1);

	@Modifying
	@Query("update Member m set m.password = :password where m.email = :email")
	int updateMemberPassword(@Param("email") String email, @Param("password") String password);

}
