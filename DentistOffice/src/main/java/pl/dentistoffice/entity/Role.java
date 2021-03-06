package pl.dentistoffice.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@NamedQueries({
	@NamedQuery(name = "findRoleWithoutOwner", query = "SELECT role FROM Role role WHERE role.id < 6")
})
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String role;
	private String roleName;
}
