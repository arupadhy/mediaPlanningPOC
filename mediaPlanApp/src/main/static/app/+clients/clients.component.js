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
var planner_service_1 = require("../shared/planner.service");
var ClientsComponent = (function () {
    function ClientsComponent(plannerService) {
        this.plannerService = plannerService;
        this.clients = [];
    }
    ClientsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.plannerService.getClients()
            .then(function (clients) {
            _this.clients = clients;
        });
    };
    ClientsComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-clients',
            templateUrl: 'clients.component.html',
            styleUrls: ['clients.component.css']
        }), 
        __metadata('design:paramtypes', [planner_service_1.PlannerService])
    ], ClientsComponent);
    return ClientsComponent;
}());
exports.ClientsComponent = ClientsComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/+clients/clients.component.js.map