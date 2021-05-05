window.BayShopAPI = window.BayShopAPI || {};
window.BayShopAPI = {
    _base_url : "http://localhost:8080/",

    post : function(endpoint, data, onSuccess, onError){
        return this._request('POST', endpoint, data, onSuccess, onError);
    },
    get : function(endpoint, data, onSuccess, onError){
        return this._request('GET', endpoint, data, onSuccess, onError);
    },
    update : function(endpoint, data, onSuccess, onError){
        return this._request('PUT', endpoint, data, onSuccess, onError);
    },
    delete : function(endpoint, data, onSuccess, onError){
        return this._request('DELETE', endpoint, data, onSuccess, onError);
    },
    template : function(endpoint, data, onSuccess, onError){
        return this._request('GET', endpoint, data, onSuccess, onError, true);
    },

    _request : function(method, endpoint, data = {}, onSuccess, onError, isTemplate){
        var url = BayShopAPI._base_url + endpoint;

        let ajaxCfg = {
            "url"           : url,
            "type"          : method,
            "data"          : data,//JSON.stringify(data),
            "dataType"      : "json",
            "headers"       : {
                'Content-Type': 'application/json; charset=utf-8'
            },
            "contentType"   : "application/json; charset=utf-8",
            "mimeType"      : "application/json",
        };

        if(isTemplate){
            ajaxCfg.headers['Content-Type'] = 'text/html;charset=utf-8';
            ajaxCfg.contentType = 'text/html;charset=utf-8';
            ajaxCfg.mimeType = 'text/html;charset=utf-8';
            delete ajaxCfg.dataType;
        }

        if(method != 'GET'){
            ajaxCfg.headers["X-CSRF-TOKEN"] = config.csrf.value;
        }

        if(onSuccess && typeof onSuccess === 'function'){
            ajaxCfg.success = onSuccess;
        }

        if(onError && typeof onError === 'function'){
            ajaxCfg.error = onError;
        }

        return $.ajax(ajaxCfg);
    }
};