package todoList.soloProject.domain.member.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSaveRequestDto {

	@NotEmpty(message = "이메일은 필수 입력값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
	private String email;

	@NotEmpty(message = "비밀번호는 필수 입력값입니다.")
	private String password;

	private String nickname;

}
