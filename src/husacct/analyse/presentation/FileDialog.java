package husacct.analyse.presentation;

import java.awt.Component;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

class FileDialog extends JFileChooser{

	private static final long serialVersionUID = 1L;
	private String buttonText;

	public FileDialog(int selectionMode){
		setFileSelectionMode(selectionMode);
	}
	
	public FileDialog(int selectionMode, String buttonText){
		this.buttonText = buttonText;
		setFileSelectionMode(selectionMode);
	}
	
	public FileDialog(int selectionMode, String buttonText, FileNameExtensionFilter extensionFilter){
		this.buttonText = buttonText;
		setFileSelectionMode(selectionMode);
		setFileFilter(extensionFilter);
		setAcceptAllFileFilterUsed(false);
	}
	
	public FileDialog(int selectionMode, String buttonText, List<FileNameExtensionFilter> extensionFilters){
		this.buttonText = buttonText;
		setFileSelectionMode(selectionMode);
		for(FileNameExtensionFilter filter : extensionFilters){
			addChoosableFileFilter(filter);
		}
		setAcceptAllFileFilterUsed(false);
	}
	
	public int showDialog(Component parent){
		return showDialog(parent, buttonText);
	}
}
