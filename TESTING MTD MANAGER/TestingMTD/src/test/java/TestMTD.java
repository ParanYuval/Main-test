import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestMTD {

	private ExtentReports report;
	private ExtentTest logger;
	private WebDriver driver;	
	public String name=null;
	private String url_add=null;
	private String restartIntervalInMinutes=null;
	private String delayBeforeMove=null; 
	private String admin=null;;
	private String password=null;
	private String browser=null;
	private String ip_DELL=null;
	private String applicationType=null;
	private String parentApplication=null;
	
	//configuration 
	@BeforeClass
	public void configurationLoad() throws Exception
	{
		ReadFromConfig data = new ReadFromConfig();
		name = data.getName();
		url_add= data.getUrl();
		restartIntervalInMinutes=data.getRestartIntervalInMinutes();
		delayBeforeMove=data.getDelayBeforeMove();
		admin=data.getAdmin();
		password=data.getPassword();
		browser=data.getBrowser();
		ip_DELL=data.getIPDELL();
		applicationType=data.getApplicationType();
		parentApplication=data.getParentApplication();
	}

	//	create Report 
	@BeforeClass
	public void createReport() {

		File dir = new File("");
		dir.mkdir();
		report = new ExtentReports("C:\\Test Report MTD Software\\Test Report MTD Software.html");
	}

	public void startTest(String test)
	{
		logger = report.startTest(test);
	}
// 	Check if the Url is existing or not before the selenium start
// Check if the url on the air or not	
	public boolean checkUrlIsExisting(String url_s) throws IOException
	{

		URL url = new URL(url_s);
		HttpURLConnection connection = (HttpURLConnection)		url.openConnection();
		try
		{
			connection.connect();
		    connection.disconnect();
		}
		catch(Exception exp)
		{
			return false;
		}
		return true;
	}

//	Get the url of the mtdmanager and check if the first screen is a login screen	
//	before the test the method check if the url is ok and if the login screen is ok or not	
	public boolean checkLoginScreenIsOpen(String url_string) throws IOException 
	{
		logger.log(LogStatus.INFO, "Try to open the URL (ip of the system)");
		if(!checkUrlIsExisting(url_string))
		{
			logger.log(LogStatus.ERROR, "The url  "+url_string+ "  doesn't work. " );
			return false;
		}
		logger.log(LogStatus.PASS, "The URL is successfully");
		openBrowser();
		driver.get(url_string);
		try{
		if(driver.findElement(By.xpath("//*[@id='ROOT-2521314']/div/div[2]/div/div/div")).isDisplayed())
		{
			logger.log(LogStatus.PASS, "The Login Screen is Ok.");
			return true;
		}
		}
		catch (Exception e) {	
			logger.log(LogStatus.ERROR, "The Login Screen is not work.");
			return false;
		}
		return false;
	}

//	Open the browser
// Open the browser
	public void openBrowser() {
		if(browser.equals("Chrome"))
		{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		}
		if(browser.equals("FireFox"))
		{
		driver = new FirefoxDriver();
		}
		logger.log(LogStatus.INFO, "The Browser started ");
		logger.log(LogStatus.INFO, "Application is up and running");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	
//	Check with the title on the browser if it is the real website or not
	public boolean realWebsite(String webTitle, String checkTitle) {
		if (!webTitle.contains(checkTitle)) {
			logger.log(LogStatus.ERROR, "ERROR to adopt the URL  " + webTitle);
			return false;
		} else
			logger.log(LogStatus.PASS, "Title verified");
		return true;
	}

// After the test this method close the test report	
//	After methods its close the report	
	@AfterMethod
	public void tearDown(ITestResult result) {
		report.endTest(logger);
		report.flush();
	}

// 	Check the Login Screen (2 tests: uncorrect values and correct valuses)	
//	Check Login	
	public void check_LOGIN() throws IOException, InterruptedException 
	{
		logger.log(LogStatus.INFO, " Insert name and password not correct, and check if the system move to the next screen");
//		checkInNOTcorectCred();		
		logger.log(LogStatus.INFO, " Insert name and password correct, and check if the system move to the next screen");
		checkInccorectCred();
	}

//	Check the Login Screen with uncorrct values	
// Check Login (not correct)	
	private void checkInNOTcorectCred() throws InterruptedException {
		// checking the incorrect user and password
		driver.findElement(By.id("gwt-uid-3")).sendKeys("admmmmin");
		driver.findElement(By.id("gwt-uid-5")).sendKeys("111111111111");
		driver.findElement(By.xpath("//*[@id='ROOT-2521314']/div/div[2]/div/div/div/div[3]/div/div[5]/div")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(8000);
		if (driver.findElement(By.xpath("//*[@id='ROOT-2521314']/div/div[2]/div/div/div/div[5]/div/div/div")).isDisplayed())
			logger.log(LogStatus.PASS, "The test success because the user insert name and password are not correct and the system didnt move to next screen");
		else
			logger.log(LogStatus.ERROR, "The Test dont success because The tester insert name and password incorrect and the system pass to the next screen");

	}

//	Check the Login Screen with correct values	
// Check Login (correct)	
	private void checkInccorectCred() throws InterruptedException
	{
		// checking the correct user and password
		driver.findElement(By.id("gwt-uid-3")).clear();
		driver.findElement(By.id("gwt-uid-5")).clear();
		driver.findElement(By.id("gwt-uid-3")).sendKeys(admin);
		driver.findElement(By.id("gwt-uid-5")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id='ROOT-2521314']/div/div[2]/div/div/div/div[3]/div/div[5]/div")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(8500);
		if (driver.getCurrentUrl().contains("login?status")) {
			logger.log(LogStatus.ERROR, "There is a problem with login because the details are correct");
			driver.close();
		}

		if (!driver.getCurrentUrl().contains("login?status")) {
			logger.log(LogStatus.PASS, "The LOGIN with correct user and password is successfully ");
		}
	}


//	open the add new app manager

	
//	Fill correct values in add new App manager	
	//Fill a true values in add a new app manager		
	public boolean FillInAddNewAppManagerTrueValues()
	{
		try
		{
			
			driver.findElement(By.xpath("//*[@id='gwt-uid-15']")).sendKeys(name);
			driver.findElement(By.xpath("//*[@id='gwt-uid-19']")).sendKeys(parentApplication);
			driver.findElement(By.xpath("//*[@id='gwt-uid-21']")).sendKeys(url_add);
			driver.findElement(By.xpath("//*[@id='gwt-uid-23']")).sendKeys(restartIntervalInMinutes);
			driver.findElement(By.xpath("//*[@id='gwt-uid-25']")).sendKeys(delayBeforeMove);
			driver.findElement(By.id("gwt-uid-27")).sendKeys(admin);
			driver.findElement(By.id("gwt-uid-27")).sendKeys(Keys.TAB);
			driver.findElement(By.id("gwt-uid-29")).sendKeys(password);

		}
		catch (Exception e) {
			return false;
		}
		logger.log(LogStatus.INFO, "In the add new Application Manager the robot fill valid values in all the text box fields");
		return true;
	}

//	Fill uncorrect values in add new App manager	
//	Insert incorrect values to the add new app and check the system. 	
	public boolean InsertIncorrectValues(String xpath, String nameInboxTxt,String incorrectValue, String orginalValue) throws InterruptedException
	{
		try
		{
			driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(incorrectValue);
			Thread.sleep(500);

			driver.findElement(By.xpath("//*[@id='ROOT-2521314-overlays']/div[3]/div/div/div[3]/div/div/div[2]/div/div/div/div/div[1]/div")).click();
		
			Thread.sleep(500);
		}
		catch (Exception e) {
			driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(orginalValue);

			//הודעה בדוח שיש בעיה בהכנסה של ערך לא תקין
			return false;
		}
		if(checkSystemInAddNewAppPass(nameInboxTxt))
		{	
			Thread.sleep(2800);
			driver.findElement(By.xpath(xpath)).clear();
			Thread.sleep(1000);
			driver.findElement(By.xpath(xpath)).sendKeys(orginalValue);
			return true;
		}
		return false;
	}
	
//	After fill uncorrect value and click on "ok" check if it pass or not	
// Check if thw windows "add new app" is Existing
	public boolean checkSystemInAddNewAppPass(String str)
	{
		try
		{
			WebElement element = driver.findElement(By.xpath("//*[@id='gwt-uid-15']"));

		}
		catch (Exception e)
		{
			logger.log(LogStatus.ERROR, "The system could not find the error in the " + str);
			return false;		
		}
		if(driver.findElement(By.xpath("//*[@id='gwt-uid-15']")).isDisplayed())
		logger.log(LogStatus.PASS, "The robot fill uncorrect value in each field and the system recognized the wrong value " + str);

		return true;	
	}
	
//	Insert uncoorect values in add new app		
//	Insert uncorrect values to the add new app and check the system. 		
	public boolean InsertuncorrectValues() throws InterruptedException
	{
		if(!InsertIncorrectValues("//*[@id='gwt-uid-15']","Name App","",name))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-19']","Parent application","",parentApplication))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-21']","URL","",url_add))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-23']","Restart Interval In Minutes","",restartIntervalInMinutes))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-23']","Restart Interval In Minutes","String",restartIntervalInMinutes))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-25']","Delay Before Move","",delayBeforeMove))
			return false;
		if(!InsertIncorrectValues("//*[@id='gwt-uid-3000']","Delay Before Move","String",delayBeforeMove))
			return false;
		return true;
	}
	
