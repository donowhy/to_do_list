package todoList.soloProject.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoList.soloProject.domain.member.entity.Member;
import todoList.soloProject.domain.member.entity.enumType.Role;
import todoList.soloProject.domain.member.repository.MemberRepository;
import todoList.soloProject.domain.member.service.dto.*;
import todoList.soloProject.global.error.code.ErrorCode;
import todoList.soloProject.global.error.exception.BusinessException;
import todoList.soloProject.jwt.JwtProvider;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    // 유저 저장
    public String save(UserSaveRequestDto userSaveRequestDto) {

        Member member = Member.builder()
                .email(userSaveRequestDto.getEmail())
                .password(userSaveRequestDto.getPassword())
                .nickname(userSaveRequestDto.getNickname())
                .role(Role.USER)
                .build();


        memberRepository.saveAndFlush(member);

        return userSaveRequestDto.getEmail();
    }


    // 유저 정보 조회
    public UserResponse getUserInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new BusinessException(ErrorCode.NOT_EXISTS_USER_ID)
        );

        return UserResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    // 로그인
    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new BusinessException(ErrorCode.INVALID_USER_DATA)
        );

        if (!member.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException();
        }

        // 로그인 시 엑세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        member.setRefreshToken(refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 닉네임 변경
    public UpdateUserResponseDto updateNickname(UpdateUserRequestDto updateUserRequestDto, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new BusinessException(ErrorCode.NOT_EXISTS_USER_ID)
        );

        member.setNickname(updateUserRequestDto.getNickName());

        return UpdateUserResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    // 유저 삭제
    public void deleteUser(DeleteUserRequestDto requestDto) {

        if(!requestDto.getPassword().equals(requestDto.getCheckPassword())){
            throw new BusinessException(ErrorCode.INVALID_USER_DATA);
        }

        Member member = memberRepository.findMemberByEmail(requestDto.getEmail()).orElseThrow(() ->
                new BusinessException(ErrorCode.NOT_EXISTS_USER_ID)
        );

        memberRepository.delete(member);
    }


    // 유저 accessToken 재발급
    public AccessTokenResponse getAccessToken(AccessTokenRequest request, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(()
                -> new BusinessException(ErrorCode.NOT_EXISTS_USER_ID));

        if(!request.getRefreshToken().equals(member.getRefreshToken())) throw new BusinessException(ErrorCode.NOT_VALID_TOKEN);

        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        member.setRefreshToken(refreshToken);

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    // 비밀번호 변경
    public void changeMyPassword(Long id, ChangeMyPasswordRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new BusinessException(ErrorCode.NOT_EXISTS_USER_ID)
        );
        if (!member.getPassword().equals(requestDto.getNowPassword())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        if (!requestDto.getPasswordOne().equals(requestDto.getPasswordTwo())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        member.setUserPassword(requestDto.getPasswordOne());
    }
}
