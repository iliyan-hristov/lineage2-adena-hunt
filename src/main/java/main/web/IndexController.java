package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.Player;
import main.service.PlayerService;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final PlayerService playerService;

    public IndexController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public String getIndexPage(){

        return "index";
    }


    @GetMapping("/login")
    public ModelAndView getLoginPage(){

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }


    @GetMapping("/register")
    public ModelAndView getRegisterPage(){

        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }


    @PostMapping("/register")
    public String registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "register";
        }

        playerService.register(registerRequest);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(LoginRequest loginRequest, HttpSession httpSession){



       Player player = playerService.login(loginRequest);
        httpSession.setAttribute("user_id", player.getId());

        return "redirect:/lobby";
    }

    @GetMapping("/lobby")
    public ModelAndView getLobbyPage(){

        ModelAndView modelAndView = new ModelAndView("lobby");

        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){

        httpSession.invalidate();

        return "redirect:/";
    }

}
