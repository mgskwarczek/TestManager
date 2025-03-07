package service;

import entity.RoleData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RoleRepository;

import java.util.List;

import static utils.Helpers.throwAndLogError;

@Service
public class RoleService {
	private final Logger logger = LogManager.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;

	public RoleData getById(Long id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.ROLE_NOT_FOUND)));
	}

	public List<RoleData> getAllRoles(){
		return roleRepository.findAll();
	}
}
