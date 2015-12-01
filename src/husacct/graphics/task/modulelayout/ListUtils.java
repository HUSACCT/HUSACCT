package husacct.graphics.task.modulelayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.lambda.functions.implementations.F1;
import org.lambda.functions.implementations.S1;

public class ListUtils {
	
	public static <T> void apply(Collection<T> coll, S1<T> fn) {
		for (T t : coll)
			fn.call(t);
	}
	
	public static <T> T pop(List<T> list) {
		T retVal = list.get(0);
		list.remove(0);
		
		return retVal;
	}
	
	public static <T> List<T> select(Collection<T> coll, F1<T, Boolean> fn) {
		ArrayList<T> results = new ArrayList<T>();
		for (T t : coll)
			if (fn.call(t)) results.add(t);
		
		return Collections.unmodifiableList(results);
	}
}
