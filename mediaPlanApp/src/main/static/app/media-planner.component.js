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
var _dashboard_1 = require('./+dashboard');
var router_1 = require('@angular/router');
var _clients_1 = require('./+clients');
var _home_1 = require('./+home');
var _clientbrief_1 = require('./+clientbrief');
var _mediaplan_1 = require('./+mediaplan');
var _concepts_1 = require('./+concepts');
var _suggestion_1 = require('./+suggestion');
var _mriquestion_1 = require('./+mriquestion');
var MediaPlannerAppComponent = (function () {
    function MediaPlannerAppComponent() {
        this.title = 'media-planner works!';
    }
    MediaPlannerAppComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'media-planner-app',
            templateUrl: 'media-planner.component.html',
            styleUrls: ['media-planner.component.css'],
            directives: [router_1.ROUTER_DIRECTIVES],
            providers: [router_1.ROUTER_PROVIDERS]
        }),
        router_1.Routes([
            { path: '/', component: _home_1.HomeComponent },
            { path: '/dashboard', component: _dashboard_1.DashboardComponent },
            { path: '/clients', component: _clients_1.ClientsComponent },
            { path: '/clientbrief', component: _clientbrief_1.ClientbriefComponent },
            { path: '/mediaplan', component: _mediaplan_1.MediaplanComponent },
            { path: '/concepts', component: _concepts_1.ConceptsComponent },
            { path: '/suggestion', component: _suggestion_1.SuggestionComponent },
            { path: '/mriquestion', component: _mriquestion_1.MriquestionComponent }
        ]), 
        __metadata('design:paramtypes', [])
    ], MediaPlannerAppComponent);
    return MediaPlannerAppComponent;
}());
exports.MediaPlannerAppComponent = MediaPlannerAppComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/media-planner.component.js.map