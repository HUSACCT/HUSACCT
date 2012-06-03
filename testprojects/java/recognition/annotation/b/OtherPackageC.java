package annotation.b;

import annotation.a.AnnotationInterface;

public class OtherPackageC {

	@annotation.a.AnnotationInterface(annotation = "")
	private String annotatedAttribute = "annotated";
	
}
