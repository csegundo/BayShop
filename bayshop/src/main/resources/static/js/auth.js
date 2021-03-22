$(function(){
    $('.main-div form .change').click(function(){
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
});