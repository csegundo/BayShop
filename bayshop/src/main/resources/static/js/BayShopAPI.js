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
    /**
     * Exclusivamente para solicitar plantillas para los popups
     * @param {string} name nombre del template (.html) ya que Spring devolverá el nombre de la plantilla
     * @param {function} callback que devuelve el contenido de la peticion
     * @returns 
     */
    template : function(name, callback){
        return this._request('VIEW', name, false, callback);
    },

    _request : function(method, endpoint, data, onSuccess, onError){
        var url         = BayShopAPI._base_url + endpoint,
            isTemplate  = method === 'VIEW';

        url = isTemplate ? (BayShopAPI._base_url + 'templates/?name=' + endpoint) : url;

        let ajaxCfg = {
            "url"           : url,
            "async"         : data.async || true,
            "type"          : isTemplate ? 'GET' : method,
            "data"          : JSON.stringify(data),
            "dataType"      : "json",
            "headers"       : {
                'Content-Type': 'application/json; charset=utf-8'
            },
            "mimeType"      : "application/json",
        };

        // Cambiamos algunos campos para solicitar la plantilla
        if(isTemplate){
            ajaxCfg.dataType = "html";
            ajaxCfg.headers = {
                'Content-Type': 'text/html; charset=utf-8'
            };
            ajaxCfg.mimeType = 'text/html';
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