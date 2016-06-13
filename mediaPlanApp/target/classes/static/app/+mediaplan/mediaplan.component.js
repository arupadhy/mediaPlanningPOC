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
var router_2 = require('@angular/router');
var _mediaplan_summary_1 = require('./+mediaplan-summary');
var domain_service_1 = require('../shared/domain.service');
var common_1 = require('@angular/common');
var MediaplanComponent = (function () {
    function MediaplanComponent(_router, _location, _routerOutletMap, _routeSegment, _domainService) {
        this._router = _router;
        this._location = _location;
        this._routerOutletMap = _routerOutletMap;
        this._routeSegment = _routeSegment;
        this._domainService = _domainService;
        this.planInput = {};
        this.productInput = {};
        this.audienceInput = {};
        this.mediaPlan = {};
        this.businessObjectiveInput = {};
        this.kpiToMeasureInput = {};
        this.showAddedInput = false;
        this.showUserInputs = false;
        var ref = this;
        this.client = window.localStorage.getItem('client');
        this.mediaPlan.campaignName = true;
        this.mediaPlan.productName = false;
        this.mediaPlan.businessInputName = false;
        this.mediaPlan.kpiName = false;
        this.mediaPlan.audienceName = false;
        this.mediaPlan.nextPage = false;
        this.planInput.planName = '';
        this.productInput.productName = '';
        this.mediaPlan.pageTwo = false;
        this.audienceInput.name = '';
        this.businessObjectiveInput.name = '';
        this.kpiToMeasureInput.name = '';
        this.enablePageOneInput();
    }
    MediaplanComponent.prototype.bubbleAnimation = function () {
        //this.cycAvatarComponent.startBubbleAnimation();
    };
    MediaplanComponent.prototype.loadCycConcepts = function () {
        var selectedInputs = { 'client': this.client, 'audience': this.audienceInput.name.replace(/,\s*$/, ""), 'product': this.productInput.productName, 'campaign': this.planInput.planName };
        window.localStorage.setItem('audience', this.audienceInput.name.replace(/,\s*$/, ""));
        window.localStorage.setItem('product', this.productInput.productName);
        window.localStorage.setItem('campaign', this.planInput.planName);
        window.localStorage.setItem('kpi', this.kpiToMeasureInput.name);
        window.localStorage.setItem('objectives', this.businessObjectiveInput.name);
        this._router.navigate(['concepts']);
    };
    MediaplanComponent.prototype.queryAnimation = function () {
        //this.cycAvatarComponent.startQueryAnimation();
    };
    MediaplanComponent.prototype.campaignNameEntered = function () {
        this.enablePageOneInput();
        this.planInput.planName = $('#planNameInput').val();
        if (this.planInput.planName && this.planInput.planName.trim()) {
            this._location._subject.emit({ finishedStep1: true });
        }
        else {
            this._location._subject.emit({ finishedStep1: false });
        }
        this.planInput.nextPage = true;
        // if (this.planInput.planName && this.planInput.planName.length && this.planInput.planName.length> 0) {
        //   this.planInput.nextPage = true;
        // }
        // else {
        //   this.planInput.nextPage = false;
        // }
    };
    MediaplanComponent.prototype.productNameEntered = function () {
        if (this.productInput.productName && this.productInput.productName.trim()) {
            this._location._subject.emit({ finishedStep2: true });
            this.planInput.nextBusinessInput = true;
        }
        else {
            this._location._subject.emit({ finishedStep2: false });
            this.planInput.nextBusinessInput = false;
        }
        //this.enablePageOneInput();
    };
    MediaplanComponent.prototype.nextToProduct = function () {
        this.productNameEntered();
        this.planInput.productInput = false;
        this.planInput.businessInput = true;
    };
    MediaplanComponent.prototype.businessObjectivesEntered = function () {
        if (this.businessObjectiveInput.name && this.businessObjectiveInput.name.trim()) {
            this._location._subject.emit({ finishedStep3: true });
            this.planInput.nextKpiInput = true;
        }
        else {
            this._location._subject.emit({ finishedStep3: false });
            this.planInput.nextKpiInput = false;
        }
        //this.enablePageTwoInput();
    };
    MediaplanComponent.prototype.nextToBusinessInput = function () {
        this.businessObjectivesEntered();
        this.planInput.businessInput = false;
        this.planInput.kpiInput = true;
    };
    MediaplanComponent.prototype.kpisEntered = function () {
        this.kpiToMeasureInput.name = $("#kpiNameInput").val();
        if (this.kpiToMeasureInput.name && this.kpiToMeasureInput.name.trim()) {
            this._location._subject.emit({ finishedStep4: true });
            this.planInput.nextAudienceInput = true;
        }
        else {
            this._location._subject.emit({ finishedStep4: false });
            this.planInput.nextAudienceInput = false;
        }
        this.enablePageTwoInput();
    };
    MediaplanComponent.prototype.nextToKpi = function () {
        this.kpisEntered();
        this.planInput.kpiInput = false;
        this.planInput.audienceInput = true;
    };
    MediaplanComponent.prototype.audienceCriteriaEntered = function () {
        this.audienceInput.name = $("#audienceNameInput").val();
        if (this.audienceInput.name && this.audienceInput.name.trim()) {
            this._location._subject.emit({ finishedStep5: true });
            this.planInput.nextPage = true;
        }
        else {
            this._location._subject.emit({ finishedStep5: false });
            this.planInput.nextPage = false;
        }
        this.enablePageTwoInput();
    };
    MediaplanComponent.prototype.nextToCampaign = function () {
        this.campaignNameEntered();
        this.mediaPlan.campaignName = false;
        this.planInput.productInput = true;
    };
    MediaplanComponent.prototype.enablePageOneInput = function () {
        //console.log(this._router.);
        var ref = this;
        $("#planNameInput").autocomplete({
            source: ["ForeEver Faster", "Go Fresh", "Where’s the Beef?", "We Try Harder", "ForeEver Lasting", " ForEver Name", "Just Do It"],
            select: function (event, ui) {
                ref.planInput.planName = ui.item.value;
            }
        });
        $("#productNameInput").autocomplete({
            source: ref._domainService.getProducts(),
            minLength: 1,
            select: function (event, ui) {
                ref.productInput.productName = ui.item.value;
            }
        });
    };
    MediaplanComponent.prototype.submitPageOneInput = function () {
        this.mediaPlan.campaignName = false;
        this.mediaPlan.pageTwo = true;
        this.mediaPlan.nextPage = false;
        this.planInput.planName = $("#planNameInput").val();
        this.productInput.productName = $("#productNameInput").val();
    };
    MediaplanComponent.prototype.split = function (val) {
        return val.split(/,\s*/);
    };
    MediaplanComponent.prototype.extractLast = function (term) {
        return this.split(term).pop();
    };
    MediaplanComponent.prototype.enablePageTwoInput = function () {
        var ref = this;
        $("#audienceNameInput").autocomplete({
            minLength: 0,
            source: function (request, response) {
                // delegate back to autocomplete, but extract the last term
                response($.ui.autocomplete.filter(ref._domainService.getStrategicAudience(), ref.extractLast(request.term)));
            },
            focus: function () {
                // prevent value inserted on focus
                return false;
            },
            select: function (event, ui) {
                var terms = ref.split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                this.value = terms.join(", ");
                ref.audienceInput.name = terms.toString();
                return false;
            }
        });
        $('#businessObjectiveInput').autocomplete({
            minLength: 0,
            source: function (request, response) {
                // delegate back to autocomplete, but extract the last term
                response($.ui.autocomplete.filter(ref._domainService.getBusinessObjectives(), ref.extractLast(request.term)));
            },
            focus: function () {
                // prevent value inserted on focus
                return false;
            },
            select: function (event, ui) {
                var terms = ref.split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                this.value = terms.join(", ");
                ref.businessObjectiveInput.name = terms.toString();
                return false;
            }
        });
        $('#kpiNameInput').autocomplete({
            minLength: 0,
            source: function (request, response) {
                // delegate back to autocomplete, but extract the last term
                response($.ui.autocomplete.filter(ref._domainService.getKPIS(), ref.extractLast(request.term)));
            },
            focus: function () {
                // prevent value inserted on focus
                return false;
            },
            select: function (event, ui) {
                var terms = ref.split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                this.value = terms.join(", ");
                ref.kpiToMeasureInput.name = terms.toString();
                return false;
            }
        });
        if (this.audienceInput.name.length > 0 && this.businessObjectiveInput.name.length > 0 && this.kpiToMeasureInput.name.length > 0) {
            this.mediaPlan.nextPage = true;
        }
        else {
            this.mediaPlan.nextPage = false;
        }
    };
    MediaplanComponent.prototype.getCycConcepts = function () {
        //this.queryAnimation();
        // let interval:any = setInterval(this.queryAnimation,100);
        this.mediaPlan.pageTwo = false;
        this.mediaPlan.nextPage = false;
        this.audienceInput.name = $("#audienceNameInput").val();
        this.businessObjectiveInput.name = $('#businessObjectiveInput').val();
        this.kpiToMeasureInput.name = $('#kpiNameInput').val();
        this.loadCycConcepts();
    };
    MediaplanComponent.prototype.ngOnInit = function () {
    };
    MediaplanComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-mediaplan',
            templateUrl: 'mediaplan.component.html',
            styleUrls: ['mediaplan.component.css'],
            directives: [_mediaplan_summary_1.MediaplanSummaryComponent],
            providers: [domain_service_1.DomainService]
        }), 
        __metadata('design:paramtypes', [router_1.Router, common_1.Location, router_1.RouterOutletMap, router_2.RouteSegment, domain_service_1.DomainService])
    ], MediaplanComponent);
    return MediaplanComponent;
}());
exports.MediaplanComponent = MediaplanComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/+mediaplan/mediaplan.component.js.map