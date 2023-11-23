package todoList.soloProject.domain.member.controller;


import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import todoList.soloProject.domain.member.service.MemberService;
import todoList.soloProject.domain.member.service.dto.*;
import todoList.soloProject.jwt.MemberInfo;
import todoList.soloProject.jwt.MembersInfo;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

	private final MemberService memberService;

	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@Operation(summary = "회원가입", description = "회원가입 시 이메일, 패스워드, 내가 가지고 있는 cardlist를 request로 받는다", tags = { "Member Controller" })
	@PostMapping("/save")
	public String save(@RequestBody @Valid UserSaveRequestDto userSaveRequestDto){
		return memberService.save(userSaveRequestDto);
	}

	@ApiResponses(value = {
		@ApiResponse(responseCode = "226", description = "로그인 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = LoginResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@Operation(summary = "로그인", description = "로그인 시 토큰 발급", tags = { "Member Controller" })
	@PostMapping("/login")
	public LoginResponse login (@RequestBody LoginRequest loginRequest) {
		return memberService.login(loginRequest);
	}


	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "회원가입 성공",
			content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "멤버 정보", description = "멤버 정보 불러오기", tags = { "Member Controller" })
	@GetMapping("/info")
	public UserResponse getUser (@MemberInfo MembersInfo membersInfo) {
		return memberService.getUserInfo(membersInfo.getId());
	}


	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "닉네임 변경 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = UpdateUserResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "닉네임 변경", description = "서비스 내 나의 닉네임 변경", tags = { "Member Controller" })
	@PutMapping("/change")
	public UpdateUserResponseDto updateNickname (@MemberInfo MembersInfo membersInfo, @RequestBody UpdateUserRequestDto updateUserRequestDto){
		return memberService.updateNickname(updateUserRequestDto, membersInfo.getId());
	}

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "멤버 삭제 성공"),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "멤버 삭제", description = "멤버 정보 삭제", tags = { "Member Controller" })
	@DeleteMapping("/delete")
	public void delete (@RequestBody DeleteUserRequestDto requestDto){
		memberService.deleteUser(requestDto);
	}


	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "토큰 재발급 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "요청 오류 "),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@Operation(summary = "토큰 재발급", description = "refreshToken이 있을 때 AccessToken 재발급한다", tags = { "Member Controller" })
	@PostMapping("/token")
	public AccessTokenResponse getAccessToken (@RequestBody AccessTokenRequest request, @MemberInfo MembersInfo membersInfo){
		return memberService.getAccessToken(request, membersInfo.getId());
	}


	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
		@ApiResponse(responseCode = "400", description = "요청 오류"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 입력후 1, 2차 확인 후 변경할 비밀번호로 바꾼다 ", tags = { "Member Controller" })
	@PutMapping("/changePassword")
	public void changeMyPassword (@MemberInfo MembersInfo membersInfo,@RequestBody ChangeMyPasswordRequestDto requestDto){
		memberService.changeMyPassword(membersInfo.getId(), requestDto);

	}
}
