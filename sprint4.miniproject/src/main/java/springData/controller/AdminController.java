package springData.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springData.domain.OrganizerUser;
import springData.repository.RoleRepository;
import springData.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new OrganizerUserValidator(userRepo));
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model, String roleName) {
		model.addAttribute("orgUser", new OrganizerUser());
		return "admin/CreateUser";
	}

	@RequestMapping(value = "/create", params = "add", method = RequestMethod.POST)
	public String addNewUser(@RequestParam("roleName") String roleName, @ModelAttribute("orgUser") @Valid OrganizerUser u, BindingResult result,String login, String pwrd, Model model) {
		if(result.hasErrors())
			return "admin/CreateUser";
		else {
			BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
			u.setRole(roleRepo.findByRole(roleName));
			u.setLogin(login);
			u.setPassword(pe.encode(pwrd));
			userRepo.save(u);
			return "admin/done";
		}
	}

	@RequestMapping(value = "create", params = "cancel", method = RequestMethod.POST)
	public String cancelNewTodo() {
		return "admin/done";
	}
}
