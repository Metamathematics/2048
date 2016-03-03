package helper;
import java.util.List;

public class HelperGame2048 {
	public static <T> void printList(List<T> list) {
		for (T elem : list)
			System.out.println(elem);
	}
}
