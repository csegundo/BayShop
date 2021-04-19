Feature: login, acceder a un producto mediante un click y comprar

Background:
    * configure driver = { type: 'chrome', showDriverLog: true, executable: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe' }

Scenario: login and buy a product

    # import del login.feature
    call read('login.feature')

    # click en un producto para ir a su vista detalle
    * click("a[data-type=product]")
    * match html('title') contains 'BayShop | Producto '
    * driver.screenshot()

    # click al boton de comprar producto
    * click("a.buy-product")
    * match html('title') contains 'BayShop | Resumen de compra'
    * driver.screenshot()

    # dentro de la pagina de resumen de compra se le da a comprar
    * submit().click("button[type=submit]")
    * driver.screenshot()
    * delay(1500)