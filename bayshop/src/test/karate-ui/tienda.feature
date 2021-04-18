Feature: login, acceder a un producto mediante un click y comprar

Background:
    * configure driver = { type: 'chrome', showDriverLog: true }

Scenario: login and buy a product

    Given driver 'http://localhost:8080/login'
    * input('#username', 'b')
    * input('#password', 'aa')
    * submit().click("button[type=submit]")
    # se comprueba que la pagina redirigida sea la correcta
    * match html('title') contains 'BayShop | Todos los productos'
    * driver.screenshot()

    # click en un producto para ir a su vista detalle
    * click("a.bayshop-product")
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