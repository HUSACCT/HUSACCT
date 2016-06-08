package husacct.analyse.task.reconstruct;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ReconstructArchitectureDTO;

public class ReconstructArchitectureDTOList extends AbstractDTO{
	private final Logger logger = Logger.getLogger(ReconstructArchitectureDTOList.class);
	public ArrayList<ReconstructArchitectureDTO> ReconstructArchitectureDTOList;
	private IModelQueryService queryService;
 
	public ReconstructArchitectureDTOList(IModelQueryService qs){
		this.queryService = qs;
	}
	
	
	public void createDynamicReconstructArchitectureDTOs(){
		ReconstructArchitectureDTOList = new ArrayList<ReconstructArchitectureDTO>();
		
		Package reconstructPackage = IAlgorithm.class.getPackage();
		List<Class<?>> classes = this.find(reconstructPackage);
		
		ArrayList<Class<?>> approachClasses = new ArrayList<Class<?>>();
		
		for (Class<?> potentialClass : classes){
			boolean extendsIAlgorithm = IAlgorithm.class.isAssignableFrom(potentialClass);
			boolean isAbstractClass = Modifier.isAbstract(potentialClass.getModifiers());
			if (extendsIAlgorithm && !isAbstractClass){
				logger.info(potentialClass.getName());
				approachClasses.add(potentialClass);
			}
		}

		
		for (Class<?> approachClass : approachClasses){
			try {
				Method m = approachClass.getMethod("getAlgorithmParameterSettings");
				
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

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	public List<Class<?>> find(Package pkg) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String pkgName = pkg.getName();
		String scannedPath = pkgName.replace('.', '/');
		URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null) {
			logger.error(String.format(BAD_PACKAGE_ERROR, scannedPath, pkgName));
		}

		String originalFilePath = scannedUrl.getFile();
		String correctedFilePath = correctSpacesInFilePath(originalFilePath);

		File scannedDir = null;
		try {
			scannedDir = new File(correctedFilePath);
		} catch (Exception e) {
			logger.info(scannedDir + " not found, expecting java is running from JAR");
		}

		if (scannedDir != null && scannedDir.exists()) {
			try {
				for (File file : scannedDir.listFiles()) {
					classes.addAll(findClassesInProject(file, pkgName));
				}
			} catch (Exception e) {
				logger.error("scannedDir is empty: " + e);
			}
		} else {
			try {
				String jarPath = originalFilePath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
				logger.info(jarPath);
				@SuppressWarnings("resource")
				JarFile jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (entryName.startsWith(scannedPath) && entryName.length() > (scannedPath.length() + "/".length())) {
						if (entryName.endsWith(".class")) {
							String className = entryName.replace('/', '.').replace('\\', '.');
							int endIndex = entryName.length() - ".class".length();
							className = className.substring(0, endIndex);
							try {
								classes.add(Class.forName(className));
							} catch (ClassNotFoundException e) {
								logger.error(className + " class could not be added to List<Class<?>> classes " + e);
							}
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(pkgName + " (" + scannedDir + ") does not appear to be a valid package", e);
			}
		}
		return classes;
	}

	private List<Class<?>> findClassesInProject(File file, String pkgName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = pkgName + "." + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(findClassesInProject(child, resource));
			}
		} else if (resource.endsWith(".class")) {
			int endIndex = resource.length() - ".class".length();
			String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException ignore) {
				logger.error(className + " class could not be added to List<Class<?>> classes " + ignore);
			}
		}
		return classes;
	}
	   
	private String correctSpacesInFilePath(String originalPath) {
		String correctedPath = originalPath.replaceAll("%20", " ");
		return correctedPath;
	}
}
