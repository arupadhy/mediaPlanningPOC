"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var endpoints_1 = require('./endpoints');
var DomainService = (function () {
    function DomainService(_http) {
        var _this = this;
        this._http = _http;
        this._http.get(endpoints_1.endpoints.GET_DOMAIN_DATA + "?name=Strategic Audience")
            .map(function (res) { return res.json(); })
            .subscribe(function (data) { return _this.STRATEGIC_AUDIENCE = data.names ? data.names : data; });
        this._http.get(endpoints_1.endpoints.GET_DOMAIN_DATA + "?name=Business Objective")
            .map(function (res) { return res.json(); })
            .subscribe(function (data) { return _this.BUSINESS_OBJECTIVES = data.names ? data.names : data; });
        this._http.get(endpoints_1.endpoints.GET_DOMAIN_DATA + "?name=KPI")
            .map(function (res) { return res.json(); })
            .subscribe(function (data) { return _this.KPIS = data.names ? data.names : data; });
        this._http.get(endpoints_1.endpoints.GET_DOMAIN_DATA + "?name=Product Name")
            .map(function (res) { return res.json(); })
            .subscribe(function (data) { return _this.PRODUCTS = data.names ? data.names : data; });
        this._http.get(endpoints_1.endpoints.GET_DOMAIN_DATA + "?name=Client Name")
            .map(function (res) { return res.json(); })
            .subscribe(function (data) { return _this.CLIENTS = data.names ? data.names : data; });
    }
    DomainService.prototype.getKPIS = function () {
        return this.KPIS;
    };
    DomainService.prototype.getProducts = function () {
        return this.PRODUCTS;
    };
    DomainService.prototype.getClients = function () {
        return this.CLIENTS;
    };
    DomainService.prototype.getStrategicAudience = function () {
        return this.STRATEGIC_AUDIENCE;
    };
    DomainService.prototype.getBusinessObjectives = function () {
        return this.BUSINESS_OBJECTIVES;
    };
    DomainService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], DomainService);
    return DomainService;
}());
exports.DomainService = DomainService;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/shared/domain.service.js.map