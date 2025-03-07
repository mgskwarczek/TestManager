package security;

import entity.RoleData;
import entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		entity.UserData user = userService.getByEmail(email);
		return new User(user.getEmail(), user.getPassword(), mapRoleToAuthority(user.getRole()));
	}

	private Collection<GrantedAuthority> mapRoleToAuthority(RoleData role) {
		Collection<GrantedAuthority> res = new ArrayList<>();
		res.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		return res;
	}
}
