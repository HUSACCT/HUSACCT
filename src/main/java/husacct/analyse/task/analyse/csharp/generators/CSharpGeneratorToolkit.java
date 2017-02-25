package husacct.analyse.task.analyse.csharp.generators;

import husacct.analyse.task.analyse.VisibilitySet;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.log4j.Logger;

public class CSharpGeneratorToolkit {
	private static final String EMPTYSTRING = "";
	private static final String DOT = ".";
	private static final String COMMA = ",";
    private static final Logger logger = Logger.getLogger(CSharpGeneratorToolkit.class);

    /**
     * Returns the parentname from the stack: IE stack is C.B.A -> "A.B.C"
     * @param parentStack
     */
    public static String getNameFromStack(Stack<String> parentStack) {
        String result = EMPTYSTRING;
        for (String parentNamePart : parentStack) {
            result += parentNamePart + DOT;
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : EMPTYSTRING;
    }

    /**
     * Inserts a dot when parentName is not empty
     * @param parentName
     */
    public static String potentiallyInsertDot(String parentName) {
        if ((parentName == null) || (parentName.equals("")))
        		return "";
        else
        	return ".";
    }

    /**
     * Concatenates two strings and inserts a dot when parentName != null
     * @param parentName
     * @param name
     */
    public static String getUniqueName(String parentName, String name) {
		String result = parentName + potentiallyInsertDot(parentName) + name;
        return result.endsWith(DOT) ? result.substring(0, result.length() -1) : result;
    }
    
    /**
     * Removes a DOT at the beginning of the given name
     * @param name
     */
    public static String removeDotAtBeginningOfName(String name) {
        return name.startsWith(DOT) ? name.substring(1, name.length()) : name;
    }

    /**
     * Removes a DOT at the end of the given name
     * @param name
     */
    public static String removeDotAtEndOfName(String name) {
        return name.endsWith(DOT) ? name.substring(0, name.length() -1) : name;
    }
   
    /**
     * Concatenates two strings by passing through to getUniqueName(String, String)
     */
    public static String belongsToClass(String namespaces, String classes) {
        return getUniqueName(namespaces, classes);
    }

    /**
     * Checks whether or not a Tree has a modifier ABSTRACT (and thus is abstract)
     * Especially useful when checking classes and methods;
     * @param tree The Tree which to check for a modifier
     * @return whether or not the given Tree has a modifier ABSTRACT
     */
    public static boolean isAbstract(Tree tree) {
        CommonTree ct = (CommonTree) tree;
        CommonTree modifierList = (CommonTree) ct.getFirstChildWithType(CSharpParser.MODIFIERS);
        if (modifierList == null || modifierList.getChildCount() < 1) {
            return false;
        } else {
            return modifierList.getFirstChildWithType(CSharpParser.ABSTRACT) != null;
        }
    }

    /**
     * Gets the visibility from a Tree by making use of the VisibilitySet.
     * @param tree The tree to get the visibility from.
     * @return The visiblity of this part of the Tree (IE "public", "package", etc.)
     */
    public static String getVisibility(Tree tree) {
        CommonTree ct = (CommonTree) tree;
        CommonTree modifierList = (CommonTree) ct.getFirstChildWithType(CSharpParser.MODIFIERS);
        if (modifierList == null || modifierList.getChildCount() < 1) {
            return VisibilitySet.DEFAULT.toString();
        } else {
            String found = modifierList.getChild(0).toString();
            if (VisibilitySet.isValidVisibillity(found)) {
                return found;
            } else {
                return VisibilitySet.DEFAULT.toString();
            }
        }
    }

    /**
     * Retrieves a descendant from the ancestor by walking the tree by getting children with the given types.
     * (IE: walkTree(myTree, MODIFIERS, ABSTRACT) gets the child from myTree with type MODIFIERS and from that tree 
     * retrieves the child with ABSTRACT)
     * Especially useful when walking a tree from which is known how it's built.
     * @param ancestor The parent to get the descendant from.
     * @param types the list of types to walk the tree sequentially.
     * @return The descendant which matches the given types.
     */
    public static CommonTree findHierarchicalSequenceOfTypes(CommonTree ancestor, int... types) {
        CommonTree currentParent = ancestor;
        for (int type : types) {
            if (currentParent == null) {
                return null;
            }
            currentParent = (CommonTree) currentParent.getFirstChildWithType(type);
        }
        return currentParent;
    }

    /**
     * Checks whether or not a parent has a certain typed child, including null-check
     */
    public static boolean hasChild(CommonTree parent, int type) {
        if (parent == null) {
            return false;
        }
        return parent.getFirstChildWithType(type) != null;
    }

    /**
     * Deletes all children from a certain Tree
     */
    public static void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount(); child++) {
            treeNode.deleteChild(child);
        }
    }

    /**
     * Creates and returns a String with the names from a Stack, delimited by a comma
     * (IE: ["A", "B", "C"] returns "A,B,C")
     */
    public static String createCommaSeperatedString(Stack<String> names) {
        String result = EMPTYSTRING;
        for (String parentNamePart : names) {
            result += parentNamePart + COMMA;
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : EMPTYSTRING;
    }

    /**
     * Retrieves the Namespace and type parts from a Tree. This occurs in an AST when a variable is 
     * declared with package and/or classnames (when there's no import, for example: 
     * 'public A.B.MyClass mc = new A.B.MyClass();')
     */
    public static String getTypeNameAndParts(CommonTree tree) {
    	String returnValue = EMPTYSTRING;
    	try {
    		if (tree != null) {
    			CommonTree typenameTree;
    			if (tree.getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
    				typenameTree = tree;
    			} else {
    				typenameTree = (CommonTree) tree.getFirstChildWithType(CSharpParser.NAMESPACE_OR_TYPE_NAME); // NAMESPACE_OR_TYPE_NAME
    			}
				if (typenameTree != null) {
					returnValue = getComplete_NAMESPACE_OR_TYPE_NAME_String(typenameTree);
				} else {
					typenameTree = (CommonTree) tree.getFirstChildWithType(CSharpParser.STRING);
					if (typenameTree != null) {
						returnValue += tree.getFirstChildWithType(CSharpParser.STRING).getText();
					} else { 
						typenameTree = (CommonTree) tree.getFirstChildWithType(CSharpParser.INT);
						if (typenameTree != null) {
							returnValue += tree.getFirstChildWithType(CSharpParser.INT).getText();
						} else {
							typenameTree = (CommonTree) tree.getFirstChildWithType(CSharpParser.BOOL);
							if (typenameTree != null) {
								returnValue += tree.getFirstChildWithType(CSharpParser.BOOL).getText();
							} 
						}
					}
				}
    		}
        } catch (Exception e) {
	        logger.warn("Exception: "  + e + ", in getTypeNameAndParts()");
	        //e.printStackTrace();
        }
		return returnValue;
	}

    public static String getComplete_NAMESPACE_OR_TYPE_NAME_String(CommonTree tree) {  
    	String returnValue = "";
    	try {
    		int treeType = tree.getType();
    		switch(treeType) {
	        case CSharpParser.NAMESPACE_OR_TYPE_NAME: case CSharpParser.NAMESPACE_OR_TYPE_PART:
	    		boolean isFirstSubString = true;
	        	for (int i = 0; i < tree.getChildCount(); i++) {
	    			String subString= getComplete_NAMESPACE_OR_TYPE_NAME_String((CommonTree) tree.getChild(i));
	    			if ((subString != null) && !subString.equals("")) {
		        		if (isFirstSubString) { 
		                	returnValue += subString;
		                	isFirstSubString = false;
		                } else {
		                	if (tree.getChild(i).getType() == CSharpParser.TYPE_ARGUMENT_LIST) { // In case of generic classes, add the parameters as <p1>, <p1, p2>, etc.
		                		returnValue += subString;
		                	} else {
		                		returnValue += "." + subString;
		                	}
		                } 
	    			}
	    		}
	            break;
	        case CSharpParser.TYPE_ARGUMENT_LIST: // In case of generic classes, add the parameters as <p1>, <p1, p2>, etc.
	        	String parameters = "";
            	int nrOfParameters = tree.getChildCount();
            	if (nrOfParameters > 0) {
            		for (int f = 0; f < nrOfParameters; f++) {
    	    			String subString= getComplete_NAMESPACE_OR_TYPE_NAME_String((CommonTree) tree.getChild(f));
		            	if ((subString != null) && subString != null) {
		            		if (f == 0) {
		            			parameters += "p" + 1;
		            		} else {
		            			parameters += ", p" + (f+1);
		            		}
		            	}
            		}
            	}
        		returnValue += "<"+ parameters + ">";
	        	break;
	        case CSharpParser.IDENTIFIER:
	        	returnValue += tree.getText();
	            break;
	        case CSharpParser.QUALIFIED_IDENTIFIER: // ? Not encountered within test with Limaki
	        	returnValue = tree.getChild(0).getText();
	            break;
	        case CSharpParser.THIS: 
	        	returnValue = "";
	            break;
	        case CSharpParser.BASE: 
	        	returnValue = "superBaseClass";
	            break;
	        case CSharpParser.DOT: // "."
	        	String left = getComplete_NAMESPACE_OR_TYPE_NAME_String((CommonTree) tree.getChild(0));
	        	String right = getComplete_NAMESPACE_OR_TYPE_NAME_String((CommonTree) tree.getChild(1));
	        	if ((left == "") || (right == "")) {
	        		returnValue += left + right;
	        	} else {
		    		returnValue += left + "." + right;
	        	}
	            break;
	        }
    	} catch (Exception e) {
    		logger.error("Exception: "+ e);
    	}
        return returnValue;
    }

    /**
     * Checks whether or not a tree is of a certain type, including null-check
     */
    public static boolean isOfType(CommonTree tree, int type) {
        if (tree == null) {
            return false;
        }
        return tree.getType() == type;
    }
    
    /**
     * Gets a descendant from a ancestor with a certain type. This method walks
     * the tree breadth-first, to make sure it is the closest relative from the ancestor.
     */
    public static CommonTree getFirstDescendantWithType(CommonTree root, int type) {
    	LinkedList<CommonTree> queue = new LinkedList<>();
    	queue.add(root);
    	while(!queue.isEmpty()) {
    		CommonTree first = queue.removeFirst();
    		if (first != null) {
	    		for (int i = 0; i < first.getChildCount(); i++) {
	    			CommonTree child = (CommonTree)first.getChild(i);
	    			if (isOfType(child, type))
	    				return child;
	    			queue.addLast(child);
	    		}
    		}
    	}
    	return null;
    }
    
    /**
     * Returns the file path of a certain tree, by checking the source name of the input stream.
     * Not every Token has an inputstream, so might return NO FILE FOUND.
     */
    public static String tryToGetFilePath(CommonTree tree) {
    	if (tree == null)
    		return "NO FILE FOUND";
    	CharStream charStream = tree.getToken().getInputStream();
    	if (charStream == null) 
    		return tryToGetFilePath((CommonTree)tree.getParent());
    	else 
    		return tree.getToken().getInputStream().getSourceName();
    }
}