//	Check all App manager	
//	Check the App manager screen
	public boolean Check_Application_manager() throws IOException, InterruptedException 
	{ 	
		if(!Opentab("div.valo-menu-item:nth-child(1) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Application manager"))
			return false;
		// add new app
		return true;
	}

//	check if the add app work or not	
// After correct values check if pass or not	
	public boolean checkIfAddPass() throws InterruptedException
	{
		if(rowsInApp()==0)
		{
			logger.log(LogStatus.ERROR, "Add new app failed");
			return false;
		}
		String newAddTxt=driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[3]/div/div[2]/div[1]/table/tbody/tr["+rowsInApp()+"]/td[1]/div")).getText();
		if(name.equals(newAddTxt))
		{
			logger.log(LogStatus.PASS, "Add new app successful");
			return true;		
		}
		logger.log(LogStatus.ERROR, "Add new app failed");
		return false;
	}
	
//	Number of the rows in the app table

//	count of row in App manager	
	public int rowsInApp() throws InterruptedException
	{
		Thread.sleep(5000);

		return ((driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[3]/div/div[2]/div[1]")).getSize().getHeight()))/41;
	}

// After the add App enter to first app	
	public boolean clickSomeAppManager() throws InterruptedException
	{
		logger.log(LogStatus.INFO, "Try to open the App Manager View");
		try{
			//FireFOX
		//	driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[3]/div/div[2]/div[1]/table/tbody/tr[1]/td[1]/div/div/span")).click();
			driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[3]/div/div[2]/div[1]/table/tbody/tr[1]/td[1]/div/div")).click();

		}
		
		catch (Exception e) {
			logger.log(LogStatus.ERROR, "The system was not open the App Manager View");
			// TODO: handle exception
			return false;
		}
		if(CheckIfAppManagerViewOpen())
		{
			logger.log(LogStatus.PASS, "The App Manager View is opened ");
			return true;
		}
		logger.log(LogStatus.ERROR, "The system was not open the App Manager View");
		return false;
	}
		

// Check if the App manager view open or not
	public boolean CheckIfAppManagerViewOpen() throws InterruptedException
	{
		Thread.sleep(1500);
		try{
			if(driver.findElement(By.xpath("//*[@id='v-header-id']/div[3]/div")).isDisplayed())
				return true;
		}
		catch (Exception e) {
			return false;
			// TODO: handle exception
		}
		return false;
	}

// 	Check the app manager view
	public boolean checkAppManagerView() throws InterruptedException
	{
		if(!clickSomeAppManager())
			return false;
		if(!turnOnMTD())
			 return false;
		if(!checkValuesInApp("#gwt-uid-27","String", "Restart rate (minutes)"))
			return false;			
		if(!checkValuesInApp("#gwt-uid-29","String", "Session dilution time (seconds):"))
			return false;	
		if(!checkValuesInApp("#gwt-uid-31","String", "Number of instances:"))
			return false;	
		if(!checkSameRowInNumberofinstances("#gwt-uid-31"))
			return false;
		return true;

	}
//	check Same Row In Number of instances
	public boolean checkSameRowInNumberofinstances(String xpath) throws NumberFormatException, InterruptedException
	{
		logger.log(LogStatus.INFO, "Check that number of instances field is equal to number of rows");
		String orginalValue=driver.findElement(By.cssSelector(xpath)).getAttribute("value");

		if(numberOfRowInAppManagerSpecifiction()==Integer.parseInt(orginalValue))
		{
			logger.log(LogStatus.PASS, "The number of instances field is equal to number of rows");
			return true;
		}
		else
		{
			logger.log(LogStatus.ERROR, "The number of instances field is not equal to number of rows");
			return false;
		}
	}
	
//	Check if the mtd work or not if not the test start mtd	
	public boolean turnOnMTD()
	{
		try
		{
		if(driver.findElement(By.cssSelector("#BaseView > div.v-panel-content.v-panel-content-borderless.v-scrollable > div > div > div > div.v-slot.v-slot-profile-form.v-slot-dashboard-view > div > div:nth-child(1) > div > div.v-slot.v-slot-v-appinstance-buttons > div > div:nth-child(1)")).getText().equals("Start MTD"))
		{
			driver.findElement(By.cssSelector("#BaseView > div.v-panel-content.v-panel-content-borderless.v-scrollable > div > div > div > div.v-slot.v-slot-profile-form.v-slot-dashboard-view > div > div:nth-child(1) > div > div.v-slot.v-slot-v-appinstance-buttons > div > div:nth-child(1)")).click();
			logger.log(LogStatus.INFO, "The MTD didnt work. The system turned on the MTD");
		}
		}
		catch (Exception e) {
			return false;
			// TODO: handle exception
		}
		return true;
	}
	
	//Check all the values in app manager view
	public boolean checkValuesInApp(String xpath, String valueToTest, String toCheck) throws InterruptedException
	{
		Thread.sleep(3000);
		logger.log(LogStatus.INFO, "The test Check the: " +toCheck);
		try
		{
		String orginalValue=driver.findElement(By.cssSelector(xpath)).getAttribute("value");
		driver.findElement(By.cssSelector(xpath)).clear();
		driver.findElement(By.cssSelector(xpath)).sendKeys(valueToTest);
		Thread.sleep(2500);
		driver.findElement(By.cssSelector("#BaseView > div.v-panel-content.v-panel-content-borderless.v-scrollable > div > div > div > div.v-slot.v-slot-profile-form.v-slot-dashboard-view > div > div:nth-child(1) > div > div.v-slot.v-slot-v-appinstance-buttons > div > div:nth-child(3)")).click();
		Thread.sleep(2500);
		driver.findElement(By.cssSelector("#BaseView > div.v-panel-content.v-panel-content-borderless.v-scrollable > div > div > div > div.v-slot.v-slot-profile-form.v-slot-dashboard-view > div > div:nth-child(1) > div > div.v-slot.v-slot-v-appinstance-buttons > div > div.v-slot.v-slot-borderless")).click();
		Thread.sleep(2500);
		System.out.println(driver.findElement(By.cssSelector(xpath)).getAttribute("value")+"   "+ orginalValue);
		if(driver.findElement(By.cssSelector(xpath)).getAttribute("value").equals(orginalValue))
		{
			logger.log(LogStatus.PASS, "The robot change the field: "+toCheck+" to a wrong value, the system will return the previous correct value.");
			return true;
		}
		else 
		{
			logger.log(LogStatus.ERROR, "The test didnt Succeess to Check the: " +toCheck);
			return false;
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			logger.log(LogStatus.ERROR, "The test didnt Succeess to Check the (Exception): " +toCheck);
			return false;
		}	
	}
	

	public int numberOfRowInAppManagerSpecifiction() throws InterruptedException
	{
		Thread.sleep(2000);
		return (((driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[3]/div/div[2]/div[1]/table")).getSize().getHeight()))/41);
	}

	//	after the login, check that the all tab working	
	public boolean Opentab(String cssSelectorTab, String xpathSame, String tabName) throws IOException, InterruptedException 
	{
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(cssSelectorTab)).click();
		Thread.sleep(2700);

		if(driver.findElement(By.xpath(xpathSame)).getText().equals(tabName))
		{
			logger.log(LogStatus.PASS,tabName+" is Open" );
			return true;
		} 
		else 
		{
			logger.log(LogStatus.ERROR,tabName+ " not working");
			return false;
		}
	}
	
	// Test Open tab
	@Test(dependsOnMethods="TestLogin")
	public void testAllTab() throws IOException, InterruptedException, Exception
	{
		try
		{
			startTest("Open All tab");
			Opentab("div.valo-menu-item:nth-child(1) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Application manager");
			Opentab("div.valo-menu-item:nth-child(2) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Topology");
			Opentab("div.valo-menu-item:nth-child(3) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Alerts");
			Opentab("div.valo-menu-item:nth-child(4) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Container registry");
			Opentab("div.valo-menu-item:nth-child(5) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","Honeypot");
			Opentab("div.valo-menu-item:nth-child(6) > span:nth-child(1) > span:nth-child(2)","//*[@id='v-header-id']/div[3]/div","RMS Logs");				
		}			
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	// Test
	@Test
	public void TestLogin() throws IOException, InterruptedException, Exception{
		
//		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Yuval\\Desktop\\DELL\\geckodriver.exe");
		startTest("Login");
		checkLoginScreenIsOpen(ip_DELL);
		check_LOGIN();	
	}

	
//	Open the Add new APP	
	public boolean OpenTheAddNewAppManger()
	{		
		try{
			logger.log(LogStatus.INFO, "Try to open the Add New Application Manager");
			driver.findElement(By.xpath("//*[@id='BaseView']/div[2]/div/div/div/div[2]/div/div[1]/div/div/div/div/div")).click();
		}
		catch (Exception e) {
			logger.log(LogStatus.ERROR, "dosent success to open the Add New Application Manager");
			return false;
		}
		logger.log(LogStatus.PASS, "Success to open the Add New Application Manager");
		return true;
	}

	
	@Test(dependsOnMethods="testAllTab")
	public void TestCheckAppManager() throws IOException, InterruptedException, Exception
	{
		startTest("Test CheckApp Manager");
		Check_Application_manager();
	}
/*	
	@Test(dependsOnMethods="TestCheckAppManager")
	public boolean TestAddNewAPPManager() throws IOException, InterruptedException, Exception
	{
		startTest("Test Add New APP Manager()");
		if(!OpenTheAddNewAppManger())
			return false;
		if(!FillInAddNewAppManagerTrueValues())
			return false;
		if(!InsertuncorrectValues())
			return false;
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='ROOT-2521314-overlays']/div[3]/div/div/div[3]/div/div/div[2]/div/div/div/div/div[1]/div")).click();
		if(!checkIfAddPass())
			return false;
		return false;	
	}
*/
	
	@Test(dependsOnMethods="TestCheckAppManager")
	public void TestAddNewAPPManager() throws IOException, InterruptedException, Exception
	{
		try{
		startTest("Test Add New APP Manager()");
		OpenTheAddNewAppManger();
		FillInAddNewAppManagerTrueValues();
		InsertuncorrectValues();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='ROOT-2521314-overlays']/div[3]/div/div/div[3]/div/div/div[2]/div/div/div/div/div[1]/div")).click();
		checkIfAddPass();
		}
		catch (Exception e) {
			logger.log(LogStatus.FATAL, e+" notttttt");
		}
	}
	@Test(dependsOnMethods="TestAddNewAPPManager")
	public void TestcheckAppManagerView() throws IOException, InterruptedException, Exception
	{
		startTest("Test check App Manager View");
		checkAppManagerView();
	}	
}
