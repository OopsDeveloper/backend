package org.oopsdev.gallery.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.oopsdev.gallery.backend.entity.Item;
import org.oopsdev.gallery.backend.entity.Member;
import org.oopsdev.gallery.backend.repository.MemberRepository;
import org.oopsdev.gallery.backend.service.JwtService;
import org.oopsdev.gallery.backend.service.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @PostMapping("/api/account/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if (member != null) {
            JwtService jwtService = new JwtServiceImpl();
            int id =  member.getId();
            String token = jwtService.getToken("id", id);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); //js로 접근 못하게 막기
            cookie.setPath("/");

            res.addCookie(cookie);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
