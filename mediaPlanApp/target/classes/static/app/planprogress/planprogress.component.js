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
var router_1 = require('@angular/router');
var common_1 = require('@angular/common');
var Cycavatar_component_1 = require('../cycavatar/Cycavatar.component');
var PlanprogressComponent = (function () {
    function PlanprogressComponent(_router, _location, _routerOutletMap) {
        var _this = this;
        this._router = _router;
        this._location = _location;
        this._routerOutletMap = _routerOutletMap;
        this.completionStage = {};
        this.active = {};
        this._location.subscribe(function (x) {
            if ("finishedStep1" in x) {
                _this.completionStage.finishedStep1 = x.finishedStep1;
                _this.active.mediaPlan = true;
            }
            if ("finishedStep2" in x) {
                _this.completionStage.finishedStep2 = x.finishedStep2;
                _this.active.mediaPlan = true;
            }
            if ("finishedStep3" in x) {
                _this.completionStage.finishedStep3 = x.finishedStep3;
                _this.active.mediaPlan = true;
            }
            if ("finishedStep4" in x) {
                _this.completionStage.finishedStep4 = x.finishedStep4;
                _this.active.mediaPlan = true;
            }
            if ("finishedStep5" in x) {
                _this.completionStage.finishedStep5 = x.finishedStep5;
                _this.active.mediaPlan = true;
            }
            if ("exportData" in x) {
                _this.active.mediaPlan = false;
                _this.active.concepts = false;
                _this.active.mri = false;
                _this.active.export = true;
                _this.cycAvatarComponent.updateAvatar();
                _this.cycAvatarComponent.startQueryAnimation();
            }
            if ("stopAnimation" in x) {
                _this.animation = 0;
            }
            if ("startAnimation" in x) {
                _this.animation = 1;
                _this.continueAnimation();
            }
        });
        this._router.changes.subscribe(function () {
            if (_this._location.path().startsWith("/clientbrief")) {
                _this.showAppHeader = true;
            }
            else if (_this._location.path().startsWith("/mediaplan")) {
                _this.completionStage = {};
                _this.active.mediaPlan = false;
                _this.active.mediaPlan = true;
                _this.active.concepts = false;
                _this.showAppHeader = false;
                _this.active.mri = false;
                _this.active.export = false;
                _this.cycAvatarComponent.displayAvatar();
            }
            else if (_this._location.path().startsWith("/concepts")) {
                _this.active.mediaPlan = false;
                _this.showAppHeader = false;
                _this.active.mediaPlan = false;
                _this.active.concepts = true;
                _this.active.mri = false;
                _this.active.export = false;
                _this.cycAvatarComponent.updateAvatar();
            }
            else if (_this._location.path().startsWith("/suggestion")) {
                _this.showAppHeader = false;
                _this.active.mediaPlan = false;
                _this.active.concepts = false;
                _this.active.mri = true;
                _this.active.export = false;
                _this.cycAvatarComponent.updateAvatar();
                _this.cycAvatarComponent.startQueryAnimation();
            }
        });
    }
    PlanprogressComponent.prototype.navigateTo = function (name) {
        this._router.navigate([name]);
    };
    PlanprogressComponent.prototype.continueAnimation = function () {
        var ref = this;
        ref.cycAvatarComponent.startQueryAnimation();
    };
    PlanprogressComponent.prototype.ngOnInit = function () {
        this.active.mediaPlan = false;
        this.active.concepts = false;
        this.active.mri = false;
        this.active.export = false;
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Boolean)
    ], PlanprogressComponent.prototype, "showAppHeader", void 0);
    __decorate([
        core_1.ViewChild(Cycavatar_component_1.CycavatarComponent), 
        __metadata('design:type', Cycavatar_component_1.CycavatarComponent)
    ], PlanprogressComponent.prototype, "cycAvatarComponent", void 0);
    PlanprogressComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'media-plan-progress',
            templateUrl: 'planprogress.component.html',
            styleUrls: ['planprogress.component.css'],
            directives: [Cycavatar_component_1.CycavatarComponent]
        }), 
        __metadata('design:paramtypes', [router_1.Router, common_1.Location, router_1.RouterOutletMap])
    ], PlanprogressComponent);
    return PlanprogressComponent;
}());
exports.PlanprogressComponent = PlanprogressComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/planprogress/planprogress.component.js.map