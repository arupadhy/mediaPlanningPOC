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
var cycavatar_component_1 = require('../cycavatar/cycavatar.component');
var MediaplanComponent = (function () {
    function MediaplanComponent(_router, _routeSegment, _domainService) {
        this._router = _router;
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
        this.client = this._routeSegment.getParam('target');
        this.mediaPlan.pageOne = true;
        this.mediaPlan.nextPage = false;
        this.planInput.planName = '';
        this.productInput.productName = '';
        this.mediaPlan.pageTwo = false;
        this.audienceInput.name = '';
        this.businessObjectiveInput.name = '';
        this.kpiToMeasureInput.name = '';
    }
    MediaplanComponent.prototype.loadCycConcepts = function () {
        var selectedInputs = { 'client': this.client, 'audience': this.audienceInput.name.replace(/,\s*$/, ""), 'product': this.productInput.productName, 'campaign': this.planInput.planName };
        this._router.navigate(['concepts', { target: selectedInputs }]);
    };
    MediaplanComponent.prototype.enablePageOneInput = function () {
        var ref = this;
        $("#planNameInput").autocomplete({
            source: ["ForeEver Faster", "Where’s the Beef?", "We Try Harder", "ForeEver Lasting", " ForEver Name", "Just Do It"]
        });
        $("#productNameInput").autocomplete({
            source: ref._domainService.getProducts(),
            minLength: 1
        });
        if (this.planInput.planName.length > 0 && this.productInput.productName.length > 0) {
            this.mediaPlan.nextPage = true;
        }
        else {
            this.mediaPlan.nextPage = false;
        }
    };
    MediaplanComponent.prototype.submitPageOneInput = function () {
        this.mediaPlan.pageOne = false;
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
            directives: [_mediaplan_summary_1.MediaplanSummaryComponent, cycavatar_component_1.CycavatarComponent],
            providers: [domain_service_1.DomainService]
        }), 
        __metadata('design:paramtypes', [router_1.Router, router_2.RouteSegment, domain_service_1.DomainService])
    ], MediaplanComponent);
    return MediaplanComponent;
}());
exports.MediaplanComponent = MediaplanComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/+mediaplan/mediaplan.component.js.map