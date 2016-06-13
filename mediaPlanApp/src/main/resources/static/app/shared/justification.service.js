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
var JustificationService = (function () {
    function JustificationService(_http) {
        this._http = _http;
    }
    JustificationService.prototype.fetchJustificationForConcept = function (index, session) {
        var proofs;
        var param = new http_1.URLSearchParams();
        param.set('index', index);
        param.set('sessionId', session);
        return this._http.get(endpoints_1.endpoints.GET_CYC_RATIONAL_CONCEPTS_DEMO, { search: param })
            .map(function (res) { return res.json(); });
    };
    JustificationService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], JustificationService);
    return JustificationService;
}());
exports.JustificationService = JustificationService;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/shared/justification.service.js.map