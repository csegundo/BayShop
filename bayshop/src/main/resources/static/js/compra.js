$(function(){
    // El total es solo a modo info para el usuario, se le descuenta en el lado del servidor
    let max     = $('input[data-key="baypoints"]').val(),
        final   = $('.final .baypoints');
    
    $('input[data-key="baypoints_selector"]').change(function(){
        var _final = max - $(this).val();
        final.html(_final);

        $('.discount .baypoints_').html($(this).val());

        var product_price = parseDouble($('.info .product .product-price').html()),
            discount_price = parseDouble($('.dicount .dicount-price').html());
        $('.total .total_price').html(product_price - discount_price);
    });
});