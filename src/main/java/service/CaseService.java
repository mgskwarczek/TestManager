package service;

import entity.CaseData;
import entity.CasePriorityData;
import entity.CaseStatusData;
import entity.CaseTestTypeData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CasePriorityRepository;
import repository.CaseRepository;
import repository.CaseStatusRepository;
import repository.CaseTestTypeRepository;

import java.time.LocalDateTime;
import java.util.*;

import static utils.Helpers.throwAndLogError;


@Service
public class CaseService {

	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private CaseStatusRepository caseStatusRepository;

	@Autowired
	private CaseTestTypeRepository caseTestTypeRepository;

	@Autowired
	private CasePriorityRepository casePriorityRepository;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private UserService userService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StepService stepService;

	private final Logger logger = LogManager.getLogger(CaseService.class);

	public CaseData getCaseById(Long id) {
		return caseRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "case", id)));
	}

	public CaseData getExistingCaseById(Long id) {
		CaseData cas = caseRepository.findExistingById(id);

		if (cas == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "case", id));
		}

		return cas;
	}

	public void createCase(CaseData cas) {
		cas.setCreationDate(LocalDateTime.now());
		caseRepository.save(cas);
		logger.info("Case with id {} has been created.", cas.getId());
	}

	public void updateCase(CaseData updatedCase) {
		updatedCase.setModificationDate(LocalDateTime.now());
		updatedCase.getSteps().size();
		entityManager.detach(updatedCase);

		CaseData caseData = getExistingCaseById(updatedCase.getId());
		auditLogHeaderService.createObjectChange(caseData, updatedCase, userService.findAdmin(), caseData.getClass().getSimpleName(), caseData.getId());

		caseRepository.save(updatedCase);
		logger.info("Case with id {} has been updated.", caseData.getId());
	}

	public void deleteCase(Long id) {
		CaseData cas = getCaseById(id);
		cas.setDeletionDate(LocalDateTime.now());
		caseRepository.save(cas);
		logger.info("Case with id {} has been deleted.", cas.getId());
	}

	public List<CaseData> getCasesBySuitIdAndCodeAndTitleAndStatusIdAndUser(
			Long suitId,
			String code,
			String title,
			Long statusId,
			String firstName,
			String lastName
	) {
		return caseRepository.findAllBySuitIdAndCodeAndTitleAndStatusIdAndUser(suitId, code, title, statusId, firstName, lastName);
	}

	public CaseTestTypeData getTestTypeById(Long id) {
		return caseTestTypeRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "testType", id)));
	}

	public Long createCaseTestType(CaseTestTypeData caseTestType) {
		caseTestTypeRepository.save(caseTestType);
		logger.info("CaseTestType with name {} and id {} has been created.", caseTestType.getName(), caseTestType.getId());
		return caseTestType.getId();
	}

	public void updateCaseTestType(CaseTestTypeData caseTestType) {
		caseTestTypeRepository.save(caseTestType);
		logger.info("CaseTestType with name {} and id {} has been updated.", caseTestType.getName(), caseTestType.getId());
	}

	public CasePriorityData getCasePriorityById(Long id) {
		return casePriorityRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "casePriority", id));
	}

	public Long createCasePriority(CasePriorityData casePriority) {
		casePriorityRepository.save(casePriority);
		logger.info("Case priority with name {} and id {} has been created.", casePriority.getName(), casePriority.getId());
		return casePriority.getId();
	}

	public void updateCasePriority(CasePriorityData casePriority) {
		casePriorityRepository.save(casePriority);
		logger.info("Case priority with id {} has been updated.", casePriority.getId());
	}

	public CaseStatusData getCaseStatusById(Long id) {
		return caseStatusRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "caseStatus", id)));
	}

	public Long createCaseStatus(CaseStatusData caseStatus) {
		caseStatusRepository.save(caseStatus);
		logger.info("Case status with id {} and name {} has been created.", caseStatus.getId(), caseStatus.getName());
		return caseStatus.getId();
	}

	public void updateCaseStatus(CaseStatusData caseStatus) {
		caseStatusRepository.save(caseStatus);
		logger.info("Status with id {} and name {} has been updated.", caseStatus.getId(), caseStatus.getName());
	}
}
