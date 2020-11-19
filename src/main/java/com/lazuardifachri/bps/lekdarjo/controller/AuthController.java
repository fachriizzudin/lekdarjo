package com.lazuardifachri.bps.lekdarjo.controller;


import com.lazuardifachri.bps.lekdarjo.security.model.ERole;
import com.lazuardifachri.bps.lekdarjo.security.model.Role;
import com.lazuardifachri.bps.lekdarjo.security.model.User;
import com.lazuardifachri.bps.lekdarjo.repository.RoleRepository;
import com.lazuardifachri.bps.lekdarjo.repository.UserRepository;
import com.lazuardifachri.bps.lekdarjo.security.jwt.JwtUtils;
import com.lazuardifachri.bps.lekdarjo.security.payload.JwtResponse;
import com.lazuardifachri.bps.lekdarjo.security.payload.LoginRequest;
import com.lazuardifachri.bps.lekdarjo.security.payload.MessageResponse;
import com.lazuardifachri.bps.lekdarjo.security.payload.SignUpRequest;
import com.lazuardifachri.bps.lekdarjo.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));

    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username or Email is already taken!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.OK);
    }


    @PostMapping(value = "/signadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username or Email is already taken!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User/Admin registered successfully!"), HttpStatus.OK);
    }


}
