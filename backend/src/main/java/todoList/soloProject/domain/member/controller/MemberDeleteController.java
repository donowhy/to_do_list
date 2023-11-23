package todoList.soloProject.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import todoList.soloProject.domain.member.service.MemberService;
import todoList.soloProject.domain.member.service.dto.DeleteUserRequestDto;

@Controller
@RequiredArgsConstructor
public class MemberDeleteController {

    private final MemberService memberService;

    @GetMapping("/deleteUserPage")
    public String deleteUserPage() {
        return "deleteUser";
    }
}
