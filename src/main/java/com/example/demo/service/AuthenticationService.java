package com.example.demo.service;

import com.example.demo.repository.InvalidateTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.InvalidateToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {

    UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    InvalidateTokenRepository invalidateTokenRepository;

    @NonFinal
    @Value("${jwt}")
    protected String Key;


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = repository.findByUsername(request.getUsername()).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXITED)
        );
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated){
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public String generateToken(User user)  {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("mxb.com")
                .issueTime(new Date())
                .claim("scope",buildscope(user))
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);

        try {
            jwsObject.sign(new MACSigner(Key.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("Can not create token");
            throw new RuntimeException(e);
        }
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        var token = request.getToken();

        boolean isValid = true;
        try{
                verifierToken(token);
            }catch (AppException e){
                isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }
    private String buildscope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermission()))
                    role.getPermission().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private SignedJWT verifierToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(Key.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!verified && expityTime.after(new Date())){
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new  AppException(ErrorCode.UN_AUTHENTICATED);
        }


        return signedJWT;
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifierToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jit)
                .expritime(expTime)
                .build();

        invalidateTokenRepository.save(invalidateToken);
    }
}
