package husaccttest.graphics;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.JInternalFrame;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IServiceListener;
import husacct.validate.IValidateService;
import husacct.validate.domain.validation.Violation;

public class ValidateServiceStub implements IValidateService {

	@Override
	public void addServiceListener(IServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyServiceListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public CategoryDTO[] getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkConformance() {
		// TODO Auto-generated method stub

	}

	@Override
	public Calendar[] getViolationHistoryDates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExportExtentions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Violation> getHistoryViolationsByDate(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportViolations(File file, String fileType, Calendar date) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportViolations(File file, String fileType) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValidated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JInternalFrame getBrowseViolationsGUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JInternalFrame getConfigurationGUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createHistoryPoint(String description) {
		// TODO Auto-generated method stub

	}

}
