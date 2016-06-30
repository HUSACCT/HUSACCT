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
	public ArrayList<ReconstructArchitectureDTO> reconstructArchitectureDTOList;
	private IModelQueryService queryService;
 
	public ReconstructArchitectureDTOList(IModelQueryService qs){
		this.queryService = qs;
	}
	
	
	public void createDynamicReconstructArchitectureDTOs(){
		reconstructArchitectureDTOList = new ArrayList<>();
		
		Package reconstructPackage = IAlgorithm.class.getPackage();
		List<Class<?>> allClassesInPackage = this.findAllClassesInPackage(reconstructPackage);
		
		ArrayList<Class<?>> allAlgorithmClasses = findAllAlgorithmClasses(allClassesInPackage);
		
		//AlgorithmParameterSettings are stored in a reconstructArchitectureDTO.
		reconstructArchitectureDTOList = getAllAlgorithmParameterSettings(allAlgorithmClasses);
	}


	private ArrayList<ReconstructArchitectureDTO> getAllAlgorithmParameterSettings(ArrayList<Class<?>> allAlgorithmClasses) {
		ArrayList<ReconstructArchitectureDTO> reconstructArchitectureDTOs = new ArrayList<>();
		for (Class<?> approachClass : allAlgorithmClasses){
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
						reconstructArchitectureDTOs.add(classReconstructArchitectureDTO);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reconstructArchitectureDTOs;
	}


	private ArrayList<Class<?>> findAllAlgorithmClasses(List<Class<?>> classes) {
		ArrayList<Class<?>> approachClasses = new ArrayList<Class<?>>();
		for (Class<?> potentialClass : classes){
			boolean extendsIAlgorithm = IAlgorithm.class.isAssignableFrom(potentialClass);
			boolean isAbstractClass = Modifier.isAbstract(potentialClass.getModifiers());
			if (extendsIAlgorithm && !isAbstractClass){
				// logger.info(potentialClass.getName());
				approachClasses.add(potentialClass);
			}
		}
		return approachClasses;
	}
	
	public ReconstructArchitectureDTO getReconstructArchitectureDTO(String approachConstant){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		for (ReconstructArchitectureDTO dto : reconstructArchitectureDTOList){
			if (dto.approachConstant.equals(approachConstant)){
				reconstructArchitectureDTO = dto;
			}
		}
		return reconstructArchitectureDTO;
	}
	
	public void updateReconstructArchitectureDTO(ReconstructArchitectureDTO newDTO){
		for(ReconstructArchitectureDTO oldDTO : reconstructArchitectureDTOList){
			if (oldDTO.approachConstant.equals(newDTO.approachConstant)){
				oldDTO = newDTO;
			}
		}
	}

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	public List<Class<?>> findAllClassesInPackage(Package pkg) {
		ArrayList<Class<?>> allClassesInPackage = new ArrayList<Class<?>>();
		String pkgName = pkg.getName();
		String packagePath = pkgName.replace('.', '/');
		URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(packagePath);
		
		if (packageUrl != null) {
			String originalFilePath = packageUrl.getFile();
			String filePathWithRegularSpaces = convertSpacesInFilePath(originalFilePath);

			File scannedDir = getFile(filePathWithRegularSpaces);

			boolean isRunnableJar = scannedDir == null || !scannedDir.exists();
			if (!isRunnableJar) {
				allClassesInPackage = getAllClassesInPackageFromProjectFile(pkgName, scannedDir);
			} else {
				allClassesInPackage = getAllClassesInPackageFromJar(pkgName, packagePath, originalFilePath, scannedDir);
			}
		}
		else{logger.error(String.format(BAD_PACKAGE_ERROR, packagePath, pkgName));}
		
		return allClassesInPackage;
	}


	private ArrayList<Class<?>> getAllClassesInPackageFromJar(String pkgName, String packagePath, String originalFilePath, File scannedDir) {
		ArrayList<Class<?>> allClassesInPackage = new ArrayList<>();
		try {
			JarFile jarFile = getJarFile(originalFilePath);
			Enumeration<JarEntry> entries = jarFile.entries();
			
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				
				Class<?> foundClass = findClassInElement(packagePath, entryName);
				if (foundClass != null) allClassesInPackage.add(foundClass);
			}
			jarFile.close();
		} catch (IOException e) {
			throw new RuntimeException(pkgName + " (" + scannedDir + ") does not appear to be a valid package", e);
		}
		return allClassesInPackage;
	}


	private Class<?> findClassInElement(String packagePath, String entryName) {
		Class<?> foundClass = null;
		boolean startsWithPackagePath = entryName.startsWith(packagePath);
		boolean correctPackageLength = entryName.length() > (packagePath.length() + "/".length());
		boolean endsWithClass = entryName.endsWith(".class");
		
		if (startsWithPackagePath && correctPackageLength && endsWithClass) {
			String className = entryName.replace('/', '.').replace('\\', '.');
			int endIndex = entryName.length() - ".class".length();
			className = className.substring(0, endIndex);
			try {
				foundClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.error(className + " class could not be added to List<Class<?>> classes " + e);
			}
		}
		return foundClass;
	}


	private JarFile getJarFile(String originalFilePath) throws IOException {
		String jarPath = originalFilePath.replaceFirst("[.]jar[!].*", ".jar");
		jarPath = jarPath.replaceFirst("file:", "");
		logger.info(jarPath);
		JarFile jarFile = new JarFile(jarPath);
		return jarFile;
	}


	private ArrayList<Class<?>> getAllClassesInPackageFromProjectFile(String pkgName, File scannedDir) {
		ArrayList<Class<?>> classesInPackage = new ArrayList<>();
		try {
			for (File file : scannedDir.listFiles()) {
				List<Class<?>> allClassesInFile = findClassesInFile(file, pkgName);
				classesInPackage.addAll(allClassesInFile);
			}
		} catch (Exception e) {
			logger.error("scannedDir is empty: " + e);
		}
		return classesInPackage;
	}


	private File getFile(String filePathWithRegularSpaces) {
		File scannedDir = null;
		try {
			scannedDir = new File(filePathWithRegularSpaces);
		} catch (Exception e) {
			logger.info(scannedDir + " not found, expecting java is running from JAR");
		}
		return scannedDir;
	}

	private List<Class<?>> findClassesInFile(File file, String pkgName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = pkgName + "." + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(findClassesInFile(child, resource));
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
	   
	private String convertSpacesInFilePath(String originalPath) {
		String correctedPath = originalPath.replaceAll("%20", " ");
		return correctedPath;
	}
}
