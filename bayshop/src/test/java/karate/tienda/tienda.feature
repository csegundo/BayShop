Feature: login, acceder a un producto mediante un click y comprar

Background:
    * configure driver = { type: 'chrome', showDriverLog: true }
    #* def productID = 3

Scenario: login and buy a product

    Given driver 'http://localhost:8080/login'
    * input('#username', 'b')
    * input('#password', 'aa')
    * submit().click("button[type=submit]")
    * match html('title') contains 'BayShop | Todos los productos' # se comprueba que la pagina redirigida sea la correcta
    * driver.screenshot()

    # click en un producto para ir a su vista detalle (producto 3)
    #* click("a[href=/producto/3]")
    #* click("a[href=/producto/" + productID + "]")
    #* match html('title') contains 'Producto 3'
    #* match html('title') contains 'Producto ' + productID

    # click al boton de comprar producto
    #* click("a.buy-product")
    #* match html('title') contains 'BayShop | Resumen de compra'

    # dentro de la pagina de resumen de compra se le da a comprar
    #* submit().click("button[type=submit]")

