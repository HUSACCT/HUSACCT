package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.validation.Violation;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.JInternalFrame;

public interface IValidateService
{
	public CategoryDTO[] getCategories();
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo);
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo);
	public String[] getExportExtentions();
	public void checkConformance();
	@Deprecated
	public void exportViolations(String name, String fileType, String path);
	public void exportViolations(File file, String fileType, Calendar date);
	public boolean isValidated();
	public JInternalFrame getBrowseViolationsGUI();
	public JInternalFrame getConfigurationGUI();
	public List<Violation> getViolationsByDate(Calendar date);
	public Calendar[] getViolationHistoryDates();
	public void saveInHistory(String description);
	public JInternalFrame getViolationHistoryGUI();
}