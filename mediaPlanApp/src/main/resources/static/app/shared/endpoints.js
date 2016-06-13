"use strict";
var app_1 = require("../../app");
var host = 'http://localhost:8080';
var DEMO_HOST = 'http://localhost:8080';
var endpoints = {
    GET_CLIENTS_API: host + "/clients",
    GET_CLIENTS_BRIEF_DATA: host + "/app/shared/clientBrief.json",
    GET_CYC_CONCEPTS: host + "/app/shared/cycconcepts.json",
    GET_CYC_CONCEPTS_DEMO: DEMO_HOST + "/conceptsSvc",
    GET_CYC_QUESTIONS: host + "/app/shared/cycquestions.json",
    GET_CYC_QUESTIONS_DEMO: DEMO_HOST + "/mriFieldConstraintsSvc",
    GET_CYC_RATIONAL_CONCEPTS_DEMO: DEMO_HOST + "/proofSvc",
    GET_REACH_DATA_DEMO: DEMO_HOST + "/mriQueryCheckSvc",
    GET_DOMAIN_DATA: DEMO_HOST + "/getdomain",
    GET_MRI_FIELDS: DEMO_HOST + "/suggestedMriFields",
    GET_AUIDENCE_INSIGHTS: DEMO_HOST + "/audienceInsightsSvc",
    GET_HARDCODED_FIELDS: DEMO_HOST + "/demofields"
};
exports.endpoints = endpoints;
if (app_1.environment.production) {
}
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/shared/endpoints.js.map