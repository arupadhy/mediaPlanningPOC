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
var AudienceinsightService = (function () {
    function AudienceinsightService(_http) {
        this._http = _http;
    }
    AudienceinsightService.prototype.getAudienceInsights = function () {
        return this._http.get(endpoints_1.endpoints.GET_AUIDENCE_INSIGHTS)
            .map(function (res) { return res.json(); });
    };
    AudienceinsightService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], AudienceinsightService);
    return AudienceinsightService;
}());
exports.AudienceinsightService = AudienceinsightService;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/shared/audienceinsight.service.js.map