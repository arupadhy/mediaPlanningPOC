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
var OutputComponent = (function () {
    function OutputComponent() {
        this.toggleHeader = "+";
    }
    OutputComponent.prototype.toggle = function () {
        if (this.toggleHeader === '-') {
            this.toggleHeader = '+';
        }
        else {
            this.toggleHeader = '-';
        }
    };
    OutputComponent.prototype.ngOnInit = function () {
        this.client = window.localStorage.getItem('client');
        this.audience = window.localStorage.getItem('audience');
        this.product = window.localStorage.getItem('product');
        this.title = window.localStorage.getItem('campaign');
        this.kpi = window.localStorage.getItem('kpi');
        this.objectives = window.localStorage.getItem('objectives');
    };
    OutputComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-output',
            templateUrl: 'output.component.html',
            styleUrls: ['output.component.css']
        }), 
        __metadata('design:paramtypes', [])
    ], OutputComponent);
    return OutputComponent;
}());
exports.OutputComponent = OutputComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/output/output.component.js.map