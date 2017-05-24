/**
 * 
 */
package net.beotel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Pomocna klasa za rad sa properties vrednostima iz variables.properties fajla
 * 
 * @author Marko
 *
 */
public class PropertiesUtil {
	public static boolean SYSTEM_PRICING_FLAG = true;
	
	public static int RNIDS_HOST_EPP_PORT = 700; 
	
	private static Logger log = Logger.getLogger(PropertiesUtil.class);
	private static PropertiesUtil instance;
	private Properties properties;
	private static String sep = System.getProperty("file.separator");
	public static PropertiesUtil getInstance() {
		if (instance == null) {
			instance = new PropertiesUtil();
		}
		return instance;
	}

	/**
	 * Metoda konstruktor koja ucitava potreban fajl.
	 */
	public PropertiesUtil() {
		// Read properties file.

		properties = new Properties();
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("variables.properties"));
			log.info("variables.properties file loaded successfully");
		} catch (Exception e) {
			log.error("variables.properties file not found: ", e);
		}
	}
	
	public Properties getProperties() {
		return properties;
	}

	public int getRowNum() {
		String rowNum = getProperties().getProperty("row.number").trim();
		return Integer.parseInt(rowNum);
	}
	
	public int getListSize() {
		String listSize = getProperties().getProperty("list.size").trim();
		return Integer.parseInt(listSize);
	}
	
	
	/**
	 * 
	 * Ova metoda dohvata relativnu putanju za dokument kompanije.
	 * Na primer: /upload/company/documents
	 */
	public String getCompanyDocumentUrl() {
		String address = getProperties().getProperty("company.document.url").trim();
		return address;
	}
	
	public String getIndividualDocumentUrl() {
		String address = getProperties().getProperty("individual.document.url").trim();
		return address;
	}
	
	public String getAgreementDocumentUrl() {
		String address = getProperties().getProperty("agreement.document.url").trim();
		return address;
	}
	
	public String getPersonPhotoUrl() {
		String address = getProperties().getProperty("person.photo.url").trim();
		return address;
	}
	
	public String getCompanyDocumentFolder() {
		String address = getProperties().getProperty("company.document.folder").trim();
		address = address.replace("#", sep);
		return address;
	}
	
	public String getIndividualDocumentFolder() {
		String address = getProperties().getProperty("individual.document.folder").trim();
		address = address.replace("#", sep);
		return address;
	}
	
	public String getAgreementDocumentFolder() {
		String address = getProperties().getProperty("agreement.document.folder").trim();
		address = address.replace("#", sep);
		return address;
	}
	
	public String getPersonPhotoFolder() {
		String address = getProperties().getProperty("person.photo.folder").trim();
		address = address.replace("#", sep);
		return address;
	}
	
	public String getApplicationUrl(){
		String applicationUrl = getProperties().getProperty("application.url").trim();
		return applicationUrl;
	}
	
	public String getSmtpHost(){
		String smtpHost = getProperties().getProperty("smtp.host").trim();
		return smtpHost;
	}
	
	public String getBeotelSmtpHost(){
		String smtpHost = "mail.beotel.net";
		return smtpHost;
	}
	
	public String getSmtpFrom(){
		String smtpFrom = getProperties().getProperty("smtp.from").trim();
		return smtpFrom;
	}
	
	public String getDaysToExpiration(){
		String daysToExpiration = getProperties().getProperty("days.to.expiration").trim();
		return daysToExpiration;
	}
	
	public String getValidityPeriod(){
		String validityPeriod = getProperties().getProperty("validity.period").trim();
		return validityPeriod;
	}

	public String getAgreementDiaryCreated() {
		String diaryCreated = getProperties().getProperty("agreement.diary.created").trim();
		return diaryCreated;
	}
	
	public String getAgreementDiaryStarted() {
		String diaryStarted = getProperties().getProperty("agreement.diary.started").trim();
		return diaryStarted;
	}
	
	public String getAgreementDiaryStopped() {
		String diaryStopped = getProperties().getProperty("agreement.diary.stopped").trim();
		return diaryStopped;
	}
	
	public String getAgreementDiaryCancelled() {
		String diaryStopped = getProperties().getProperty("agreement.diary.cancelled").trim();
		return diaryStopped;
	}
	
	public String getApacheRootFolder() {
		String diaryStopped = getProperties().getProperty("apache.root.folder").trim();
		return diaryStopped;
	}
	
	public String getApacheURL() {
		String diaryStopped = getProperties().getProperty("apache.url").trim();
		return diaryStopped;
	}

	public String getPhpURL() {
		String phpUrl = getProperties().getProperty("rsreg.service.url").trim();
		return phpUrl;
	}	

	public String getWebOperator() {
		String webOperator = getProperties().getProperty("operator.web").trim();
		return webOperator;
	}
	
	public String getVeratWebOperator() {
		String webOperator = getProperties().getProperty("operator.verat.web").trim();
		return webOperator;
	}
	
	public String getSalesEmail() {
		String email = getProperties().getProperty("sales.email").trim();
		return email;
	}
	
	public String getMarketingEmail() {
		String email = getProperties().getProperty("marketing.email").trim();
		return email;
	}
	
	public String getBusinessSalesEmail() {
		String email = getProperties().getProperty("business.sales.email").trim();
		return email;
	}

	public String getWsServerUrl() {
		String wsServerUrl = getProperties().getProperty("ws.server.url").trim();
		return wsServerUrl;
	}

	public String getInoDomainOvertime() {
		String inoDomainOvertime = getProperties().getProperty("ino.domain.overtime").trim();
		return inoDomainOvertime;
	}

	public String getRsDomainOvertime() {
		String rsDomainOvertime = getProperties().getProperty("rs.domain.overtime").trim();
		return rsDomainOvertime;
	}
	
	public String getUserFormUrl(){
		String userFormUrl = getProperties().getProperty("user.form.url").trim();
		return userFormUrl;
	}
	
	public String getUserFormFolder(){
		String userFormFolder = getProperties().getProperty("user.form.folder").trim();
		userFormFolder = userFormFolder.replace("#", sep);
		return userFormFolder;
	}
	
	public String getTempFilesFolder(){
		String tempFilesFolder = getProperties().getProperty("temp.file.dest").trim();
		return tempFilesFolder;
	}
	
	public String getKoradminIP(){
		String koradminIP = getProperties().getProperty("koradmin.ip").trim();
		return koradminIP;
	}
	
	public String getKoradminAddress(){
		String koradminAddress = getProperties().getProperty("koradmin.url").trim();
		return koradminAddress;
	}

	public int getCancellationDay(){
		String cancellationDay = getProperties().getProperty("cancellation.day").trim();
		return Integer.parseInt(cancellationDay);
	}

	public String getPortaHost(){
		String host = getProperties().getProperty("porta.host").trim();
		return host;
	}
	
	public String getPortaUsername(){
		String username = getProperties().getProperty("porta.username").trim();
		return username;
	}
	
	public String getPortaPassword(){
		String password = getProperties().getProperty("porta.password").trim();
		return password;
	}
	
	public String getVoipReportSubject(){
		String password = getProperties().getProperty("voip.report.subject").trim();
		return password;
	}
	public Integer getDidDisplayNumber(){
		String accountNumber = getProperties().getProperty("did.display.number").trim();
		return Integer.parseInt(accountNumber);
	}
	public Integer getDidPoolSize(){
		String accountNumber = getProperties().getProperty("did.pool.size").trim();
		return Integer.parseInt(accountNumber);
	}
	public String getSipHost(){
		String sipHost = getProperties().getProperty("sip.host").trim();
		return sipHost;
	}
	public String getVoipPortalHost(){
		String sipHost = getProperties().getProperty("voip.portal.host").trim();
		return sipHost;
	}
	public String getPortaWSHost(){
		String portaWSHost = getProperties().getProperty("porta.ws.host").trim();
		return portaWSHost;
	}
	public Integer getPortaListSize(){
		String accountNumber = getProperties().getProperty("porta.list.size").trim();
		return Integer.parseInt(accountNumber);
	}
	public String getProfileName(){
		String profile = getProperties().getProperty("profile.name").trim();
		return profile;
	}
	public boolean isProductionVersion(){
		return "production".equals(getProfileName());
	}
	public Integer getAliasMaxCount(){
		String aliasMaxCount = getProperties().getProperty("alias.max.count").trim();
		return Integer.parseInt(aliasMaxCount);
	}
	public Integer getAccountMaxCount(){
		String accountMaxCount = getProperties().getProperty("account.max.count").trim();
		return Integer.parseInt(accountMaxCount);
	}
	public Integer getMinimumPoolSize(){
		String minimumNumber = getProperties().getProperty("minimum.pool.size").trim();
		return Integer.parseInt(minimumNumber);
	}
	public Integer getResellerMinimumPoolSize(){
		String minimumNumber = getProperties().getProperty("reseller.minimum.pool.size").trim();
		return Integer.parseInt(minimumNumber);
	}
	public String[] getMaskList(){
		String maskListTemp = getProperties().getProperty("mask.list").trim();
		String[] result = maskListTemp.split(",");
		return result;
	}

	public String[] getMoneyList(){
		String moneyListTemp = getProperties().getProperty("sms.voip.money").trim();
		String[] result = moneyListTemp.split(",");
		return result;
	}
	public String[] getSmsRemoteIP(){
		String smsRemoteIP = getProperties().getProperty("sms.remote.ip").trim();
		String[] result = smsRemoteIP.split(";");
		return result;
	}
	
	public String getPaymentsName() {
		String paymentsName = getProperties().getProperty("payments.name").trim();
		return paymentsName;
	}
	
	public String getTemplateCustomer() {
		String customerName = getProperties().getProperty("porta.template.customer").trim();
		return customerName;
	}
	
	public String getPayRecipientName(){
		String payRecipient = getProperties().getProperty("pay.recipient.name").trim();
		return payRecipient;
	}
	
	public String getPayRecipientAddress(){
		String payRecipient = getProperties().getProperty("pay.recipient.address").trim();
		return payRecipient;
	}
	
	public String getPaymentSlipFrom(){
		String paymentSlipFrom = getProperties().getProperty("payment.slip.from").trim();
		return paymentSlipFrom;
	}
	
	public String getPaymentSlipSubject(){
		String paymentSlipSubject = getProperties().getProperty("payment.slip.subject").trim();
		return paymentSlipSubject;
	}

	public String getPayCode(){
		String payCode = getProperties().getProperty("pay.code").trim();
		return payCode;
	}

	public String getPayValute(){
		String payValute = getProperties().getProperty("pay.valute").trim();
		return payValute;
	}

	public String getBeotelAccount(){
		String beotelAccount = getProperties().getProperty("beotel.account").trim();
		return beotelAccount;
	}
	
	public String getBeotelVoipAccount(){
		String beotelAccount = getProperties().getProperty("beotel.voip.account").trim();
		return beotelAccount;
	}
	
	public String getBeotelPib(){
		String beotelPib = getProperties().getProperty("beotel.pib").trim();
		return beotelPib;
	}
	
	public String getBeotelOrCarrierPibSet(){
		String beotelPib = getBeotelPib();
		String carrierPib = getBeotelCarrierPib();
		return "('"+beotelPib+"','"+carrierPib+"')";
	}
	
	public String getBeotelCarrierPib(){
		String carrierPib = getProperties().getProperty("carrier.pib").trim();
		return carrierPib;
	}
	
	public String getModelNo(){		
		String modelNo = getProperties().getProperty("model.no").trim();
		return modelNo;
	}
	
	public String getImgFolder() {
		String tempFileDest = getProperties().getProperty("temp.file.dest").trim();
		String imgFolder = null;
		if(tempFileDest != null && tempFileDest.contains("mysunflower")){
			imgFolder = getProperties().getProperty("portal.img.folder").trim();
		}else{
			imgFolder = getProperties().getProperty("img.folder").trim();
		}
		return imgFolder;
	}
	
	public String getListSeparator() {
		String listSeparator = getProperties().getProperty("list.separator").trim();
		return listSeparator;
	}
	
	public String getElementSeparator() {
		String elementSeparator = getProperties().getProperty("element.separator").trim();
		return elementSeparator;
	}
	
	public String getVoipPaymentSubject(){
		String voipPaymentSubject = getProperties().getProperty("voip.payment.subject").trim();
		return voipPaymentSubject;
	}
	
	public String getVoipIndividualPackageName(){
		String voipIndividualPackageName = getProperties().getProperty("voip.individual.package.name").trim();
		return voipIndividualPackageName;
	}
	
	public String getVoipGratisPackageName(){
		String voipGratisPackageName = getProperties().getProperty("voip.gratis.package.name").trim();
		return voipGratisPackageName;
	}	
	
	
	public String getCustomerCreditLimitsParamName(){
		String customerCreditLimits = getProperties().getProperty("customer.credit.limits").trim();
		return customerCreditLimits;		
	}
	
	public String getCustomerPromotionalCreditParamName(){
		String customerCreditLimits = getProperties().getProperty("customer.promotional.credit").trim();
		return customerCreditLimits;		
	}
	
	public String getCustomerSubscriptionsParamName(){
		String customerSubscriptions = getProperties().getProperty("customer.subscriptions").trim();
		return customerSubscriptions;		
	}
	
	
	public Integer getVoipIndividualBatch(){
		String individualBatch = getProperties().getProperty("voip.individual.batch").trim();
		return Integer.parseInt(individualBatch);
	}
	
	public Integer getVoipCompanyBatch(){
		String companyBatch = getProperties().getProperty("voip.company.batch").trim();
		return Integer.parseInt(companyBatch);
	}
	
	public Integer getNiceNumbersBatch(){
		String niceBatch = getProperties().getProperty("nice.numbers.batch").trim();
		return Integer.parseInt(niceBatch);
	}
	
	public String getSupportEmail() {
		String supportEmail = getProperties().getProperty("support.email").trim();
		return supportEmail;
	}
	
	public Integer getPaymentsNumber(){
		String paymentsNumber = getProperties().getProperty("payments.number").trim();
		return Integer.parseInt(paymentsNumber);		
	}
	
	public String getRefundsName() {
		String refundsName = getProperties().getProperty("refunds.name");
		return refundsName;
	}
	
	public Integer getBeotelDistributerId() {
		String beotelDistributerId = getProperties().getProperty("beotel.distributer.id").trim();
		return Integer.parseInt(beotelDistributerId);	
	}
	
	/**
	 * metoda za dohvatanje sadrzaja falja
	 * @param file
	 * @return
	 */
	public String getContents(File file) {
	    //...checks on file are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(file));
	      try {
	        String line = null; 

	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append("\r\n");	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      log.error("Greska prilikom citanja iz fajla: "+ex);
	    }
	    
	    return contents.toString();
	}

	public Integer getWarnDayInactiveAgreements() {
		String warnDayInactiveAgreements = getProperties().getProperty("warn.day.inactive.agreements").trim();
		return Integer.parseInt(warnDayInactiveAgreements);
	}

	public int getCancellationDayInactiveAgreements() {
		String cancellationDayInactiveAgreements = getProperties().getProperty("cancellation.day.inactive.agreements").trim();
		return Integer.parseInt(cancellationDayInactiveAgreements);
	}

	
	public String getSmsPassword(){
		String smsPassword = getProperties().getProperty("sms.password").trim();
		return smsPassword;
	}
	
	public String getSmsUsername(){
		String smsUsername = getProperties().getProperty("sms.username").trim();
		return smsUsername;
	}
	
	public String getSmsSignature(){
		String smsUsername = getProperties().getProperty("sms.signature").trim();
		return smsUsername;
	}
	
	public List<Integer> getCCDistributerIdList(){
		String distributerList = getProperties().getProperty("cc.distributer.id.list").trim();
		return getAsIntegerList(distributerList);
	}
	
	public List<Integer> getCCSalesPointIdList(){
		String salespointList = getProperties().getProperty("cc.salespoint.id.list").trim();
		return getAsIntegerList(salespointList);
	}
	
	public String getCCRepresentationNumberStart(){
		String representationNumberStart = getProperties().getProperty("cc.representation.number.start").trim();
		return representationNumberStart;
	}
	
	public String getCCCustomerName(){
		String ccCustomerName = getProperties().getProperty("cc.customer.name").trim();
		return ccCustomerName;
	}
	
	public String getCCDistributerReportSubject(){
		String ccDistributerSubject = getProperties().getProperty("cc.distributer.report.subject").trim();
		return ccDistributerSubject;
	}
	
	public String getCCDistributerReportTo(){
		String ccDistributerTo = getProperties().getProperty("cc.distributer.report.to").trim();
		return ccDistributerTo;
	}	
	
	
	public Map<String, String> getCCSeriaBatch(){
		Map<String, String> result = new HashMap<String, String>();
		String[] ccSeriaPairs = getProperties().getProperty("cc.seria.batch").split(",");
		for(String temp: ccSeriaPairs){
			String[] ccSeriaBatchPair = temp.split(":");
			result.put(ccSeriaBatchPair[0], ccSeriaBatchPair[1]);
		}
		return result;
	}
	
	private List<Integer> getAsIntegerList(String input){
		String[] tempResult = input.split(",");
		List<Integer> integerList = new ArrayList<Integer>();
		for(String temp : tempResult){
			int tempInt = Integer.parseInt(temp.trim());
			integerList.add(tempInt);
		}
		return integerList;
	}
	public String getEpaymentVoipServiceName(){
		String epaymentServiceId = getProperties().getProperty("epayment.voip.service.name").trim();
		return epaymentServiceId;
	}

	public String getOnlineOperator() {
		String operatorName = getProperties().getProperty("online.operator").trim();
		return operatorName;
	}

	public String[] getECommerceWSRemoteIP() {
		String remoteIP = getProperties().getProperty("ecommerce.remote.ip").trim();
		String[] result = remoteIP.split(";");
		return result;
	}

	
	public String getOnlinePaymentType() {
		String onlinePaymentType = getProperties().getProperty("online.payment.type").trim();
		return onlinePaymentType;
	}
	
	public List<Integer> getNiceNumbersBatchList(){
		String niceNumbersBatchList = getProperties().getProperty("nice.numbers.batch.list").trim();
		return getAsIntegerList(niceNumbersBatchList);
	}

	public String getReturnMoneyTPSName() {
		String returnMoneyTPSName = getProperties().getProperty("return.money.tps.name").trim();
		return returnMoneyTPSName;
	}
	public int getDistributerPaymentDelay() {
		String distributerPaymentDelay = getProperties().getProperty("distributer.payment.delay").trim();
		return Integer.valueOf(distributerPaymentDelay);
	}

	public String getDistributerWarningMailTemplate() {
		String distributerWarningMailTemplate = getProperties().getProperty("distributer.warning.mail.template").trim();
		return distributerWarningMailTemplate;
	}

	public String getEquipmentNavisionCode() {
		String equipmentNavisionCode = getProperties().getProperty("equipment.navision.code").trim();
		return equipmentNavisionCode;
	}

	public String getEquipmentInvoiceDescription() {
		String equipmentInvoiceDescription = getProperties().getProperty("equipment.invoice.description").trim();
		return equipmentInvoiceDescription;
	}
	
	public String getDNSAutomationScriptUrl() {
		String dnsAutomationScriptUrl = getProperties().getProperty("dns.automation.command.url").trim();
		return dnsAutomationScriptUrl;
	}
	
	public String getDNSAutomationUsername() {
		String dnsAutomationUsername = getProperties().getProperty("dns.automation.username").trim();
		return dnsAutomationUsername;
	}
	
	public String getDNSAutomationPassword() {
		String dnsAutomationPassword = getProperties().getProperty("dns.automation.password").trim();
		return dnsAutomationPassword;
	}

	public String getDNSAutomationSecret() {
		String dnsAutomationSecret = getProperties().getProperty("dns.automation.secret").trim();
		return dnsAutomationSecret;
	}

	public String getDefaultMailServerUrl() {
		String defaultMailserverUrl = getProperties().getProperty("default.mailserver.url").trim();
		return defaultMailserverUrl;
	}
	
	public Map<String, String[]>getMerakServerList(){
		Map<String, String[]> merakServersParams = new HashMap<String, String[]>();
		String merakServerList = getProperties().getProperty("merak.server.list").trim();
		if(!merakServerList.contains("#")){
			return merakServersParams;
		}
		String[] serverList = null;
		if(merakServerList.contains(";")){
			serverList = merakServerList.split(";");
		}else{
			serverList = new String[]{merakServerList};
		}
		for(String serverParams: serverList){
			String[] params = serverParams.split("#");
			if(params!= null && params.length==3){
				merakServersParams.put(params[0], new String[]{params[1], params[2]});
			}
		}
		return merakServersParams;
	}
	
	/**
	 * Metoda za dohvatanje parametara za pristup odredjenom cPanelu
	 * @param ipAddress
	 * @return
	 */
	public Map<String, String> getCPanelCredentials(String ipAddress) {
		String cPanelResellerList = getProperties().getProperty("cpanel.server.list").trim();
		String[] cPanelServerList = cPanelResellerList.split("#");
		Map<String, String> credentials = null;
		for(String cPanelServer: cPanelServerList){
			String[] params = cPanelServer.split(",");
			if(params!= null && params.length==4){
				String cpanelIP = params[0];
				if(ipAddress.equals(cpanelIP)){
					credentials = new HashMap<String, String>();
					credentials.put("username", params[2]);
					credentials.put("password", params[3]);
					return credentials;
				}
			}
		}
		return null;
	}
	
	/**
	 * Metodaz a dohvatanje parametara za resellera
	 * @param ipAddress
	 * @param reseller
	 * @return
	 */
	public Map<String, String>getCPanelResellerCredentials(String ipAddress, String reseller){
		String cPanelResellerList = getProperties().getProperty("cpanel.reseller.list").trim();
		String[] cPanelServerList = cPanelResellerList.split("#");
		Map<String, String> credentials = null;//
		for(String cPanelServer: cPanelServerList){
			String[] params = cPanelServer.split(";");
			if(params!= null && params.length>1){
				String cpanelIP = params[0];
				if(ipAddress.equals(cpanelIP)){
					for(int i=1; 1<=params.length; i++){
						String[] resellerCredentials = params[i].split(",");
						if(resellerCredentials!= null && resellerCredentials.length==2){
							String username = resellerCredentials[0].trim();
							String password =  resellerCredentials[1].trim();
							if(username.equals(reseller)){
								credentials = new HashMap<String, String>();
								credentials.put("username", username);
								credentials.put("password", password);
								return credentials;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	

	public String getDefaultMailServer() {
		String defaultMailserver = getProperties().getProperty("default.mailserver").trim();
		return defaultMailserver;
	}
	
	public Integer getPackageChangeValidityDays() {
		String packageChangeValidityDays = getProperties().getProperty("package.change.validity.days").trim();
		return Integer.parseInt(packageChangeValidityDays);
	}

	public String getPackageChangeAuthMail() {
		String packageChangeAuthMail = getProperties().getProperty("package.change.auth.mail").trim();
		return packageChangeAuthMail;
	}
	
	public String getPeriodBeforeExpirationCondition() {
		String periodBeforeExpirationCondition = getProperties().getProperty("period.before.expiration.condition").trim();
		return periodBeforeExpirationCondition;
	}

	public int getDaysBeforeExpirationGratis() {
		String daysBeforeExpirationGratis = getProperties().getProperty("days.before.expiration.gratis").trim();
		return Integer.parseInt(daysBeforeExpirationGratis);
	}
	
	public String getBankAccountPaymentType(){
		String bankAccountPaymentType = getProperties().getProperty("bank.account.payment.type").trim();
		return bankAccountPaymentType;		
	}

	public int getUnderPaymentTolerance() {
		String underPaymentTolerance = getProperties().getProperty("under.payment.tolerance").trim();
		return Integer.parseInt(underPaymentTolerance);
	}

	public int getOverPaymentTolerance() {
		String overPaymentTolerance = getProperties().getProperty("over.payment.tolerance").trim();
		return Integer.parseInt(overPaymentTolerance);
	}
	
	public int getUnderPaymentAcceptableTolerance() {
		String underPaymentTolerance = getProperties().getProperty("under.payment.acceptable.tolerance").trim();
		return Integer.parseInt(underPaymentTolerance);
	}

	public int getBusinessUnderPaymentAcceptableTolerance() {
		String underPaymentTolerance = getProperties().getProperty("business.under.payment.acceptable.tolerance").trim();
		return Integer.parseInt(underPaymentTolerance);
	}
	
	public int getOverPaymentAcceptableTolerance() {
		String overPaymentTolerance = getProperties().getProperty("over.payment.acceptable.tolerance").trim();
		return Integer.parseInt(overPaymentTolerance);
	}	

	public int getBusinessOverPaymentAcceptableTolerance() {
		String overPaymentTolerance = getProperties().getProperty("business.over.payment.acceptable.tolerance").trim();
		return Integer.parseInt(overPaymentTolerance);
	}	
	
	public int getAdslXDays() {
		String xDays = getProperties().getProperty("adsl.x.days").trim();
		return Integer.parseInt(xDays);
	}
	
	public int getAdslRealizeGraceDays(){
		String graceDays = getProperties().getProperty("adsl.realize.grace.days").trim();
		return Integer.parseInt(graceDays);		
	}
	
	public int getWifiRealizeGraceDays(){
		String graceDays = getProperties().getProperty("wifi.realize.grace.days").trim();
		return Integer.parseInt(graceDays);		
	}

	public String getLogsMailTemplate() {
		String logsMailTemplate = getProperties().getProperty("logs.mail.template").trim();
		return logsMailTemplate;	
	}
	public String getActivationReportEmail(){
		String actEmail = getProperties().getProperty("activation.report.email").trim();
		return actEmail;
	}
	public String getWifiActivationReportEmail(){
		String actEmail = getProperties().getProperty("wifi.activation.report.email").trim();
		return actEmail;
	}
	
	public String getIPTVFilePath() {
		String iPTVPathFolder = getProperties().getProperty("iptv.folder").trim();
		iPTVPathFolder = iPTVPathFolder.replace("#", sep);
		return iPTVPathFolder;
	}
	
	public String getVipFilePath() {
		String vipPathFolder = getProperties().getProperty("vip.folder").trim();
		vipPathFolder = vipPathFolder.replace("#", sep);
		return vipPathFolder;
	}
	
	public String getPlannedActionOrderFilePath() {
		String vipPathFolder = getProperties().getProperty("actions.folder").trim();
		vipPathFolder = vipPathFolder.replace("#", sep);
		return vipPathFolder;
	}
	
	public String getHRFilePath() {
		String hrPathFolder = getProperties().getProperty("hr.folder").trim();
		hrPathFolder = hrPathFolder.replace("#", sep);
		return hrPathFolder;
	}
	public String getSystemFileSeparator() {
		return sep;
	}

	public String getPortalApplicationUrl() {
		return getProperties().getProperty("portal.application.url").trim();
	}

	public String getSoftwareInternal() {
		return getProperties().getProperty("software.internal").trim();
	}

	public int getDistributerPaymentTolerance() {
		return Integer.parseInt(getProperties().getProperty("distributer.payment.tolerance").trim());
	}

	public String getPortalImgFolder() {
		String imgFolder = getProperties().getProperty("portal.img.folder").trim();
		return imgFolder;
	}

	public String[] getInvoiceMailFromList() {
		String mailListFormTemp = getProperties().getProperty("invoice.mail.from.list").trim();
		String[] result = mailListFormTemp.split(",");
		return result;
	}

	public String getBeotelExpressAccount(){
		return getProperties().getProperty("beotel.express.account").trim();
	}

	public int getHRDaysBackward() {
		return Integer.parseInt(getProperties().getProperty("hr.days.backward").trim());
	}
	
	public int getHRAdminDaysBackward() {
		return Integer.parseInt(getProperties().getProperty("hr.admin.days.backward").trim());
	}

	public int getAdslRealizeGraceDaysCompany() {
		return Integer.parseInt(getProperties().getProperty(
				"adsl.realize.grace.days.company").trim());
	}

	public int getWifiRealizeGraceDaysCompany() {
		return Integer.parseInt(getProperties().getProperty(
				"wifi.realize.grace.days.company").trim());
	}

	public int getReservationValidityDays() {
		return Integer.parseInt(getProperties().getProperty("reservation.validity.days").trim());
	}

	public int getBusinessUnderPaymentTolerance() {
		String underPaymentTolerance = getProperties().getProperty("business.under.payment.tolerance").trim();
		return Integer.parseInt(underPaymentTolerance);
	}

	public int getBusinessOverPaymentTolerance() {
		String overPaymentTolerance = getProperties().getProperty("business.over.payment.tolerance").trim();
		return Integer.parseInt(overPaymentTolerance);
	}

	public int getVacationConversionDays() {
		String vacationConversionDays = getProperties().getProperty("vacation.conversion.days").trim();
		return Integer.parseInt(vacationConversionDays);
	}
	
	public int getMailAccountsPerAgreement() {
		String mailAccountsPerAgreement = getProperties().getProperty("mail.accounts.per.agreement").trim();
		return Integer.parseInt(mailAccountsPerAgreement);
	}

	public String getHolidayExchangeTreshold() {
		return getProperties().getProperty("holiday.exchange.treshold").trim();
	}

	public int getVoipPrepaidProductId() {
		String voipPrepaidProductIdString = getProperties().getProperty("voip.prepaid.product.id").trim();
		return Integer.parseInt(voipPrepaidProductIdString);
	}
	
	/**
	 * Metoda koja dohvata url aplikacije na kojoj se nalazimo ()sunflower/mysunflower
	 * @return
	 */
	public String getThisApplicationUrl() {
		String tempFileDest = getProperties().getProperty("temp.file.dest").trim();
		String thisApplicationUrl = null;
		if(tempFileDest != null && tempFileDest.contains("mysunflower")){
			thisApplicationUrl = getPortalApplicationUrl();
		}else{
			thisApplicationUrl = getApplicationUrl();
		}
		return thisApplicationUrl;
	}

	public int getDefaultVendorId() {
		String defaultVendorIdString = getProperties().getProperty("default.vendor.id").trim();
		return Integer.parseInt(defaultVendorIdString);
	}

	public String getDistributerStopNotificationTo() {
		return getProperties().getProperty("distributer.stop.notification.to").trim();
	}
	
	public int getHourOvertimeValue() {
		String overtimePrice = getProperties().getProperty("hour.overtime.value").trim();
		return Integer.parseInt(overtimePrice);
	}
	
	public String getPremiumSmtpHost() {
		return getProperties().getProperty("premium.smtp.host").trim();
	}
	
	public String getPremiumSmtpUsername() {
		return getProperties().getProperty("premium.smtp.username").trim();
	}	
	
	public String getPremiumSmtpPassword() {
		return getProperties().getProperty("premium.smtp.password").trim();
	}

	public String getPetsPortalUrl() {
		return getProperties().getProperty("pets.portal.url").trim();
	}	
	
	public String[] getQiwiRemoteIP(){
		String qiwiRemoteIP = getProperties().getProperty("qiwi.remote.ip").trim();
		String[] result = qiwiRemoteIP.split(";");
		return result;
	}

	public int getBusinessItemUnderPaymentTolerance() {
		String underPaymentTolerance = getProperties().getProperty("business.item.underpayment.tolerance").trim();
		return Integer.parseInt(underPaymentTolerance);
	}

	public int getBusinessItemOverPaymentTolerance() {
		String overPaymentTolerance = getProperties().getProperty("business.item.overpayment.tolerance").trim();
		return Integer.parseInt(overPaymentTolerance);
	}
	
	public String getRegApplicationInvoiceSubject() {
		String result = getProperties().getProperty("reg.application.invoice.subject");
		return result;
	}
	
	public int getMIKValidityDays() {
		String mikValidity = getProperties().getProperty("mik.validity.days").trim();
		return Integer.parseInt(mikValidity);
	}

	/**
	 * Metod za dohvatanje liste IP Adresa svih cpanela
	 * @return
	 */
	public List<String> getCPanelServerList() {
		String cPanelServerList = getProperties().getProperty("cpanel.server.list").trim();
		String[] cPanelServerArray= cPanelServerList.split("#");
		List<String> result = new ArrayList<String>(); 
		for(String cPanelServer: cPanelServerArray){
			String[] params = cPanelServer.split(",");
			if(params!= null && params.length>1){
				String cpanelIP = params[0];
				result.add(cpanelIP);
			}
		}
		return result;
	}	
	
	/**
	 * Metoda za dohvatanje simbolickog imena cPanela
	 * @param ipAddress
	 * @return
	 */
	public String getCPanelName(String ipAddress) {
		String cPanelResellerList = getProperties().getProperty("cpanel.server.list").trim();
		String[] cPanelServerList = cPanelResellerList.split("#");
		for(String cPanelServer: cPanelServerList){
			String[] params = cPanelServer.split(",");
			if(params!= null && params.length==4){
				String cpanelIP = params[0];
				if(ipAddress.equals(cpanelIP)){
					return params[1];
				}
			}
		}
		return null;
	}

	public String[] getRemoteIPECommerceWS() {
		String result = getProperties().getProperty("remote.ip.ecommerce");
		return result.split(";");
	}

	public String getMailValidityPeriod() {
		String result = getProperties().getProperty("mail.validity.period").trim();
		return result;
	}

	public String getNotificationBeforeDeadline(){
		String result = getProperties().getProperty("notification.before.deadline").trim();
		return result;		
	}
	
	public String getPaymentSlipSubjectPrefix() {
		String result = getProperties().getProperty("payment.slip.subject.prefix");
		return result;
	}
	
	public String getPaymentSlipRecipientPlace() {
		String result = getProperties().getProperty("payment.slip.recipient.place").trim();
		return result;
	}
	
	public Integer getPaymentSlipDueDays() {
		String result = getProperties().getProperty("payment.slip.due.days").trim();
		return Integer.parseInt(result);
	}
	
	public Integer getKamDailyScheduleLimit() {
		String result = getProperties().getProperty("kam.daily.schedule.limit").trim();
		return Integer.parseInt(result);
	}

	public String getCloudInterfaceUrl() {
		String result = getProperties().getProperty("cloud.url").trim();
		return result;
	}
	
	public String getOnAppSoftwareBasicAuthenticationCredentials() {
		String result = getProperties().getProperty("cloud.credentials").trim();
		return result;
	}
	
	public String getInvoicingRestNavisionResourceCode() {
		String restCode = getProperties().getProperty("invoicing.rest.navision.resource.code");
		return restCode;
	}
	
	public String getPortaGenericCustomer() {
		String genericCustomer = getProperties().getProperty("porta.generic.customer");
		return genericCustomer;
	}

	public String getIptvRestUrl() {
		String url = getProperties().getProperty("iptv.rest.url");
		return url;
	}
	
	public String getIptvRestCredentials() {
		String credentials = getProperties().getProperty("iptv.rest.credentials");
		return credentials;
	}
	
	public int getKviskoYearCount() {
		String result = getProperties().getProperty("kvisko.year.count").trim();
		return Integer.parseInt(result);
	}

	public String[] getMadnetRemoteIP() {
		String madnetRemoteIP = getProperties().getProperty("madnet.remote.ip").trim();
		String[] result = madnetRemoteIP.split(";");
		return result;
	}
	
	// props za novi rnids epp registraciju
	public String getRnidsHostAddress() {
		String address = getProperties().getProperty("rnids.host.address").trim();
		return address;
	}
	
	public int getRnidsHostPort() {
//		String portAsString = getProperties().getProperty("rnids.host.port").trim();
//		Integer port = TextUtil.getInstance().parseInt(portAsString);
//		return port;
		return RNIDS_HOST_EPP_PORT;
	}
	
	public String getRnidsUsername() {
		String username = getProperties().getProperty("rnids.username").trim();
		return username;
	}
	
	/**
	 * Da li je omoguceno korizcenjen cenovnika?
	 * 
	 * @return
	 */
	public boolean getSystemPricingFlag() {
		// TODO staviti u varbs...
		return SYSTEM_PRICING_FLAG;
	}
	
	public String getRnidsPassword() {
		String password = getProperties().getProperty("rnids.password").trim();
		return password;
	}

	public String getRnidsRuntimeHome() {
		String rnidsRuntimeHome = getProperties().getProperty("rnids.runtime.home").trim();
		return rnidsRuntimeHome;
	}
}
