package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    @GetMapping("/login-form") //로그인 화면 defaultValue = "/" 보고싶은 페이지의 주소를 줘야함
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL, Model model){
        model.addAttribute("redirectURL", redirectURL);
        return "memberPages/login";
    }
    @PostMapping("/login") //로그인 처리 defaultValue = "/" 보고싶은 페이지의 주소를 줘야함
    public String login(@ModelAttribute MemberDTO memberDTO,HttpSession session,
    @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL){
        MemberDTO loginResult = memberService.login(memberDTO);
        session.setAttribute("loginEmail",loginResult.getMemberEmail());
        session.setAttribute("id",loginResult.getId());
        if (loginResult != null){
            // return "memberPages/mypage";
            return "redirect:" + redirectURL; // 로그인 하지 않은 사용자가 로그인 직전에 요청한 주소로 보내줌
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
    // member/3
    // /member?id=3
    @GetMapping("/{id}") //파라미터 값을 담아줌!!
    public String findById(@PathVariable Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "memberPages/detail";
    }

    // ajax 상세조회
    @PostMapping("/ajax/{id}")
    public @ResponseBody MemberDTO findByIdAjax(@PathVariable Long id){
        MemberDTO memberDTO = memberService.findById(id);
        return memberDTO;
    }

    // get 요청 삭제 /member/delete/3
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }

    /**
     * /member/3 : 조회(get) R, 저장(post) C, 수정(put) U, 삭제(delete) D
     */


    //delete 요청 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAjax(@PathVariable Long id){
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); // ajax 호출한 부분에 리턴으로 200 응답을 줌.
    }
    // 수정화면 요청
    @GetMapping("/update")
    public String updateForm(HttpSession session,Model model ){
        Long id = (Long) session.getAttribute("id"); //강제형 변환
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("updateMember",memberDTO);
        return "memberPages/update";
    }
    @PostMapping("/update")
    public  String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/"+memberDTO.getId();
    }
    // 수정처리(put 요청)
    @PutMapping("/{id}") // json 형싟에서 불러올때는 @requestBody putmapping은 ajax일떄
    public ResponseEntity updateByAjax(@RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // 이메일 중복체크
    @PostMapping("/emailCheck")
    public @ResponseBody String emailCheck(@RequestParam String memberEmail){
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); // 세션 전체를 무효화
       // session.removeAttribute("loginEmail"); // loginEmail 만 세션값 삭제
        return "redirect:/";
    }
}

