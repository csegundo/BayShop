Feature: browser login

Background:
  	* configure driver = { type: 'chrome', showDriverLog: true, executable: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe' }
  
Scenario: login en nuestra app

	Given driver 'http://localhost:8080/login'
	* input('#username', 'b')
  	* input('#password', 'aa')
  	* submit().click("button[type=submit]")
	# se comprueba que la pagina redirigida sea la correcta
  	* match html('title') contains 'BayShop | Todos los productos'
  	* driver.screenshot()