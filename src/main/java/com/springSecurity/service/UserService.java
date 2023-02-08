package com.springSecurity.service;

import com.springSecurity.security.jwt.JwtCreate;
import com.springSecurity.token.TokenModel;
import com.springSecurity.token.TokenRepo;
import com.springSecurity.user.UserModel;
import com.springSecurity.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final JwtCreate jwtCreate;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Value("${spring.mail.username}") String sender;
    @Value("${application.url}") String url;

    public UserService(UserRepo userRepo, TokenRepo tokenRepo, JwtCreate jwtCreate, MailService mailService) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.jwtCreate = jwtCreate;
        this.mailService = mailService;
    }

    public List<UserModel> getUsers(){
        return userRepo.findAll();
    }
    public void saveUser(UserModel user)throws IllegalStateException {
        checkEmail(user.getEmail());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        createToken(user);
    }

    private void checkEmail(String email) {
        checkUniqueEmail(email);
        checkRegexEmail(email);
    }

    private void checkRegexEmail(String email) {
        String regexPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
                + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(!Pattern.compile(regexPattern).matcher(email).matches()){
            throw new IllegalStateException("Wrong email");
        }
    }

    private void checkUniqueEmail(String email) {
        getUsers().forEach(user -> {
            if(user.getEmail().equals(email)){
                throw new RuntimeException("This email was already used to create user");
            }
        });
    }

    @Transactional
    private void createToken(UserModel user) {
        TokenModel token = new TokenModel();
        token.setUser(user);
        token.setValue(jwtCreate.generateJWT(user));
        tokenRepo.save(token);
        sendMail(token.getValue(), user);
    }

    private void sendMail(String value, UserModel user) {
        String link = url + "token?value=" + value;
        String sendTo = user.getEmail();
        String subjectMail = "Confirm your email for finish registration";
        mailService.sendMail(sender, sendTo, subjectMail, link);
    }

    public String compareToken(String token) {
        Optional<TokenModel> tokenValue = tokenRepo.findByValue(token);
        if(tokenValue.isPresent()){
            UserModel userModel = tokenValue.get().getUser();
            userModel.setEnable(true);
            userRepo.save(userModel);
            return "Account is enable";
        }
        return "Incorrect token";
    }
}
