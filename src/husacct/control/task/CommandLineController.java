package husacct.control.task;

import org.apache.log4j.Logger;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.QualifiedSwitch;
import com.martiansoftware.jsap.Switch;


public class CommandLineController {
	
	private Logger logger = Logger.getLogger(CommandLineController.class);
	
	JSAP jsap = new JSAP();
	private JSAPResult jsapResult;
	
	public CommandLineController() {
		
		Switch noguiOption = new Switch("nogui");
		noguiOption.setLongFlag("nogui");
		
		FlaggedOption bootstrapOption = new QualifiedSwitch ("bootstrap");
		bootstrapOption.setStringParser(JSAP.STRING_PARSER);
		bootstrapOption.setLongFlag("bootstrap");
		bootstrapOption.setRequired(false);
		bootstrapOption.setList(true);
		bootstrapOption.setListSeparator(',');
		
		try {
			jsap.registerParameter(noguiOption);
			jsap.registerParameter(bootstrapOption);
		} catch (JSAPException exception) {
			logger.debug(exception.getMessage());
		}
		
		//logger.info(jsap.getUsage());
	}
	
	public void parse(String[] commandLineArguments){
		jsapResult = jsap.parse(commandLineArguments);
		if(!jsapResult.success()){
			logger.debug(jsapResult.getException(null));
		}
	}
	
	public JSAPResult getResult(){
		return jsapResult;
	}
	
}
