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

        var product_price = parseFloat($('.info .product .product-price').html()),
            discount_price = parseFloat($('.discount .discount-price').html());
        
        $('.total .total_price').html(product_price - discount_price);

        var spend = $(this).val();
        // Por cada 3 BayPoints es 1€ de descuento ==> CAMBIAR
        $('.discount .discount-price').html(spend);
    });


    // Acciones sobre usuarios y productos en la vista de admin
    $('body.admin table.all-users .bt-removeUser').click(function(){
        var _id = $(this).parent().data().id;

        if(confirm(`Seguro que quieres borrar el usuario con ID: ${_id}`)){
            BayShopAPI.post("admin/deleteAccount/" + _id, { "idparam" : _id }, function(response){
                console.debug('response ajax', response);
                $(this).parents('tr').remove();
            }, function(error){
                console.error(error);
            });
        }
    });
});