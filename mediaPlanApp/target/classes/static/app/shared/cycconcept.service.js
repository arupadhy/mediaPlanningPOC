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
var common_1 = require('@angular/common');
var CycconceptService = (function () {
    function CycconceptService(_http, _location) {
        this._http = _http;
        this._location = _location;
        this.concepts = [];
        this.isComplete = false;
    }
    CycconceptService.prototype.extractSentence = function (concept, selectedSent) {
        var ref = this;
        var sentences = [];
        concept.sentences.forEach(function (s) {
            var sentSelected = ref.isSentencePreSelected(s, selectedSent);
            if (sentSelected) {
                concept.selected = true;
            }
            var se = new sentence_model_1.Sentence(s.nl, s.cycl, sentSelected);
            sentences.push(se);
        });
        return sentences;
    };
    CycconceptService.prototype.isCycQueryComplete = function () {
        return this.isComplete;
    };
    CycconceptService.prototype.isSentencePreSelected = function (sentence, selectedSentences) {
        var selected = false;
        selectedSentences.forEach(function (s) {
            if (s.name == sentence.nl && s.cycl == sentence.cycl) {
                selected = s.sentenceSelected;
                return;
            }
        });
        return selected;
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
    CycconceptService.prototype.loadCycConcepts = function (targetAudience, availableConcepts, selectedSentences) {
        var _this = this;
        //TODO refactor spinner as a service.
        this._location._subject.emit({ startAnimation: true });
        this.concepts = [];
        //TODO not the best way to keep track of existing concepts.
        var cache = this.updateConceptsCache(availableConcepts);
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
            data.concepts.forEach(function (c, index) {
                var isNew = false;
                var isSelected = false;
                if (availableConcepts && !cache[c.nl]) {
                    isNew = true;
                }
                var sent = ref.extractSentence(c, selectedSentences);
                var con = new concept_model_1.Concept(c.nl, c.index, sent, sessionId, isNew, c.selected);
                if (index == 0) {
                    con.sentences.showSentences = true;
                }
                ref.concepts.push(con);
            });
            spinner.stop();
            _this._location._subject.emit({ stopAnimation: true });
            _this.isComplete = true;
        });
        return this.concepts;
    };
    CycconceptService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http, common_1.Location])
    ], CycconceptService);
    return CycconceptService;
}());
exports.CycconceptService = CycconceptService;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/shared/cycconcept.service.js.map