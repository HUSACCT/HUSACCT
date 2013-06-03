package husacct.define.presentation.utils;

import husacct.define.domain.services.DomainGateway;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class ExpressionEngine {
	private Logger logger;
	private SoftwareUnitJDialog softwareUnitFrame;
public AnalyzedModuleComponent saveRegExToResultTree(String regEx, String packageClass)
{
logger=	Logger.getLogger(ExpressionEngine.class);
	String translatedRegEx = "";
	boolean packagesOnly = false;
	boolean innerClass = false;
	String start = "^";
	String end = "$";

	if (regEx.startsWith("*.") && regEx.endsWith(".*")) {
		packagesOnly = true;
		regEx = regEx.replace("*.", "");
		regEx = regEx.replace(".*", "");
		if (regEx.contains(".")) {
			innerClass = true;
			StringTokenizer stringTokenizer = new StringTokenizer(regEx,
					".");
			while (stringTokenizer.hasMoreElements()) {
				translatedRegEx = translatedRegEx + start
						+ stringTokenizer.nextToken() + end + ".";
			}
			translatedRegEx = translatedRegEx
					.replaceAll("\\.(?!.*\\.)", "");
		} else {
		    translatedRegEx = start + regEx + end;
		}
	} else if (regEx.startsWith("*.")) {
		packagesOnly = true;
		regEx = regEx.replace("*.", "");
		if (regEx.contains(".")) {
			innerClass = true;
			StringTokenizer stringTokenizer = new StringTokenizer(regEx,
					".");
			while (stringTokenizer.hasMoreElements()) {
				translatedRegEx = translatedRegEx + start
						+ stringTokenizer.nextToken() + end + ".";
			}
			translatedRegEx = translatedRegEx
					.replaceAll("\\.(?!.*\\.)", "");
		} else {
		    translatedRegEx = start + regEx + end;
		}
	} else if (regEx.endsWith(".*")) {
		packagesOnly = true;
		regEx = regEx.replace(".*", "");
		if (regEx.contains(".")) {
			innerClass = true;
			StringTokenizer stringTokenizer = new StringTokenizer(regEx,
					".");
			while (stringTokenizer.hasMoreElements()) {
				translatedRegEx = translatedRegEx + start
						+ stringTokenizer.nextToken() + end + ".";
			}
			translatedRegEx = translatedRegEx
					.replaceAll("\\.(?!.*\\.)", "");
		} else {
		    translatedRegEx = start + regEx + end;
		}
	} else if (regEx.startsWith("*") && regEx.endsWith("*")) {
		regEx = regEx.replace("*", "");
		translatedRegEx = regEx;
	} else if (regEx.startsWith("*")) {
		regEx = regEx.replace("*", "");
		translatedRegEx = regEx + end;
	} else if (regEx.endsWith("*")) {
		regEx = regEx.replace("*", "");
		translatedRegEx = start + regEx;
	} else {
		translatedRegEx = start + regEx + end;
	}

	Pattern regExPattern = null;

	for (Iterator<AbstractCombinedComponent> it = JtreeController
			.instance().getRootOfModel().getChildren().iterator(); it
			.hasNext();) {
		AnalyzedModuleComponent module = (AnalyzedModuleComponent) it
				.next();

		if (!packagesOnly && !innerClass) {
			regExPattern = Pattern.compile(translatedRegEx);
			Matcher matcher = regExPattern.matcher(module.getName());
			if (packageClass.equals("P")) {
				if (module.getType().equals("PACKAGE")) {
					while (matcher.find()) {
						logger.info("Adding software unit to module with id "
								+ DomainGateway.getInstance().getSelectedModuleId());
						try {
							JtreeController.instance()
									.additemgetResultTree(module);
						} catch (Exception e) {
							logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame,
									e.getMessage());
						}
					}
				}
			}

			else if (packageClass.equals("C")) {
				if (module.getType().equals("CLASS")
						|| module.getType().equals("INTERFACE")) {
					while (matcher.find()) {
						logger.info("Adding software unit to module with id "
								+ DomainGateway.getInstance().getSelectedModuleId());
						try {
							JtreeController.instance()
									.additemgetResultTree(module);
						} catch (Exception e) {

							logger.error(e.getStackTrace());
							UiDialogs.errorDialog(softwareUnitFrame,
									e.getMessage());
						}
					}
				}
			}

			else if (packageClass.equals("PC")) {
				while (matcher.find()) {
					// logger.info("Adding software unit to module with id "
					// + this.getModuleId());
					try {

						JtreeController.instance().additemgetResultTree(
								module);
					} catch (Exception e) {
						// this.logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame,
								e.getMessage());
					}
				}
			}
			checkChildRegEx(module, regExPattern, packageClass,
					packagesOnly, false);
		}

		else if (packagesOnly && !innerClass) {
			regExPattern = Pattern.compile(translatedRegEx);
			Matcher matcher = regExPattern.matcher(module.getName());
			if (module.getType().equals("PACKAGE")) {
				while (matcher.find()) {
					logger.info("Adding software unit to module with id "
							+ DomainGateway.getInstance().getSelectedModuleId());
					try {
						JtreeController.instance().additemgetResultTree(
								module);
					} catch (Exception e) {
						logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame,
								e.getMessage());
					}
				}
			}
			checkChildRegEx(module, regExPattern, packageClass,
					packagesOnly, false);
		}

		else if (innerClass) {
			StringTokenizer stringTokenizer = new StringTokenizer(
					translatedRegEx, ".");
			String firstPackage = stringTokenizer.nextToken();
			regExPattern = Pattern.compile(firstPackage);
			Matcher matcher = regExPattern.matcher(module.getName());
			if (matcher.find()) {
				translatedRegEx = translatedRegEx.replace(firstPackage
						+ ".", "");
				regExPattern = Pattern.compile(translatedRegEx);
				checkChildRegEx(module, regExPattern, packageClass,
						packagesOnly, true);
			}
		}
	}



