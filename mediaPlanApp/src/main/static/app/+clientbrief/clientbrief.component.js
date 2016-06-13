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
var clientbrief_service_1 = require('../shared/clientbrief.service');
var router_1 = require('@angular/router');
var ClientbriefComponent = (function () {
    function ClientbriefComponent(_clientBrief, _router) {
        this._clientBrief = _clientBrief;
        this._router = _router;
    }
    ClientbriefComponent.prototype.ngOnInit = function () {
        var _this = this;
        console.log("clientbrief component loaded");
        this._clientBrief.loadClientBriefs().subscribe(function (data) { return _this.clientBriefs = data; }, function (error) { return console.log(error); }, function () { return console.log("completed client briefs loading"); });
    };
    ClientbriefComponent.prototype.createMediaPlan = function (client) {
        this._router.navigate(['/mediaplan', { 'target': client }]);
    };
    ClientbriefComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-clientbrief',
            templateUrl: 'clientbrief.component.html',
            styleUrls: ['clientbrief.component.css'],
            providers: [clientbrief_service_1.ClientbriefService]
        }), 
        __metadata('design:paramtypes', [clientbrief_service_1.ClientbriefService, router_1.Router])
    ], ClientbriefComponent);
    return ClientbriefComponent;
}());
exports.ClientbriefComponent = ClientbriefComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/+clientbrief/clientbrief.component.js.map