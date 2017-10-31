import java.io.File;

import com.beust.testng.TestNG;

@SuppressWarnings("deprecation")
public class Program {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		 TestNG testng = new TestNG();
         Class[] classes = new Class[]{TestMTD.class};
         testng.setTestClasses(classes);
         testng.run();
	}
}
