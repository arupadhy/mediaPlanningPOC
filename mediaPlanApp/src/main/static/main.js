"use strict";
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var app_1 = require('./app');
var shared_1 = require('./app/shared');
if (app_1.environment.production) {
    core_1.enableProdMode();
}
platform_browser_dynamic_1.bootstrap(app_1.MediaPlannerAppComponent, [
    http_1.HTTP_PROVIDERS,
    shared_1.PlannerService
]);
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/main.js.map