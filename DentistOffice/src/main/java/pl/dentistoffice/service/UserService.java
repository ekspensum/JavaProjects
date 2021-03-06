package pl.dentistoffice.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.dentistoffice.dao.UserRepository;
import pl.dentistoffice.entity.Admin;
import pl.dentistoffice.entity.Assistant;
import pl.dentistoffice.entity.Doctor;
import pl.dentistoffice.entity.Patient;
import pl.dentistoffice.entity.Role;
import pl.dentistoffice.entity.User;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
		
    @Autowired
    private HibernateSearchService searchsService;
	
	@Autowired
	private ActivationService activationService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserService() {
		super();
	}

//	for tests
	public UserService(UserRepository userRepository, NotificationService notificationService, ActivationService activationService, 
									HibernateSearchService searchsService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.notificationService = notificationService;
		this.activationService = activationService;
		this.searchsService = searchsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewDoctor(Doctor doctor) throws Exception {
		User user = doctor.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		doctor.setRegisteredDateTime(LocalDateTime.now());
		userRepository.saveDoctor(doctor);
		notificationService.sendEmailWithRegisterNotification(doctor);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void editDoctor(Doctor doctor) throws Exception {
		User user = doctor.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		List<Role> currentRolesList = createCurrentRolesList(user);
		user.setRoles(currentRolesList);
		doctor.setEditedDateTime(LocalDateTime.now());
		userRepository.updateDoctor(doctor);
	}
	
	public Doctor getDoctor(int id) {
		return userRepository.readDoctor(id);
	}
	
	public List<Doctor> getAllDoctors(){
		List<Doctor> allDoctors = userRepository.readAllDoctors();
		allDoctors.sort(new Comparator<Doctor>() {

			@Override
			public int compare(Doctor o1, Doctor o2) {
				return o1.getLastName().compareTo(o2.getLastName());
			}
		});
		return allDoctors;
	}
	
	public Doctor getLoggedDoctor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Doctor doctor = userRepository.readDoctor(username);
		return doctor;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewAssistant(Assistant assistant) throws Exception {
		User user = assistant.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		assistant.setRegisteredDateTime(LocalDateTime.now());
		userRepository.saveAssistant(assistant);
		notificationService.sendEmailWithRegisterNotification(assistant);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void editAssistant(Assistant assistant) throws Exception {
		User user = assistant.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		assistant.setEditedDateTime(LocalDateTime.now());
		List<Role> currentRolesList = createCurrentRolesList(user);
		user.setRoles(currentRolesList);
		userRepository.updateAssistant(assistant);
	}
	
	public Assistant getAssistant(int id) {
		return userRepository.readAssistant(id);
	}
	
	public List<Assistant> getAllAssistants(){
		List<Assistant> allAssistants = userRepository.readAllAssistants();
		allAssistants.sort(new Comparator<Assistant>() {

			@Override
			public int compare(Assistant o1, Assistant o2) {
				return o1.getLastName().compareTo(o2.getLastName());
			}
		});
		return allAssistants;
	}

	public Assistant getLoggedAssistant() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Assistant assistant = userRepository.readAssistant(username);
		return assistant;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewPatient(Patient patient, String contextPath) throws Exception {
		User user = patient.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		List<Role> patientRole = getPatientRole();
		user.setRoles(patientRole);
		user.setEnabled(false);
		patient.setUser(user);
		patient.setRegisteredDateTime(LocalDateTime.now());	
		String activationString = UUID.randomUUID().toString();
		patient.setActivationString(activationString);
		userRepository.savePatient(patient);
		activationService.sendEmailWithActivationLink(patient, contextPath);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void editPatient(Patient patient) throws Exception {
		User user = patient.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		patient.setEditedDateTime(LocalDateTime.now());
		userRepository.updatePatient(patient);
	}
	
	public Patient getPatient(int id) {
		return userRepository.readPatient(id);
	}

	public Patient getLoggedPatient() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Patient patient = userRepository.readPatient(username);
		return patient;
	}
	
	public List<Patient> searchPatient(String text){
		List<Patient> searchPatient = searchsService.searchPatientNamePeselStreetPhoneByKeywordQuery(text);
		searchPatient.sort(new Comparator<Patient>() {

			@Override
			public int compare(Patient o1, Patient o2) {
				return o1.getLastName().compareTo(o2.getLastName());
			}
		});
		return searchPatient;
	}
	
	public Patient findMobilePatientByToken(String token) {
		try {
			Patient mobilePatient = userRepository.readPatientByToken(token);
			return mobilePatient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteTokenForMobilePatient(int patientId) {
		int deleteTokenForPatient = userRepository.deleteTokenForPatient(patientId);
		if(deleteTokenForPatient == 1) {
			return true;
		}
		return false;
	}
	
	public Patient loginMobilePatient(String username, String rawPassword) {
		try {
			Patient mobilePatient = userRepository.readPatient(username);
			boolean matchesPassword = passwordEncoder.matches(rawPassword, mobilePatient.getUser().getPassword());
			if(matchesPassword && mobilePatient.getUser().isEnabled()) {
	            String token = UUID.randomUUID().toString();
	            mobilePatient.setToken(token);
	            userRepository.updatePatient(mobilePatient);
				return mobilePatient;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void addNewAdmin(Admin admin) throws Exception {
		User user = admin.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		admin.setRegisteredDateTime(LocalDateTime.now());
		userRepository.saveAdmin(admin);
		notificationService.sendEmailWithRegisterNotification(admin);
	}
		
	public Admin getLoggedAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Admin admin = userRepository.readAdmin(username);
		return admin;
	}
	
	public Admin getAdmin(int id) {
		return userRepository.readAdmin(id);
	}
	
	public List<Admin> getAllAdmins(){
		List<Admin> allAdmins = userRepository.readAllAdmins();
		allAdmins.sort(new Comparator<Admin>() {

			@Override
			public int compare(Admin o1, Admin o2) {
				return o1.getLastName().compareTo(o2.getLastName());
			}
		});
		return allAdmins;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void editAdmin(Admin admin) throws Exception {
		User user = admin.getUser();
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordField()));
		admin.setEditedDateTime(LocalDateTime.now());
		List<Role> currentRolesList = createCurrentRolesList(user);
		user.setRoles(currentRolesList);
		userRepository.updateAdmin(admin);
	}
	
	public Collection<? extends GrantedAuthority> getAuthoritiesLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		return authorities;
	}
	
	public List<Role> getPatientRole() {
		List<Role> allRoles = userRepository.readAllRoles();
		ListIterator<Role> listIterator = allRoles.listIterator();
		while (listIterator.hasNext()) {
			Role role = (Role) listIterator.next();
			if(!role.getRole().equals("ROLE_PATIENT")) {
				listIterator.remove();
			}
		}
		return allRoles;
	}
	
	public List<Role> getAllRoles(){
		List<Role> allRoles = userRepository.readAllRoles();
		allRoles.sort(new Comparator<Role>() {

			@Override
			public int compare(Role o1, Role o2) {
				return o1.getId() - o2.getId();
			}
		});
		return allRoles;
	}
	
	public List<Role> getEmployeeRolesWithoutId(int withoutId){
		List<Role> allRoles = userRepository.readAllRoles();
		ListIterator<Role> listIterator = allRoles.listIterator();
		while (listIterator.hasNext()) {
			Role role = (Role) listIterator.next();
			if(role.getId() == 3 || role.getId() == withoutId) {
				listIterator.remove();
			}
		}
		allRoles.sort(new Comparator<Role>() {

			@Override
			public int compare(Role o1, Role o2) {
				return o1.getId() - o2.getId();
			}
		});
		return allRoles;
	}
	
	public Map<DayOfWeek, Map<LocalTime, Boolean>> getTemplateWorkingWeekMap() {
		Map<DayOfWeek, Map<LocalTime, Boolean>> templateMap = new LinkedHashMap<>();
		Map<LocalTime, Boolean> weekDayTimeMap;
		DayOfWeek [] daysOfWeek = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
		String [] weekDayTime = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", 
								"15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30"};
		
		for(int i = 0; i<daysOfWeek.length; i++) {
			weekDayTimeMap = new LinkedHashMap<>();
			for (int j = 0; j < weekDayTime.length; j++) {
				weekDayTimeMap.put(LocalTime.parse(weekDayTime[j]), false);
			}
			templateMap.put(daysOfWeek[i], weekDayTimeMap);			
		}
		
		return templateMap;
	}
	
	public String [] dayOfWeekPolish() {
		String [] dayOfWeekPolish = {"Zero", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"};
		return dayOfWeekPolish;
	}
	
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userRepository.readUser(username);
		return user;
	}
	
	public boolean checkDinstinctLoginWithRegisterUser(String login) {
		try {
			userRepository.readUser(login);
		} catch (NoResultException e) {
			return true;
		}
		return false;
	}
	
	public boolean checkDinstinctLoginWithEditUser(String login, int editUserId) {
		User user = userRepository.readUser(editUserId);
		if(user.getUsername().equals(login)) {
			return true;
		} else {
			try {
				userRepository.readUser(login);
			} catch (NoResultException e) {
				return true;
			}			
		}
		return false;
	}
	
//	PRIVATE/PROTECTED METHODS
	protected List<Role> createCurrentRolesList(User user){
		List<Role> selectedIdRoles = user.getRoles(); //only id is selected on page. Role and roleName was't change
		List<Role> currentRolesList = new ArrayList<>();
		Role currentRole;
		for (Role role : selectedIdRoles) {
			currentRole = new Role();
			currentRole.setId(role.getId());
			currentRolesList.add(currentRole);
		}
		return currentRolesList;
	}	
}
