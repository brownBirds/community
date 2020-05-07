package com.boke.community.controller;

import com.boke.community.dto.AccessTokenDto;
import com.boke.community.dto.GitHubUser;
import com.boke.community.mapper.UserMapper;
import com.boke.community.model.User;
import com.boke.community.provider.GithubProvider;
import com.boke.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.Redirect.uri}")
    private String Redirect_uri;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDto accesstokendto = new AccessTokenDto();
        accesstokendto.setClient_id(clientId);
        accesstokendto.setClient_secret(clientSecret);
        accesstokendto.setRedirect_uri(Redirect_uri);
        accesstokendto.setCode(code);
        accesstokendto.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokendto);
        GitHubUser gitHubUser = githubProvider.getUser(accessToken);
        if (gitHubUser!=null && gitHubUser.getId() != null){
            //登录成功，写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setAvatarUrl(gitHubUser.getAvatar_url());
            user.setGmtModified(user.getGmtCreate());
            userService.createOrUpdate(user);
            Cookie cookie=new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
        }else{
            return "redirect:";
        }
        return "redirect:";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie= new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