return null;


}



public void checkChildRegEx(AnalyzedModuleComponent childModule,
		Pattern pattern, String packageClass, boolean packagesOnly,
		boolean innerClass) {
	String translatedRegEx = pattern.pattern();
	String nextPackage = "";
	if (innerClass) {
		StringTokenizer stringTokenizer = new StringTokenizer(
				translatedRegEx, ".");
		nextPackage = stringTokenizer.nextToken();
		pattern = Pattern.compile(nextPackage);
	}

	for (AbstractCombinedComponent mod : childModule.getChildren()) {
		AnalyzedModuleComponent module = (AnalyzedModuleComponent) mod;

		if (!packagesOnly && !innerClass) {
			Matcher matcher = pattern.matcher(module.getName());
			if (packageClass.equals("P")) {
				if (module.getType().equals("PACKAGE")) {
					while (matcher.find()) {
						logger.info("Adding software unit to module with id "
								+ DomainGateway.getInstance().getSelectedModuleId());
						try {
							JtreeController.instance()
									.additemgetResultTree(module);
						} catch (Exception e) {
							logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame,
									e.getMessage());
						}
					}
				}
			}

			else if (packageClass.equals("C")) {
				if (module.getType().equals("CLASS")
						|| module.getType().equals("INTERFACE")) {
					while (matcher.find()) {
						logger.info("Adding software unit to module with id "
								+ DomainGateway.getInstance().getSelectedModuleId());
						try {
							JtreeController.instance()
									.additemgetResultTree(module);
						} catch (Exception e) {
							logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame,
									e.getMessage());
						}
					}
				}
			}

			else if (packageClass.equals("PC")) {
				while (matcher.find()) {
					logger.info("Adding software unit to module with id "
							+ DomainGateway.getInstance().getSelectedModuleId());
					try {
						JtreeController.instance().additemgetResultTree(
								module);
					} catch (Exception e) {
						logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame,
								e.getMessage());
					}
				}
			}
			checkChildRegEx(module, pattern, packageClass, packagesOnly,
					false);
		}

		else if (packagesOnly && !innerClass) {
			Matcher matcher = pattern.matcher(module.getName());
			if (module.getType().equals("PACKAGE")) {
				while (matcher.find()) {
					logger.info("Adding software unit to module with id "
							+ DomainGateway.getInstance().getSelectedModuleId());
					try {
						JtreeController.instance().additemgetResultTree(
								module);
					} catch (Exception e) {
						logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame,
								e.getMessage());
					}
				}
			}
			checkChildRegEx(module, pattern, packageClass, packagesOnly,
					false);
		}

		else if (innerClass) {
			Matcher matcher = pattern.matcher(module.getName());
			if (matcher.find()) {
				translatedRegEx = translatedRegEx.replace(nextPackage, "");
				if (translatedRegEx.startsWith(".")) {
					translatedRegEx = translatedRegEx.substring(1,
							translatedRegEx.length());
					pattern = Pattern.compile(translatedRegEx);
					checkChildRegEx(module, pattern, packageClass,
							packagesOnly, true);
				} else {
					logger.info("Adding software unit to module with id "
							+ DomainGateway.getInstance().getSelectedModuleId());
					try {
						JtreeController.instance().additemgetResultTree(
								module);
					} catch (Exception e) {
						logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame,
								e.getMessage());
					}
				}
			}
		}
	}
}



	
}
