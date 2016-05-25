package husacct.analyse.task.reconstruct;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ReconstructArchitectureDTO;

public class ReconstructArchitectureListDTO extends AbstractDTO{
	public ArrayList<ReconstructArchitectureDTO> ReconstructArchitectureDTOList;
	private IModelQueryService queryService;
 
	public ReconstructArchitectureListDTO(IModelQueryService qs){
		this.queryService = qs;
	}
	
	
	public void createDynamicReconstructArchitectureDTOs(){
		ReconstructArchitectureDTOList = new ArrayList<ReconstructArchitectureDTO>();
		
		List<Class<?>> classes = this.find("husacct.analyse.task.reconstruct");
		ArrayList<Class<?>> approachClasses = new ArrayList<Class<?>>();
		
		for (Class<?> potentialClass : classes){
			boolean extendsIAlgorithm = IAlgorithm.class.isAssignableFrom(potentialClass);
			boolean isAbstractClass = Modifier.isAbstract(potentialClass.getModifiers());
			if (extendsIAlgorithm && !isAbstractClass){
				approachClasses.add(potentialClass);
			}
		}

		
		for (Class<?> approachClass : approachClasses){
			try {
				Method m = approachClass.getMethod("getAlgorithmThresholdSettings");
				
				Constructor<?> cons = approachClass.getConstructor(IModelQueryService.class);
				cons.setAccessible(true);
				Object obj = cons.newInstance(queryService);
				m.setAccessible(true);
				ReconstructArchitectureDTO classReconstructArchitectureDTO = (ReconstructArchitectureDTO) m.invoke(obj);
				
				boolean raIsNull = classReconstructArchitectureDTO == null;
				if (!raIsNull){
					boolean raConstantHasValue = classReconstructArchitectureDTO.approachConstant!= null && !classReconstructArchitectureDTO.approachConstant.isEmpty();
					if (raConstantHasValue){
						ReconstructArchitectureDTOList.add(classReconstructArchitectureDTO);
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public ReconstructArchitectureDTO getReconstructArchitectureDTO(String approachConstant){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		for (ReconstructArchitectureDTO dto : ReconstructArchitectureDTOList){
			if (dto.approachConstant.equals(approachConstant)){
				reconstructArchitectureDTO = dto;
			}
		}
		return reconstructArchitectureDTO;
	}
	
	public void updateReconstructArchitectureDTO(ReconstructArchitectureDTO newDTO){
		for(ReconstructArchitectureDTO oldDTO : ReconstructArchitectureDTOList){
			if (oldDTO.approachConstant.equals(newDTO.approachConstant)){
				oldDTO = newDTO;
			}
		}
	}
	
	
	
	 	private static final char PKG_SEPARATOR = '.';

	    private static final char DIR_SEPARATOR = '/';

	    private static final String CLASS_FILE_SUFFIX = ".class";

	    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	    public List<Class<?>> find(String scannedPackage) {
	        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
	        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
	        if (scannedUrl == null) {
	            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
	        }
	        File scannedDir = new File(scannedUrl.getFile());
	        List<Class<?>> classes = new ArrayList<Class<?>>();
	        for (File file : scannedDir.listFiles()) {
	            classes.addAll(find(file, scannedPackage));
	        }
	        return classes;
	    }
	    
	    private List<Class<?>> find(File file, String scannedPackage) {
	        List<Class<?>> classes = new ArrayList<Class<?>>();
	        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
	        if (file.isDirectory()) {
	            for (File child : file.listFiles()) {
	                classes.addAll(find(child, resource));
	            }
	        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
	            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
	            String className = resource.substring(0, endIndex);
	            try {
	                classes.add(Class.forName(className));
	            } catch (ClassNotFoundException ignore) {
	            }
	        }
	        return classes;
	    }

	
	    public String testMethod(){
	    	return "Hallo";
	    }
}
