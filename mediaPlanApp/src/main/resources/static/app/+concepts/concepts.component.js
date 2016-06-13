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
var cycconcept_service_1 = require('../shared/cycconcept.service');
var router_1 = require('@angular/router');
var justification_service_1 = require('../shared/justification.service');
var cycavatar_component_1 = require('../cycavatar/cycavatar.component');
var ConceptsComponent = (function () {
    function ConceptsComponent(_justificationService, _cycConceptService, _router, _routeParams) {
        this._justificationService = _justificationService;
        this._cycConceptService = _cycConceptService;
        this._router = _router;
        this._routeParams = _routeParams;
        this.editActive = false;
        this.sentenceRationale = false;
        this.editAudienceEnabled = false;
        this.sentenceSelected = false;
        this.allSelectedSentences = [];
        this.showSentences = false;
        this.isProofViewOpen = false;
        this.conceptQueryInProgress = false;
        var audience = window.localStorage.getItem('audience');
        var product = window.localStorage.getItem('product');
        var campaign = window.localStorage.getItem('campaign');
        this.selectedInputs = { audience: audience, product: product, campaign: campaign };
    }
    ConceptsComponent.prototype.disableEdit = function (evt) {
        if (evt.which == 13) {
            this.editAudienceEnabled = false;
            this.conceptQueryInProgress = true;
            window.localStorage.setItem('audience', this.selectedInputs.audience);
            this.fetchConceptsFromCyc();
        }
    };
    // public prepareList() {
    //     $('#expList').find('li:has(ul)')
    //     .click( function(event) {
    //         if (this == event.target) {
    //             $(this).toggleClass('expanded');
    //             $(this).children('ul').toggle('medium');
    //         }
    //         return false;
    //     })
    //     .addClass('collapsed')
    //     .children('ul').hide();
    //     //Create the button funtionality
    //     $('#expandList')
    //     .click( function() {
    //         $('.collapsed').addClass('expanded');
    //         $('.collapsed').children().show('medium');
    //     })
    //     $('#collapseList')
    //     .click( function() {
    //         $('.collapsed').removeClass('expanded');
    //         $('.collapsed').children().hide('medium');
    //     })
    // };
    ConceptsComponent.prototype.enableEdit = function () {
        this.editAudienceEnabled = true;
    };
    ConceptsComponent.prototype.editSelectedAudience = function () {
        this.editActive = true;
    };
    ConceptsComponent.prototype.getRational = function () {
        return this.rationale;
    };
    ConceptsComponent.prototype.showRationale = function (concept) {
        var _this = this;
        var ref = this;
        this.isProofViewOpen = true;
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        this._justificationService
            .fetchJustificationForConcept(concept.index, concept.sessionId)
            .subscribe(function (data) {
            _this.rationale = data.proofs;
            spinner.stop();
            $("#proofDialog").dialog({
                modal: true,
                position: 'center',
                width: 1000,
                height: 500,
                title: "Rational for " + concept.name,
                show: {
                    effect: "blind",
                    duration: 1000
                },
                hide: {
                    effect: "explode",
                    duration: 1000
                },
                close: function (event, ui) {
                    ref.isProofViewOpen = false;
                }
            });
        });
        // concept.sentenceRationale = !concept.sentenceRationale;
    };
    ConceptsComponent.prototype.hideRationale = function (concept) {
        concept.sentenceRationale = false;
    };
    ConceptsComponent.prototype.selectSentence = function (concept, sentence) {
        this.session = concept.sessionId;
        sentence.sentenceSelected = !sentence.sentenceSelected;
        if (sentence.sentenceSelected == true) {
            concept.selectedSentencesCount++;
            this.allSelectedSentences.push(sentence);
            concept.selected = true;
        }
        else if (sentence.sentenceSelected == false) {
            var index = this.allSelectedSentences.indexOf(sentence);
            this.allSelectedSentences.splice(index, 1);
            concept.selectedSentencesCount--;
        }
    };
    ConceptsComponent.prototype.toggleSentences = function (sentenceList) {
        sentenceList.showSentences = !sentenceList.showSentences;
    };
    ConceptsComponent.prototype.toggleConcept = function (concept) {
        concept.sentences.showSentences = !concept.sentences.showSentences;
    };
    ConceptsComponent.prototype.loadCycQuestions = function () {
        window.localStorage.setItem('target', JSON.stringify(this.allSelectedSentences));
        window.localStorage.setItem('session', this.session);
        this._router.navigate(['suggestion']);
    };
    ConceptsComponent.prototype.fetchConceptsFromCyc = function () {
        this.availableConcepts = this.concepts;
        this.concepts = this._cycConceptService.loadCycConcepts(this.selectedInputs.audience, this.availableConcepts, this.allSelectedSentences);
    };
    ConceptsComponent.prototype.ngOnInit = function () {
        this.fetchConceptsFromCyc();
    };
    ConceptsComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-concepts',
            templateUrl: 'concepts.component.html',
            styleUrls: ['concepts.component.css'],
            providers: [cycconcept_service_1.CycconceptService, justification_service_1.JustificationService],
            directives: [cycavatar_component_1.CycavatarComponent]
        }), 
        __metadata('design:paramtypes', [justification_service_1.JustificationService, cycconcept_service_1.CycconceptService, router_1.Router, router_1.RouteSegment])
    ], ConceptsComponent);
    return ConceptsComponent;
}());
exports.ConceptsComponent = ConceptsComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/+concepts/concepts.component.js.map