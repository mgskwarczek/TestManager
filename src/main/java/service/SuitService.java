package service;

import entity.SuitData;
import entity.SuitStatusData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SuitRepository;
import repository.SuitStatusRepository;

import java.time.LocalDateTime;
import java.util.List;

import static utils.Helpers.throwAndLogError;


@Service
public class SuitService {

	@Autowired
	private SuitRepository suitRepository;

	@Autowired
	private SuitStatusRepository suitStatusRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private EntityManager entityManager;

	private final Logger logger = LogManager.getLogger(SuitService.class);

	public SuitData getSuitById(Long id) {
		return suitRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "suit", id)));
	}

	public SuitData getExistingSuitById(Long id) {
		SuitData suit = suitRepository.findExistingById(id);

		if (suit == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "suit", id));
		}

		return suit;
	}

	// czy powinienem zapisywać na projekcie że został dodany suit
	public Long createSuit(SuitData suit) {
		suit.setCreationDate(LocalDateTime.now());
		suitRepository.save(suit);

		logger.info("Suit with id {} has been created.", suit.getId());
		return suit.getId();
	}

	public void updateSuit(SuitData updatedSuit) {
		updatedSuit.setModificationDate(LocalDateTime.now());
		entityManager.detach(updatedSuit);

		SuitData suit = getExistingSuitById(updatedSuit.getId());
		auditLogHeaderService.createObjectChange(suit, updatedSuit, userService.findAdmin(), suit.getClass().getSimpleName(), suit.getId());
		suitRepository.save(updatedSuit);

		logger.info("Suit with id {} has been updated.", updatedSuit.getId());
	}

	// czy usuwanie suita ma usunąć też wszystkie casy i stepy?
	public void deleteSuit(Long id) {
		SuitData suit = getSuitById(id);
		suit.setDeletionDate(LocalDateTime.now());
		suitRepository.save(suit);
		logger.info("Suit with id {} has been deleted.", id);
	}

	public List<SuitData> getSuitsByUserIdAndTitleAndProjectTitleAndDate(
			Long userId,
			String suitTitle,
			String projectTitle,
			LocalDateTime lowerDate,
			LocalDateTime upperDate) {
		return suitRepository.findAllByUserIdAndTitleAndProjectTitleAndModificationDate(userId, suitTitle, projectTitle, lowerDate, upperDate);
	}

	public List<SuitData> getSuitsByProjectIdAndCodeAndTitleAndUser(
			Long projectId,
			String code,
			String title,
			String firstName,
			String lastName
	) {
		return suitRepository.findAllByProjectIdAndCodeAndTitleAndUser(projectId, code, title, firstName, lastName);
	}

	public SuitStatusData getSuitStatusById(Long id) {
		return suitStatusRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "suit status", id)));
	}

	public Long createSuitStatus(SuitStatusData suitStatus) {
		suitStatusRepository.save(suitStatus);
		logger.info("Suit status with id {} has been created.", suitStatus.getId());
		return suitStatus.getId();
	}

	public void updateSuitStatus(SuitStatusData suitStatus) {
		suitStatusRepository.save(suitStatus);
		logger.info("Suit status with id {} has been updated.", suitStatus.getId());
	}

}
