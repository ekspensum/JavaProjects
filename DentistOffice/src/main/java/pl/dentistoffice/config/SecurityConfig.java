package pl.dentistoffice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
		.authoritiesByUsernameQuery("SELECT username, role FROM users_role INNER JOIN users ON users_role.users_id = users.id INNER JOIN role ON users_role.roles_id = role.id WHERE username=?")
		.passwordEncoder(new BCryptPasswordEncoder());
//		.and()
//		.inMemoryAuthentication()
//		.passwordEncoder(new BCryptPasswordEncoder())
//		.withUser("owner")
//		.password(new BCryptPasswordEncoder().encode("owner"))
//		.roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.mvcMatchers("/visit/patient/*", "/users/patient/edit")
		.hasRole("PATIENT")
		.mvcMatchers("/...", "/...", "/...")
		.hasRole("ADMIN")
		.mvcMatchers("/...")
		.hasRole("DOCTOR")
		.mvcMatchers("/...")
		.hasRole("ASSISTANT")
		.mvcMatchers("/...")
		.hasRole("OWNER")
		.mvcMatchers("/loginSuccess")
		.hasAnyRole("PATIENT", "ADMIN", "DOCTOR", "ASSISTANT", "OWNER")
		.and()
		.formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccess").failureUrl("/login?error")
		.and()
		.logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling().accessDeniedPage("/403");
	}

	
}
