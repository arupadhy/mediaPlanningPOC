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
var http_2 = require('@angular/http');
var endpoints_1 = require('./endpoints');
var concept_model_1 = require('../+concepts/concept.model');
var sentence_model_1 = require('../+concepts/sentence.model');
var CycconceptService = (function () {
    function CycconceptService(_http) {
        this._http = _http;
        this.concepts = [];
        this.isComplete = false;
    }
    CycconceptService.prototype.extractSentence = function (concept) {
        var sentences = [];
        concept.sentences.forEach(function (s) {
            sentences.push(new sentence_model_1.Sentence(s.nl, s.cycl));
        });
        return sentences;
    };
    CycconceptService.prototype.isCycQueryComplete = function () {
        return this.isComplete;
    };
    CycconceptService.prototype.updateConceptsCache = function (availableConcepts) {
        var conceptsCache = {};
        if (availableConcepts && availableConcepts.length) {
            availableConcepts.forEach(function (c) {
                var name = c.name;
                conceptsCache[name] = true;
            });
        }
        return conceptsCache;
    };
    CycconceptService.prototype.loadCycConcepts = function (targetAudience, availableConcepts) {
        var _this = this;
        //TODO refactor spinner as a service.
        this.concepts = [];
        //TODO not the best way to keep track of existing concepts.
        var cache = this.updateConceptsCache(availableConcepts);
        console.log(cache);
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        var ref = this;
        var params = new http_2.URLSearchParams();
        params.set('text', targetAudience);
        this._http.get(endpoints_1.endpoints.GET_CYC_CONCEPTS_DEMO, {
            search: params
        }).map(function (res) { return res.json(); })
            .subscribe(function (data) {
            if (data.statusCode !== 200) {
                ref.errorMessage = 'Server encountered an error';
                return;
            }
            var sessionId = data.sessionId;
            data.concepts.forEach(function (c) {
                var isNew = false;
                if (availableConcepts && !cache[c.nl]) {
                    console.log("this is new concept");
                    console.log(cache);
                    isNew = true;
                }
                ref.concepts.push(new concept_model_1.Concept(c.nl, c.index, ref.extractSentence(c), sessionId, isNew));
            });
            spinner.stop();
            _this.isComplete = true;
            console.log("concepts:");
            console.log(_this.concepts);
        });
        return this.concepts;
    };
    CycconceptService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], CycconceptService);
    return CycconceptService;
}());
exports.CycconceptService = CycconceptService;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/shared/cycconcept.service.js.map