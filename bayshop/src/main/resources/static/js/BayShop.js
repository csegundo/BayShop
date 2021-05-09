/**
 * Este archivo contiene las funcionalidades de las páginas de BayShop
 */

$(function(){
    // FUNCION QUE RECIBE UN WRAPPER Y DEVUELVE TODOS LOS DATOS DE UN FORMULARIO
    function get_bayshop_form_data(wrapper){
        var _data = {};
        $.each(wrapper.find('.form-control'), function(i, item){
            _data[$(item).data().key] = $(item).val(); // type text, number, email, password
        });

        return _data;
    }

    
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


    // Ver mensaje en el popup
    $('body.messages .view-message').on('click', function(){
        var _id = $(this).parents('tr').data().id;
        BayShopAPI.template('mensajes/ver', { "id" : _id }, function(response){
            var popup = CPOPUP.create('Mensaje');
            popup.html(response);

            popup.find('.bt-close').click(function(){
                CPOPUP.close(popup);
            });
        }, function(error){console.error('POPUP error', error);});
    });


    // Filtrar mensajes
    $('body.messages select[name="type"]').on('change', function(){
        var parent = $(this).parents('.messages'),
            type = $(this).find('option:selected').val();
        switch(type){
            case "inbox":
                parent.find('.msg[data-type=inbox]').show();
                parent.find('.msg[data-type!=inbox]').hide();
                break;
            case "send":
                parent.find('.msg[data-type=send]').show();
                parent.find('.msg[data-type!=send]').hide();
                break;
            case "all":
            default: parent.find('.msg').show(); break;
        }
    });

    // Borrar mensajes recibidos
    $('body.messages .delete-message').on('click', function(){
        if(confirm('¿Seguro que deseas borrar el mensaje?')){
            var _row = $(this).parents('tr'), _id = _row.data().id;
            BayShopAPI.delete("mensajes/api/delete/" + _id, { 'id' : _id }, function(response){
                _row.remove();
                console.debug('DELETE MESSAGE success', response);
            }, function(error){console.error('DELETE MESSAGE error', error);});
        }
    });

    // Acciones sobre usuarios y productos en la vista de admin
    $('body.admin table.all-users .bt-removeUser').click(function(){
        var _id = $(this).parent().data().id, _row = $(this).parents('tr');

        if(confirm(`¿Seguro que quieres borrar el usuario con ID: ${_id}?`)){
            BayShopAPI.delete("admin/api/deleteAccount", { "id" : _id }, function(response){
                console.debug('response ajax', response);
                _row.remove();
            }, function(error){
                console.error(error);
            });
        }
    });
    $('body.admin table.all-users').on('click', '.bt-disableUser, .bt-enableUser', function(){
        var _id = $(this).parent().data().id, _row = $(this).parents('tr'), _enable = $(this).data().enable == 1;

        BayShopAPI.post("admin/api/toggleUserStatus", { "enable" : _enable, "id" : _id }, function(response){
            console.debug('response ajax', response);
            _row.find('td.status').html(_enable ? 'Si' : 'No');
        }, function(error){
            console.error(error);
        });
    });

    $('body.admin table.all-products .bt-deleteProduct').click(function(){
        var _id = $(this).parent().data().id, _row = $(this).parents('tr');

        if(confirm(`¿Seguro que quieres borrar el producto con ID: ${_id}?`)){
            BayShopAPI.delete("admin/api/deleteProduct", { "id" : _id }, function(response){
                console.debug('response ajax', response);
                _row.remove();
            }, function(error){
                console.error(error);
            });
        }
    });
    $('body.admin table.all-products').on('click', '.bt-disableProduct, .bt-enableProduct', function(){
        var _id = $(this).parent().data().id, _row = $(this).parents('tr'), _enable = $(this).data().enable == 1;

        BayShopAPI.post("admin/api/toggleProductStatus", { "enable" : _enable, "id" : _id }, function(response){
            console.debug('response ajax', response);
            _row.find('td.status').html(_enable ? 'Si' : 'No');
        }, function(error){
            console.error(error);
        });
    });


    // Editar perfil template
    $('header nav a[data-action="edit"]').click(function(){
        BayShopAPI.template('user/edit', {}, function(response){
            var popup = CPOPUP.create('Editar perfil');
            popup.html(response);

            popup.find('.bt-close').click(function(){
                CPOPUP.close(popup);
            });

            // delete account
            popup.find('.element.delete').on('click', '.btn-removeAccount', function(){
                if(confirm('Última oportunidad. ¿Seguro que quieres borrar la cuenta?')){
                    BayShopAPI.post("user/api/deleteAccount", {}, function(response){
                        console.debug('RESPONSE DELETE ACCOUNT', response);
                    }, function(error){console.error('ERROR DELETE ACCOUNT', error);});
                }
            });

            // change username
            popup.find('.element.username').on('click', '.bt-changeUsername', function(){
                var wp = $(this).parents('.element.username'), data = get_bayshop_form_data(wp);
                BayShopAPI.post("user/api/changeUsername", data, function(response){
                    wp.find('.error,.success').hide();
                    wp.find(`.${response.success ? 'success' : 'error'}`).html(response.message).show();
                    console.debug('RESPONSE CHANGE USERNAME', response);
                }, function(error){
                    popup.find('.element.username .error').html(error).show();
                });
            });

            // change password
            popup.find('.element.password').on('click', '.bt-changePassword', function(){
                var wp = $(this).parents('.element.password'), data = get_bayshop_form_data(wp);
                BayShopAPI.post("user/api/changePassword", data, function(response){
                    wp.find('.error,.success').hide();
                    wp.find(`.${response.success ? 'success' : 'error'}`).html(response.message).show();
                    console.debug('RESPONSE CHANGE PASSWORD', response);
                }, function(error){
                    popup.find('.element.password .error').html(error).show();
                });
            });
        }, function(error){console.error('POPUP error', error);});
    });
});