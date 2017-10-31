import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;	
public class ReadFromConfig {

	public Properties prop=null;
	public InputStream input=ReadFromConfig.class.getClassLoader().getResourceAsStream("config.properties");
	
	
	@Override
	public String toString() {
		return "ReadFromConfig [prop=" + prop + ", input=" + input + "]";
	}

	public ReadFromConfig() throws Exception
	{
		prop= new Properties();
		prop.load(input);
	}
	
	public String getName()
	{
		return prop.getProperty("name");
	}
	
	public String getUrl()
	{
		return prop.getProperty("url_add");
	}
	
	public String getRestartIntervalInMinutes()
	{
		return prop.getProperty("restartIntervalInMinutes");
	}
	
	public String getDelayBeforeMove()
	{
		return prop.getProperty("delayBeforeMove");
	}
	
	public String getAdmin()
	{
		return prop.getProperty("admin");
	}
	
	public String getPassword()
	{
		return prop.getProperty("password");
	}
	
	public String getBrowser()
	{
		return prop.getProperty("browser");
	}
	
	public String getIPDELL()
	{
		return prop.getProperty("iP_DELL");
	}
	
	public String getApplicationType()
	{
		return prop.getProperty("ApplicationType");
	}
	
	public String getParentApplication()
	{
		return prop.getProperty("ParentApplication");
	}
	
	public static void main(String[] args) {
		try {
			Properties prop=new Properties();
			InputStream file = ReadFromConfig.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(file);

			System.err.println("Name : "+ prop.getProperty("name"));
			System.err.println("URL : "+ prop.getProperty("url_add"));
			System.err.println("Restart Interval In Minutes : "+ prop.getProperty("restartIntervalInMinutes"));
			System.err.println("Delay Before Move : "+ prop.getProperty("delayBeforeMove"));
			System.err.println("Admin : "+ prop.getProperty("admin"));
			System.err.println("Password : "+ prop.getProperty("password"));
			

			file.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
