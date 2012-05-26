package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IObservableService;
import husacct.validate.domain.validation.Violation;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.JInternalFrame;

public interface IValidateService extends IObservableService
{
	public CategoryDTO[] getCategories();

	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo);
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo);

	public void checkConformance();

	public Calendar[] getViolationHistoryDates();
	public String[] getExportExtentions();
	public List<Violation> getHistoryViolationsByDate(Calendar date);
	
	public void exportViolations(File file, String fileType, Calendar date);	
	public void exportViolations(File file, String fileType);

	public boolean isValidated();

	public JInternalFrame getBrowseViolationsGUI();
	public JInternalFrame getConfigurationGUI();

	public void createHistoryPoint(String description);
}