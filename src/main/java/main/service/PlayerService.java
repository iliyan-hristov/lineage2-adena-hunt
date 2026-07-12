package main.service;

import jakarta.validation.Valid;
import main.exception.DomainException;
import main.model.Player;
import main.model.PlayerClass;
import main.property.ClassProperties;
import main.repository.PlayerRepository;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClassProperties classProperties;

    public PlayerService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, ClassProperties classProperties) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.classProperties = classProperties;
    }

    public void register(@Valid RegisterRequest registerRequest) {

        Player player = Player.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .nickname(registerRequest.getNickname())
                .level(1)
                .xp(100)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();


        playerRepository.save(player);
    }

    public Player login(LoginRequest loginRequest) {

        Player player = playerRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new DomainException("User or password incorrect!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), player.getPassword())){
            throw new DomainException("User or password incorrect!");
        }

        return player;

    }

    public void selectClass(UUID userId, PlayerClass playerClass) {

        Player player = getById(userId);
        ClassProperties.ClassDetails classDetails = classProperties.getDetailsByPlayerClass(playerClass);

        player.setPlayerClass(playerClass);
        player.setBannerImg(classDetails.getBannerImg());
        player.setHealth(classDetails.getHealthFactor());
        player.setAttack(classDetails.getAttackFactor());
        player.setDefense(classDetails.getDefenseFactor());

        player.setUpdatedOn(LocalDateTime.now());

        playerRepository.save(player);


    }


    public Player getById(UUID playerId) {


        return playerRepository.findById(playerId)
                .orElseThrow(() -> new DomainException("Player not found!"));

    }

}
