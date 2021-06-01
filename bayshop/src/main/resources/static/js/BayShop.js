/**
 * Este archivo contiene las funcionalidades de las páginas de BayShop
 */

$(function() {
    // FUNCION QUE RECIBE UN WRAPPER Y DEVUELVE TODOS LOS DATOS DE UN FORMULARIO
    function get_bayshop_form_data(wrapper) {
        var _data = {};
        $.each(wrapper.find('.form-control'), function(i, item) {
            _data[$(item).data().key] = $(item).val(); // type text, number, email, password
        });

        return _data;
    }

    // FUNCION QUE SE LLAMA DESDE LOS WS PARA ACTUALIZAR LA TABLA
    let _ws_notifyMessage = function(messageObj) {
        var _table = $('body.messages table.all-user-messages tbody');

        if (_table.length > 0) {
            _table.prepend(`
            <tr class="msg" data-id="${messageObj.id}" data-type="inbox">
                <td>${messageObj.sender}</td>
                <td>${messageObj.receiver}</td>
                <td>${messageObj.date}</td>
                <td>${messageObj.msg}</td>
                <td>
                    <div class="btn btn-light view-message"><i class="fa fa-eye"></i></div>
                    <div class="btn btn-light delete-message"><i class="fa fa-trash"></i></div>
                </td>
            </tr>
            `);
        }
    };

    // SEND MESSAGE
    $('body.create-message .bt-send').click(function() {
        var dest = $('body.create-message select#dest option:selected').val(),
            msg = $('body.create-message textarea#msg').val();
        BayShopAPI.post("mensajes/api/new", { "dest": dest, "msg": msg }, function(response) {
            console.debug('MENSAJE ENVIADO', response);
            window.location.href = "/mensajes";
        }, function(error) {
            console.error('ERROR INTERNO AL ENVIAR MENSAJE');
        });
    });

    // SEND MESSAGE FROM PRODCUT
    $('body.product .bt-send').click(function() {
        var msg = $('body.product textarea#msg').val();
        var dest = $(this).parent().parent().data().id;
        BayShopAPI.post("mensajes/api/new", { "dest": dest, "msg": msg }, function(response) {
            console.debug('MENSAJE ENVIADO', response);
            var alert = $(`<div class="slide-alert success">El mensaje se envío correctamente</div>`);
            $('body').append(alert);
            $(alert).animate({ opacity: '0' }, 6000, null, function() { $(alert).remove(); });
            $('body.product textarea#msg').val("");
        }, function(error) {
            console.error('ERROR INTERNO AL ENVIAR MENSAJE');
        });
    });


    // LOGIN Y REGISTER
    $('.main-div-auth form .change').click(function() {
        var action = $(this).data().action,
            elem = '<i class="fa fa-eye"></i>',
            type = 'text',
            ipt = $(this).parents('.form-group').find('input');

        switch (action) {
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
    $('input[data-key="baypoints_selector"]').change(function() {
        $('input[data-type="hidden_points"]').val($(this).val());

        var _final = $('input[data-key="baypoints"]').val() - $(this).val();
        $('.final .baypoints').html(_final);

        $('.discount .baypoints_').html($(this).val());

        var product_price = parseFloat($('.info .product .product-price').html()),
            discount_price = parseFloat($('.discount .discount-price').html()) / 100.00;

        $('.total .total_price').html(product_price - discount_price);

        var spend = $(this).val();
        // Por cada 3 BayPoints es 1€ de descuento ==> CAMBIAR
        $('.discount .discount-price').html(spend);
    });


    // Ver mensaje en el popup
    $('body.messages table.all-user-messages tbody').on('click', '.view-message', function() {
        var _btn = $(this),
            _id = _btn.parents('tr').data().id,
            _inbox = _btn.parents('tr').data().type == 'inbox';

        BayShopAPI.template('mensajes/ver', { "id": _id }, function(response) {
            var popup = CPOPUP.create('Mensaje');
            popup.html(response);

            popup.find('.bt-close').click(function() {
                CPOPUP.close(popup);
            });
            if (_inbox) {
                if (_btn.parents('tr').find('.status').html() == 'No') {
                    __unread--;
                    __unread = __unread < 0 ? 0 : __unread;
                    $('header nav .msg-user-unread').html('(' + __unread + ')');
                }
                _btn.parents('tr').find('.status').html('Si');
            }
        }, function(error) { console.error('POPUP error', error); });
    });


    // Filtrar mensajes
    $('body.messages select[name="type"]').on('change', function() {
        var parent = $(this).parents('.messages'),
            type = $(this).find('option:selected').val();
        switch (type) {
            case "inbox":
                parent.find('.msg[data-type=inbox]').show();
                parent.find('.msg[data-type!=inbox]').hide();
                break;
            case "send":
                parent.find('.msg[data-type=send]').show();
                parent.find('.msg[data-type!=send]').hide();
                break;
            case "all":
            default:
                parent.find('.msg').show();
                break;
        }
    });

    // Borrar mensajes recibidos
    $('body.messages .delete-message').on('click', function() {
        if (confirm('¿Seguro que deseas borrar el mensaje?')) {
            var _row = $(this).parents('tr'),
                _id = _row.data().id;
            BayShopAPI.delete("mensajes/api/delete/" + _id, { 'id': _id }, function(response) {
                _row.remove();
                console.debug('DELETE MESSAGE success', response);
            }, function(error) { console.error('DELETE MESSAGE error', error); });
        }
    });

    // Acciones sobre usuarios y productos en la vista de admin
    $('body.admin table.all-users .bt-removeUser').click(function() {
        var _id = $(this).parent().data().id,
            _row = $(this).parents('tr');

        if (confirm(`¿Seguro que quieres borrar el usuario con ID: ${_id}?`)) {
            BayShopAPI.delete("admin/api/deleteAccount", { "id": _id }, function(response) {
                console.debug('response ajax', response);
                _row.remove();
            }, function(error) {
                console.error(error);
            });
        }
    });
    $('body.admin table.all-users .bt-editUser').click(function() {
        var _id = $(this).parent().data().id;
        BayShopAPI.template('user/edit/' + _id, {}, function(response) {
            var popup = CPOPUP.create('Editar perfil');

            popup.html(response);
            popup.find('.bt-close').click(function() {
                CPOPUP.close(popup);
            });

            popup.find('.bt-updateUsername').click(function() {
                var _id = $(this).data().id,
                    _username = $(this).parents('.set-username-user').find('input').val();
                BayShopAPI.post("user/userNameChange/" + _id, { "username": _username }, function(response) {
                    console.debug('RESPONSE UPDATE USER USERNAME', response);
                    CPOPUP.close(popup);
                });
            });
            popup.find('.bt-saveRoles').click(function() {
                var _roles = [];
                $.each(popup.find('.set-user-roles .role'), function(i, item) {
                    if ($(item).is(':checked')) {
                        _roles.push($(item).val());
                    }
                });

                BayShopAPI.post("admin/api/updateRoles", { "roles": _roles.join(','), "user": _id }, function(response) {
                    console.debug('RESPONSE UPDATE USER ROLES', response);
                    CPOPUP.close(popup);
                });
            });
        });
    });
    $('body.admin table.all-users').on('click', '.bt-disableUser, .bt-enableUser', function() {
        var _id = $(this).parent().data().id,
            _row = $(this).parents('tr'),
            _enable = $(this).data().enable == 1;

        BayShopAPI.post("admin/api/toggleUserStatus", { "enable": _enable, "id": _id }, function(response) {
            console.debug('response ajax', response);
            _row.find('td.status').html(_enable ? 'Si' : 'No');
        }, function(error) {
            console.error(error);
        });
    });

    $('body.admin table.all-products .bt-deleteProduct').click(function() {
        var _id = $(this).parent().data().id,
            _row = $(this).parents('tr');

        if (confirm(`¿Seguro que quieres borrar el producto con ID: ${_id}?`)) {
            BayShopAPI.delete("admin/api/deleteProduct", { "id": _id }, function(response) {
                console.debug('response ajax', response);
                _row.remove();
            }, function(error) {
                console.error(error);
            });
        }
    });
    $('body.admin table.all-products').on('click', '.bt-disableProduct, .bt-enableProduct', function() {
        var _id = $(this).parent().data().id,
            _row = $(this).parents('tr'),
            _enable = $(this).data().enable == 1;

        BayShopAPI.post("admin/api/toggleProductStatus", { "enable": _enable, "id": _id }, function(response) {
            console.debug('response ajax', response);
            _row.find('td.status').html(_enable ? 'Si' : 'No');
        }, function(error) {
            console.error(error);
        });
    });


    // Editar perfil template
    $('header nav a[data-action="edit"]').click(function() {
        BayShopAPI.template('user/edit', {}, function(response) {
            var popup = CPOPUP.create('Editar perfil');
            popup.html(response);

            popup.find('.bt-close').click(function() {
                CPOPUP.close(popup);
            });

            // change profile image
            popup.find('.element.image').on('submit', 'form.upload-user-image', function(event) {
                event.preventDefault();
                event.stopPropagation();
                var formData = new FormData(this);

                $.ajax({
                    type: 'POST',
                    url: $(this).attr('action'),
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function(data) {
                        data = JSON.parse(data);
                        popup.find('.element .success,.element .error').hide();
                        popup.find('.element.image input[type=file]').val('');
                        popup.find('.element.image .success').html(data.message).show();
                        console.log("success");
                        console.log(data);
                    },
                    error: function(data) {
                        data = JSON.parse(data);
                        popup.find('.element .success,.element .error').hide();
                        popup.find('.element.image input[type=file]').val('');
                        popup.find('.element.image .error').html(data.message).show();
                        console.log("error");
                        console.log(data);
                    }
                });
            });

            // delete account
            popup.find('.element.delete').on('click', '.btn-removeAccount', function() {
                if (confirm('Última oportunidad. ¿Seguro que quieres borrar la cuenta?')) {
                    BayShopAPI.post("user/api/deleteAccount", {}, function(response) {
                        console.debug('RESPONSE DELETE ACCOUNT', response);
                    }, function(error) { console.error('ERROR DELETE ACCOUNT', error); });
                }
            });

            // change username
            popup.find('.element.username').on('click', '.bt-changeUsername', function() {
                var wp = $(this).parents('.element.username'),
                    data = get_bayshop_form_data(wp);
                BayShopAPI.post("user/api/changeUsername", data, function(response) {
                    wp.find('.error,.success').hide();
                    wp.find(`.${response.success ? 'success' : 'error'}`).html(response.message).show();
                    console.debug('RESPONSE CHANGE USERNAME', response);
                }, function(error) {
                    popup.find('.element.username .error').html(error).show();
                });
            });

            // change password
            popup.find('.element.password').on('click', '.bt-changePassword', function() {
                var wp = $(this).parents('.element.password'),
                    data = get_bayshop_form_data(wp);
                BayShopAPI.post("user/api/changePassword", data, function(response) {
                    wp.find('.error,.success').hide();
                    wp.find(`.${response.success ? 'success' : 'error'}`).html(response.message).show();
                    console.debug('RESPONSE CHANGE PASSWORD', response);
                }, function(error) {
                    popup.find('.element.password .error').html(error).show();
                });
            });
        }, function(error) { console.error('POPUP error', error); });
    });

    // Validate Product para Index.html y producto.html
    $('body .bt-validateProduct, body .bt-rejectProduct').click(function() {
        var _id = $(this).parent().data().id,
            _enable = $(this).data().enable == 1;
        var _element = $(this),
            _page = $(this).data().page;
        BayShopAPI.post("revisor/api/toggleProductValidation", { "enable": _enable, "id": _id }, function(response) {

            if (_page) {
                var wp = _element.parent();
                wp.find('.bt-validateProduct').remove();
                wp.find('.bt-rejectProduct').remove();
            } else {
                $('.producto[data-id="' + _id + '"]').parent().remove();
            }
            console.debug('response ajax', response);
        }, function(error) {
            console.error(error);
        });
    });

});