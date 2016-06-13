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
var common_1 = require('@angular/common');
var cycquestion_service_1 = require('../shared/cycquestion.service');
var router_1 = require('@angular/router');
var cycquestion_component_1 = require('../cycquestion/cycquestion.component');
var querybuilder_component_1 = require('../querybuilder/querybuilder.component');
var reachdata_service_1 = require('../shared/reachdata.service');
var mrifield_service_1 = require('../shared/mrifield.service');
var audienceinsight_service_1 = require('../shared/audienceinsight.service');
var SuggestionComponent = (function () {
    function SuggestionComponent(_location, _router, _audienceService, _mriFieldService, _cycQuestionService, _routeSeg, _reachService) {
        this._location = _location;
        this._router = _router;
        this._audienceService = _audienceService;
        this._mriFieldService = _mriFieldService;
        this._cycQuestionService = _cycQuestionService;
        this._routeSeg = _routeSeg;
        this._reachService = _reachService;
        this.color = {};
        this.mappedArray = [];
        this.defaultQueryCondition = true;
        this.fieldSelected = false;
        this.allSelectedFields = [];
        this.mriFields = [];
        this.mriQueryInProgress = false;
        this.audienceInsightQueryInProgress = false;
        this.editAudienceEnabled = false;
        this.recommendedFields = [];
        this.questionsUsedInQuery = [];
        this.color.questions = 'grey';
        this.color.query = 'white';
    }
    SuggestionComponent.prototype.fireNewQuery = function (x) {
        var _this = this;
        this.questionsUsedInQuery = x.questions;
        this.queryConstructedSoFar = x.query;
        this._reachService.getReachData(x.query)
            .subscribe(function (data) {
            _this.reachData = data;
        });
    };
    SuggestionComponent.prototype.editTargetAudience = function (evt) {
        if (evt.which == 13) {
            this.editAudienceEnabled = false;
            this._router.navigate(['concepts', { target: this.selectedInputs }]);
        }
    };
    SuggestionComponent.prototype.enableEdit = function () {
        this.editAudienceEnabled = true;
    };
    SuggestionComponent.prototype.updateQuery = function (query) {
        this.mappedArray['queryString'] = query || this.mappedArray['queryString'];
        console.log(this.mappedArray);
    };
    SuggestionComponent.prototype.ngOnInit = function () {
        var audience = window.localStorage.getItem('audience');
        var product = window.localStorage.getItem('product');
        var campaign = window.localStorage.getItem('campaign');
        this.selectedInputs = { audience: audience, product: product, campaign: campaign };
        var q = JSON.parse(window.localStorage.getItem('target'));
        this.sessionId = window.localStorage.getItem('session');
        this.questions = this._cycQuestionService.loadQuestionsFromCyc(q, this.sessionId);
    };
    SuggestionComponent.prototype.loadMriFields = function () {
        var _this = this;
        this.mriQueryInProgress = true;
        this._location._subject.emit({ exportData: true });
        this._mriFieldService.getHardCodedFields().split(';').forEach(function (x) { return _this.recommendedFields.push({ value: x }); });
        this.mriFields = this._mriFieldService.getMriFields(this.questionsUsedInQuery);
    };
    SuggestionComponent.prototype.loadInsight = function () {
        var _this = this;
        console.log("loading insight");
        this.audienceInsightQueryInProgress = true;
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        this._audienceService.getAudienceInsights()
            .subscribe(function (data) {
            _this.audienceInsights = data.audienceInsights;
            _this.contrastingFacts = _this.audienceInsights.CONTRASTING_FACTS;
            _this.interestingFacts = _this.audienceInsights.INTERESTING_FACTS;
            _this.interestingValues = _this.audienceInsights.INTERESTING_VALUES;
            spinner.stop();
        });
        ;
    };
    SuggestionComponent.prototype.selectField = function (field) {
        console.log(field);
        console.log(field.fieldSelected);
        field.fieldSelected = !field.fieldSelected;
        console.log(field);
        console.log(field.fieldSelected);
        if (field.fieldSelected == true) {
            console.log('true');
            this.allSelectedFields.push(field);
        }
        else if (field.fieldSelected == false) {
            console.log('false');
            var index = this.allSelectedFields.indexOf(field);
            this.allSelectedFields.splice(index, 1);
        }
    };
    SuggestionComponent.prototype.selectAnswer = function (answer) {
        answer.recommended = !answer.recommended;
    };
    SuggestionComponent.prototype.selectQuestion = function (q) {
        q.selected = !q.selected;
    };
    SuggestionComponent.prototype.showOutputs = function () {
        this._router.navigate(['output']);
    };
    SuggestionComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-suggestion',
            templateUrl: 'suggestion.component.html',
            styleUrls: ['suggestion.component.css'],
            providers: [cycquestion_service_1.CycquestionService, reachdata_service_1.ReachdataService, mrifield_service_1.MrifieldService, audienceinsight_service_1.AudienceinsightService],
            directives: [cycquestion_component_1.CycquestionComponent, querybuilder_component_1.QuerybuilderComponent]
        }), 
        __metadata('design:paramtypes', [common_1.Location, router_1.Router, audienceinsight_service_1.AudienceinsightService, mrifield_service_1.MrifieldService, cycquestion_service_1.CycquestionService, router_1.RouteSegment, reachdata_service_1.ReachdataService])
    ], SuggestionComponent);
    return SuggestionComponent;
}());
exports.SuggestionComponent = SuggestionComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/+suggestion/suggestion.component.js.map