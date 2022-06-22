package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form") //회원가입 화면 요청
    public String saveForm(){
        return "/memberPages/save";
    }
    @PostMapping("/save") //회원가입 처리
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "memberPages/login";
    }
    @GetMapping("/login-form") //로그인 화면
    public String login(){
        return "memberPages/login";
    }
    @PostMapping("/login") //로그인 처리
    public String login(@ModelAttribute MemberDTO memberDTO){
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null){
            return "memberPages/main";
        }else {
            return "memberPages/login";
        }
    }
    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/list";
    }
}

