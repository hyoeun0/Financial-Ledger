package com.FinancialLedger.money.controller;

import com.FinancialLedger.money.dto.UserDTO;
import com.FinancialLedger.money.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class UserController {
    // 생성자 주입
    private final UserService userService;
    // 회원가입 페이지 출력 요철
    @GetMapping("/money/sign")
    public String signForm(){
        return "sign";
    }
    @PostMapping("/money/sign")
    public String sign(@ModelAttribute UserDTO userDTO){
        System.out.println("UserController.sign"); //soutm 단축키 현재 메서드 뭔지 작성
        System.out.println("userDTO = " + userDTO); //soutp 단축키 매개변수 자동 생성
        userService.sign(userDTO);

        return "login";
    }

    @GetMapping("/money/login")
    public String loginForm(){
        return "login";
    }
    @PostMapping("/money/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session){
        UserDTO loginResult = userService.login(userDTO);
        if(loginResult != null){
            // login 성공
            // 세션 저장하기
            session.setAttribute("loginID", loginResult.getUserID());
            return  "main";
        } else {
            // login 실패
            return "login";
        }
    }

    // ajax 아이디 중복체크
    @PostMapping("/money/id-check")
    public @ResponseBody String idCheck(@RequestParam("userID") String userID){
        System.out.println("userID = " + userID);
        String checkResult = userService.idCheck(userID);
        return  checkResult;
    }

    @GetMapping("/money/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

}
