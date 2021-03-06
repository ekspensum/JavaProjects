package pl.dentistoffice.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import pl.dentistoffice.entity.Billing;
import pl.dentistoffice.entity.Doctor;
import pl.dentistoffice.entity.Patient;
import pl.dentistoffice.entity.Visit;
import pl.dentistoffice.entity.VisitStatus;
import pl.dentistoffice.entity.VisitTreatmentComment;

public interface VisitRepository {

	public void saveVisit(Visit visit);
	public Visit readVisit(int id);
	public List<Visit> readVisits(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
	public List<Visit> readVisits(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, VisitStatus visitStatus);
	public List<Visit> readVisits(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, VisitStatus visitStatus, Doctor doctor);
	public List<Visit> readVisits(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, boolean visitConfirmation);
	public List<Visit> readVisits(Patient patient, VisitStatus visitStatus);
	public List<Visit> readVisits(Patient patient);
	public List<Visit> readVisits(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Doctor doctor);
	public boolean updateVisitOnFinalize(Visit visit);
	public void removeVisit(Visit visit);
	public boolean isScheduledVisit(LocalDateTime localDateTime, Doctor doctor);
	
	public boolean saveVisitStatus(VisitStatus visitStaus);
	public VisitStatus readVisitStatus(int id);
	public List<VisitStatus> readAllVisitStatus();
	
	public boolean saveBilling(Billing billing);
	public Billing readBilling(int id);
	public List<Billing> readBilings(LocalDate dateFrom, LocalDate dateTo);
	public List<Billing> readBilings(LocalDate dateFrom, LocalDate dateTo, Patient patient);
	public List<Billing> readBilings(LocalDate dateFrom, LocalDate dateTo, Doctor doctor);
	
	public void saveVisitTreatmentComment(VisitTreatmentComment visitTreatmentComment);
	
	public boolean adjustSequenceGeneratorPrimaryKey();
}