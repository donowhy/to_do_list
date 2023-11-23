package todoList.soloProject.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todoList.soloProject.domain.member.entity.enumType.Role;
import todoList.soloProject.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@NotBlank
	private String password;

	@Column(length = 30, unique=true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	private String nickname;

	private String refreshToken;

	@Builder
	public Member(Long id, String password, String email, Role role, String nickname, String refreshToken) {
		this.id = id;
		this.password = password;
		this.email = email;
		this.role = role;
		this.nickname = nickname;
		this.refreshToken = refreshToken;
	}

	public void setNickname(String nickName) {
		this.nickname = nickName;
	}

	public void setUserPassword(String pw) {
		this.password = pw;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
