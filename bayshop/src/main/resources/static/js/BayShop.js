/**
 * Este archivo contiene las funcionalidades de las páginas de BayShop
 */

$(function(){
    // LOGIN Y REGISTER
    $('.main-div-auth form .change').click(function(){
        var action  = $(this).data().action,
            elem    = '<i class="fa fa-eye"></i>',
            type    = 'text',
            ipt     = $(this).parents('.form-group').find('input');

        switch(action){
            case 'hide':
                elem = '<i class="fa fa-eye"></i>';
                type = 'password';
                break;
            case 'show':
                elem = '<i class="fa fa-eye-slash"></i>';
                type = 'text';
            break;
        }

        action = action == 'hide' ? 'show' : 'hide';

        $(this).data('action', action);
        $(this).attr('data-action', action);
        ipt.attr('type', type);
        $(this).html(elem);
    });


    // COMPRAS - RESUMEN DE COMPRA
    // El total es solo a modo info para el usuario, se le descuenta en el lado del servidor
    $('input[data-key="baypoints_selector"]').change(function(){
        var _final = $('input[data-key="baypoints"]').val() - $(this).val();
        $('.final .baypoints').html(_final);

        $('.discount .baypoints_').html($(this).val());

        var product_price = parseDouble($('.info .product .product-price').html()),
            discount_price = parseDouble($('.dicount .dicount-price').html());
        $('.total .total_price').html(product_price - discount_price);
    });
});