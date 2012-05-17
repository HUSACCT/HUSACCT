package husacct.analyse.task.analyser.csharp;

import java.util.Arrays;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class CSharpGenerator {
	public CSharpGenerator() {
		Arrays.sort(typeCollection);
		Arrays.sort(accessorCollection);
	}
	protected final int IDENTIFIER = 4;
	protected final int USING = 18;
	protected final int SEMICOLON = 25;
	protected final int BACKWARDBRACKET = 26;
	protected final int FINAL = 45;
	protected final int NAMESPACE = 61;
	protected final int FORWARDCURLYBRACKET = 62;
	protected final int BACKWARDCURLYBRACKET = 63;
	protected final int IS = 66;
	protected final int NEW	= 68;
	protected final int PUBLIC = 69;
	protected final int PROTECTED = 70;
	protected final int PRIVATE = 71;
	protected final int ABSTRACT = 74;
	protected final int STATIC = 76;
	protected final int VOID = 82;
	protected final int FORWARDBRACKET = 88;
	protected final int COMMA = 89;
	protected final int COLON = 90;
	protected final int CLASS = 155;
	protected final int INT = 164;
	protected final int STRUCT = 169;
	protected final int INTERFACE = 172;
	protected final int RETURN = 153;
	protected final int GET = 156;
	protected final int SET = 157;
	protected final int DOT = 14;
	protected final int EQUALS = 137;
	protected final int BYTE = 161;
	protected final int SBYTE = 160;
	protected final int UINT = 165;
	protected final int SHORT = 162;
	protected final int USHORT = 163;
	protected final int LONG = 166;
	protected final int ULONG = 167;
	protected final int FLOAT = 199;
	protected final int DOUBLE = 198;
	protected final int CHAR = 168;
	protected final int BOOL = 196;
	protected final int OBJECT = 200;
	protected final int STRING = 201;
	protected final int DECIMAL = 197;
	protected final int VAR = 177;
	protected final int INTERNAL = 72;
	protected final int THROW = 190;
	protected final int CATCH = 192;
	protected final int FINALLY = 193;
	protected final int[] typeCollection = new int[] {BYTE, SBYTE, INT, UINT, SHORT, USHORT, LONG, ULONG, FLOAT, DOUBLE, CHAR, BOOL, OBJECT, STRING, VAR, DECIMAL, IDENTIFIER};
	protected final int[] accessorCollection = new int[] {PRIVATE, PUBLIC, PROTECTED, INTERNAL};
	protected ModelCreationService modelService = new FamixCreationServiceImpl();

	
}
